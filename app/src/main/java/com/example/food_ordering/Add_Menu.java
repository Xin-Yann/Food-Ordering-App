package com.example.food_ordering;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_Menu extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;
    private Uri selectedImageUri;
    FirebaseFirestore firestore;
    Button addButton;
    Button selectImageButton;
    Spinner selectMenuType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_add_menu);

        firestore = FirebaseFirestore.getInstance();

        selectImageButton = findViewById(R.id.selectImageBtn);

        selectImageButton.setOnClickListener(v -> {
            // Create an intent to open the file picker or camera, depending on your requirements.
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // or Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // For camera

            // Start the intent for result
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
            // Handle the selected image here
            selectedImageUri = data.getData();

            // You can display the selected image in an ImageView
            ImageView imageView = findViewById(R.id.selectedImage);
            imageView.setImageURI(selectedImageUri);

            // You can also upload the selected image to a server or perform any other desired actions.
            // Ensure you have the necessary permissions and code to handle image uploads.
        }
    }

    private void addMenu() {
        Map<String, Object> menus = new HashMap<>();
        // Get data from user input fields and add it to the 'menu' map
        String menuCategory = ((android.widget.Spinner) findViewById(R.id.selectMenuType)).getSelectedItem().toString();
        String menuImage = selectedImageUri != null ? selectedImageUri.toString() : "";
        String menuId = ((TextInputEditText) findViewById(R.id.inputMenuId)).getText().toString();
        String menuName = ((TextInputEditText) findViewById(R.id.inputMenuName)).getText().toString();
        String menuDescription = ((TextInputEditText) findViewById(R.id.inputMenuDescription)).getText().toString();
        String menuPrice = ((TextInputEditText) findViewById(R.id.inputmenuPrice)).getText().toString();

        // Create a menu object with the added data
        Map<String, Object> menu = new HashMap<>();
        menu.put("menu_category", menuCategory);
        menu.put("menu_image", menuImage);
        menu.put("menu_id", menuId);
        menu.put("menu_name", menuName);
        menu.put("menu_description", menuDescription);
        menu.put("menu_price", menuPrice);

        firestore.collection("menu").add(menu).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Menu added successfully", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to add menu", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void toHome(View view){
        Intent intent = new Intent(this, Admin_home.class);
        Button toHome = findViewById(R.id.home_page);
        startActivity(intent);
    }
}