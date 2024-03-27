package com.shaztech.kurycx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowDrawGeneratorActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textView;
    List<String> fieldValues;

    Handler handler;
    int delay = 200; //milliseconds
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_draw_generator);

        backBtn=findViewById(R.id.rcBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        db = FirebaseFirestore.getInstance();
        textView = findViewById(R.id.textViewLoadingNames);
        handler = new Handler();

        fetchFieldValueFromMultipleDocuments();

    }

    private void goBack() {
        getOnBackPressedDispatcher().onBackPressed();
    }

    private void fetchFieldValueFromMultipleDocuments() {
        CollectionReference collectionReference = db.collection("Customers");

        collectionReference.whereEqualTo("is_draw", "no").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    fieldValues = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            String fieldValue = document.getString("name");
                            if (fieldValue != null) {
                                fieldValues.add(fieldValue);
                            }

                        }
                        //
                    }
                    showDelayedTextView();
                    // displayFieldValues(fieldValues);

                } else {
                    // Handle errors
                }
            }
        });
    }

    private void showDelayedTextView() {

        handler = new Handler();
        for (int i = 0; i < fieldValues.size(); i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                public void run() {
                    textView.setText(fieldValues.get(index));
                    if (index == fieldValues.size() - 1) {
                        getDrawDetails();
                    }

                }
            }, delay * i);

        }

    }

    private void getDrawDetails() {
        db = FirebaseFirestore.getInstance();
        db.collection("app_admin_settings")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String winnerGuid = document.getString("winner_guid");
                                String winnerWeek = document.getString("current_week");
                                String isWinnerAnnounced = document.getString("winner_announced_for_current_month");
                                if (Objects.equals(isWinnerAnnounced, "yes")) {
                                    Intent i = new Intent(ShowDrawGeneratorActivity.this, DrawCompletedScreenActivity.class);
                                    i.putExtra("winner_guid", winnerGuid);
                                    i.putExtra("week", winnerWeek);
                                    startActivity(i);
                                    finish();

                                } else {
                                    textView.setText("WINNER WILL BE ANNOUNCED ON SUNDAY");

                                }


                            }
                        } else {
                            // Log.d("TAGviewcx", "Error getting documents: ", task.getException());
                            Toast.makeText(getApplicationContext(), "error in finding winner", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }


}