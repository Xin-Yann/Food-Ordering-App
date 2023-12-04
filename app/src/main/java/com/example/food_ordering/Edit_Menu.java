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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

        selectImageButton = findViewById(R.id.selectImageBtn);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE);
        });

        // Retrieve the itemId from the Intent
        menuItemId = getIntent().getStringExtra("itemId");

        if (menuItemId != null) {
            // Fetch the existing data and populate the UI elements
            fetchDataAndPopulateUI();

            editButton = findViewById(R.id.editBtn);

            editButton.setOnClickListener(view -> {
                if (menuItemId != null) {
                    editMenu(menuItemId);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid menu item ID", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Invalid menu item ID", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void fetchDataAndPopulateUI() {
        Spinner selectMenuType = findViewById(R.id.selectMenuType);
        String[] selectionOptions = {"Main Dish", "Beverage", "Dessert"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectionOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMenuType.setAdapter(adapter);

        firestore.collection("menu")
                .whereEqualTo("menu_id", menuItemId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        String menuCategory = documentSnapshot.getString("menu_category");
                        String menuId = documentSnapshot.getString("menu_id");
                        String menuName = documentSnapshot.getString("menu_name");
                        String menuDetail = documentSnapshot.getString("menu_detail");
                        String menuPrice = documentSnapshot.getString("menu_price");

                        TextInputEditText inputMenuId = findViewById(R.id.inputMenuId);
                        TextInputEditText inputMenuName = findViewById(R.id.inputMenuName);
                        TextInputEditText inputMenuDescription = findViewById(R.id.inputMenuDescription);
                        TextInputEditText inputMenuPrice = findViewById(R.id.inputmenuPrice);

                        int categoryIndex = getIndexOfCategory(menuCategory, selectionOptions);
                        selectMenuType.setSelection(categoryIndex);

                        inputMenuId.setText(menuId);
                        inputMenuName.setText(menuName);
                        inputMenuDescription.setText(menuDetail);
                        inputMenuPrice.setText(menuPrice);
                    } else {
                        Toast.makeText(getApplicationContext(), "Menu item not found", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Failed to fetch menu data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private int getIndexOfCategory(String category, String[] categories) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();

            ImageView imageView = findViewById(R.id.selectedImage);
            imageView.setImageURI(selectedImageUri);
        }
    }

    private void editMenu(String menuItemId) {
        if (selectedImageUri != null) {
            storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis());

            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            Map<String, Object> updatedMenu = new HashMap<>();
                            String menuImage = imageUrl;
                            String menuName = ((TextInputEditText) findViewById(R.id.inputMenuName)).getText().toString();
                            String menuDetail = ((TextInputEditText) findViewById(R.id.inputMenuDescription)).getText().toString();
                            String menuPrice = ((TextInputEditText) findViewById(R.id.inputmenuPrice)).getText().toString();
                            String menuCategory = ((Spinner) findViewById(R.id.selectMenuType)).getSelectedItem().toString();

                            updatedMenu.put("menu_image", menuImage);
                            updatedMenu.put("menu_name", menuName);
                            updatedMenu.put("menu_detail", menuDetail);
                            updatedMenu.put("menu_price", menuPrice);
                            updatedMenu.put("menu_category", menuCategory);

                            firestore.collection("menu")
                                    .whereEqualTo("menu_id", menuItemId)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                            String documentId = documentSnapshot.getId();
                                            DocumentReference docRef = firestore.collection("menu").document(documentId);

                                            docRef.update(updatedMenu)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Toast.makeText(getApplicationContext(), "Menu edited successfully", Toast.LENGTH_LONG).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(getApplicationContext(), "Failed to edit menu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Menu item not found for menu_id: " + menuItemId, Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to fetch menu data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        finish();
                                    });
                        });
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            // If no new image is selected, update other details directly
            Map<String, Object> updatedMenu = new HashMap<>();
            String menuName = ((TextInputEditText) findViewById(R.id.inputMenuName)).getText().toString();
            String menuDetail = ((TextInputEditText) findViewById(R.id.inputMenuDescription)).getText().toString();
            String menuPrice = ((TextInputEditText) findViewById(R.id.inputmenuPrice)).getText().toString();
            String menuCategory = ((Spinner) findViewById(R.id.selectMenuType)).getSelectedItem().toString();

            updatedMenu.put("menu_name", menuName);
            updatedMenu.put("menu_detail", menuDetail);
            updatedMenu.put("menu_price", menuPrice);
            updatedMenu.put("menu_category", menuCategory);

            firestore.collection("menu")
                    .whereEqualTo("menu_id", menuItemId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                            String documentId = documentSnapshot.getId();
                            DocumentReference docRef = firestore.collection("menu").document(documentId);

                            docRef.update(updatedMenu)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getApplicationContext(), "Menu edited successfully", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to edit menu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Menu item not found for menu_id: " + menuItemId, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to fetch menu data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    });
        }
    }


    public void toHome(View view) {
        Intent intent = new Intent(this, Admin_home.class);
        Button toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }
}
