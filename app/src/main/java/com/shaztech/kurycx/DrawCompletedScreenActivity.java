package com.shaztech.kurycx;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DrawCompletedScreenActivity extends AppCompatActivity {
    String getWinnerGuid, getWeek, profileUrl;
    TextView nameTv, phoneTv, addressTv, drawWeekTv;
    ImageView dpImageView;
    FirebaseFirestore db;
    private AlertDialog youWinDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_completed_screen);
        nameTv = findViewById(R.id.drawWinnerNameTextView);
        phoneTv = findViewById(R.id.drawWinnerPhoneNumberTextView);
        addressTv = findViewById(R.id.drawWinnerLocationTextView);
        dpImageView = findViewById(R.id.drawWinnerProfileImageView);
        drawWeekTv = findViewById(R.id.textViewDrawWeek);

        Intent intent = getIntent();
        getWinnerGuid = intent.getStringExtra("winner_guid");
        getWeek = intent.getStringExtra("week");

        // Toast.makeText(getApplicationContext(), "RESULT IS READY: "+getWinnerGuid, Toast.LENGTH_LONG).show();
        updateUi();

        if(getWinnerGuid.equals(FirebaseAuth.getInstance().getUid())){
            showYouWinPopup();
        }
    }

    private void updateUi() {

        db = FirebaseFirestore.getInstance();
        db.collection("Customers")
                .whereEqualTo("guid", getWinnerGuid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                nameTv.setText(document.getString("name"));
                                phoneTv.setText(document.getString("phone"));
                                addressTv.setText(document.getString("address"));
                                drawWeekTv.setText(String.format("Draw Winner of the %s is", getWeek));
                                profileUrl = document.getString("dp_link");
                                updateProfilePicture(profileUrl);


                            }
                        } else {

                            Toast.makeText(getApplicationContext(), "Error updating UI", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
    private void updateProfilePicture(String profileUrl) {
        //Toast.makeText(this, "dp"+profileUrl, Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(profileUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.icon_account) // Placeholder image
                        .error(R.drawable.icon_account) // Error image in case of loading failure
                )
                .into(dpImageView);
    }

    private void showYouWinPopup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.you_winner_popup, null));
        builder.setCancelable(true);
        // builder.setCancelable(true);

        youWinDialog = builder.create();
        youWinDialog.show();

    }
}