package com.example.food_ordering;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Edit_Menu extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;
    private Uri selectedImageUri;
    FirebaseFirestore firestore;
    String menuItemId;
    Button editButton;
    Button selectImageButton;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_edit_menu);

        firestore = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);

        selectImageButton = findViewById(R.id.selectImageBtn);

        selectImageButton.setOnClickListener(v -> {
            // Create intent to open photo
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        editButton = findViewById(R.id.editBtn);

        editButton.setOnClickListener(view -> {
            // Retrieve dynamic Id
            menuItemId = getIntent().getStringExtra("menuItemId");

            if (menuItemId != null) {
                editMenu(menuItemId);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid menu item ID", Toast.LENGTH_LONG).show();
            }
        });
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

    private void editMenu(String menuItemId) {
        if (selectedImageUri != null) {
            storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis()); // Set a unique path for each image

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString(); // Shareable URL

                            Map<String, Object> updatedMenu = new HashMap<>();
                            // Gather updated data from input fields
                            String menuImage = imageUrl;
                            String menuName = ((TextInputEditText) findViewById(R.id.inputMenuName)).getText().toString();
                            String menuDetail = ((TextInputEditText) findViewById(R.id.inputMenuDescription)).getText().toString();
                            String menuPrice = ((TextInputEditText) findViewById(R.id.inputmenuPrice)).getText().toString();


                            // Replace details with updated data
                            updatedMenu.put("menu_image", menuImage);
                            updatedMenu.put("menu_name", menuName);
                            updatedMenu.put("menu_detail", menuDetail);
                            updatedMenu.put("menu_price", menuPrice);

                            firestore.collection("menu").document(this.menuItemId)
                                    .update(updatedMenu)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Menu edited successfully", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed to edit menu", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });
                    });
        }
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, Admin_home.class);
        Button toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }
}
