package com.example.food_ordering;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Add_Menu extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;
    private Uri selectedImageUri;
    FirebaseFirestore firestore;
    Button addButton;
    Button selectImageButton;
    Spinner selectMenuType;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_add_menu);

        firestore = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);

        selectImageButton = findViewById(R.id.selectImageBtn);

        selectImageButton.setOnClickListener(v -> {
            // Create intent to open photo
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        addButton = findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMenu();
            }
        });

        selectMenuType = findViewById(R.id.selectMenuType);
        String[] selectionOptions = {"Main Dish", "Beverage", "Dessert"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectionOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMenuType.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            // Handle selected image
            selectedImageUri = data.getData();

            // Display selected image
            ImageView imageView = findViewById(R.id.selectedImage);
            imageView.setImageURI(selectedImageUri);
        }
    }

    private void addMenu() {
        if (selectedImageUri != null) {
            storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis()); // Set a unique path for each image

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image upload successful
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString(); // This is the shareable URL

                            // Then save URL in Firestore
                            Map<String, Object> menu = new HashMap<>();
                            // Get data from user input
                            String menuCategory = selectMenuType.getSelectedItem().toString();
                            String menuImage = imageUrl;
                            String menuId = ((TextInputEditText) findViewById(R.id.inputMenuId)).getText().toString();
                            String menuName = ((TextInputEditText) findViewById(R.id.inputMenuName)).getText().toString();
                            String menuDetail = ((TextInputEditText) findViewById(R.id.inputMenuDescription)).getText().toString();
                            String menuPrice = ((TextInputEditText) findViewById(R.id.inputmenuPrice)).getText().toString();

                            // Create menu object with the added data
                            menu.put("menu_category", menuCategory);
                            menu.put("menu_image", menuImage);
                            menu.put("menu_id", menuId);
                            menu.put("menu_name", menuName);
                            menu.put("menu_detail", menuDetail);
                            menu.put("menu_price", menuPrice);

                            firestore.collection("menu").add(menu).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Menu added successfully", Toast.LENGTH_LONG).show();
                                    // Clear text while data is added successful
                                    TextInputEditText inputedMenuId = findViewById(R.id.inputMenuId);
                                    TextInputEditText inputedMenuName = findViewById(R.id.inputMenuName);
                                    TextInputEditText inputedMenuDetail = findViewById(R.id.inputMenuDescription);
                                    TextInputEditText inputedMenuPrice = findViewById(R.id.inputmenuPrice);

                                    inputedMenuId.getText().clear();
                                    inputedMenuName.getText().clear();
                                    inputedMenuDetail.getText().clear();
                                    inputedMenuPrice.getText().clear();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to add menu", Toast.LENGTH_LONG).show();
                                }
                            });
                        });
                    });
        }
    }

    public void toHome(View view){
        Intent intent = new Intent(this, Admin_home.class);
        Button toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }
}