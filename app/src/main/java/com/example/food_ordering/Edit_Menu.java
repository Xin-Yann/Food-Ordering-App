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
            // Create an intent to open the file picker or camera, depending on your requirements.
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // For file picker
            // or Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // For camera

            // Start the intent for result
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        editButton = findViewById(R.id.editBtn);

        editButton.setOnClickListener(view -> {
            // Retrieve the dynamic menu item ID from the Intent extra
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
            // Handle the selected image here
            selectedImageUri = data.getData();

            // You can display the selected image in an ImageView
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
                        // Image upload successful
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString(); // This is the shareable URL

                            Map<String, Object> updatedMenu = new HashMap<>();
                            // Gather data from input fields
                            String menuImage = imageUrl; // Use the URL obtained from Firebase Storage
                            String menuName = ((TextInputEditText) findViewById(R.id.inputMenuName)).getText().toString();
                            String menuDetail = ((TextInputEditText) findViewById(R.id.inputMenuDescription)).getText().toString();
                            String menuPrice = ((TextInputEditText) findViewById(R.id.inputmenuPrice)).getText().toString();

                            // You may also need to retrieve the menu ID to identify which menu item to edit

                            // Create a menu object with the updated data
                            updatedMenu.put("menu_image", menuImage);
                            updatedMenu.put("menu_name", menuName);
                            updatedMenu.put("menu_detail", menuDetail);
                            updatedMenu.put("menu_price", menuPrice);

                            // Update the menu item in Firestore
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
