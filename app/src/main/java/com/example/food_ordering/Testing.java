package com.example.food_ordering;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Testing extends AppCompatActivity {
    TextView fullName, lastName, description;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);

        fStore = FirebaseFirestore.getInstance();
        fullName = findViewById(R.id.fullname); // Replace with the actual ID of your TextView
        lastName = findViewById(R.id.lastname); // Replace with the actual ID of your TextView
        description = findViewById(R.id.description); // Replace with the actual ID of your TextView

        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    StringBuilder allUserData = new StringBuilder();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String firstName = document.getString("firstName");
                        String userLastName = document.getString("lastName");
                        String userDescription = document.getString("description");

                        allUserData.append("First Name: ").append(firstName).append("\n");
                        allUserData.append("Last Name: ").append(userLastName).append("\n");
                        allUserData.append("Description: ").append(userDescription).append("\n\n");
                    }

                    // Display the data from all documents in your TextView
                    fullName.setText(allUserData.toString());
                } else {
                    // Handle the case where the query was not successful
                }
            }
        });
    }
}
