package com.shaztech.kurycx;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfilePageActivity extends AppCompatActivity {
    FirebaseFirestore db;

    EditText profileNameTv, profilePhoneTv, profilePlaceTv;
    TextView usernameTv;
    ImageButton backBtn;
    private Button saveProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        //
        profileNameTv = findViewById(R.id.profileNameEditText);
        profilePhoneTv = findViewById(R.id.profilePhoneEditText);
        profilePlaceTv = findViewById(R.id.profilePlaceEditText);
        usernameTv = findViewById(R.id.textViewUsername);
        backBtn=findViewById(R.id.backButtonImageButton);
        saveProfileBtn=findViewById(R.id.buttonSaveProfile);
        //
        updateUi();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        // save profile
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "!!! RESTRICTED !!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUi() {

        db = FirebaseFirestore.getInstance();
        db.collection("Customers").whereEqualTo("guid", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        profileNameTv.setText(document.getString("name"));
                        profilePhoneTv.setText(document.getString("phone"));
                        profilePlaceTv.setText(document.getString("address"));
                        usernameTv.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Error updating UI", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    public void goBack() {
getOnBackPressedDispatcher().onBackPressed();
    }

}