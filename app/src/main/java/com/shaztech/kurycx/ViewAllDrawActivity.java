package com.shaztech.kurycx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shaztech.kurycx.adapters.ViewAllDrawAdapter;
import com.shaztech.kurycx.models.Customers;

import java.util.ArrayList;
import java.util.List;

public class ViewAllDrawActivity extends AppCompatActivity {
    private RecyclerView allDrawRecView;
    private ArrayList<Customers> customersArrayList;
    private ViewAllDrawAdapter viewAllDrawAdapter;
    private FirebaseFirestore db;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_draw);
        // init
        backBtn=findViewById(R.id.allDrawBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        allDrawRecView=findViewById(R.id.allDrawRecyclerView);

        db = FirebaseFirestore.getInstance();
        customersArrayList=new ArrayList<>();

        allDrawRecView.setHasFixedSize(true);
        allDrawRecView.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.

        viewAllDrawAdapter=new ViewAllDrawAdapter(customersArrayList, this);
        allDrawRecView.setAdapter(viewAllDrawAdapter);

        // get data from firebase

        db.collection("Customers")
                .whereEqualTo("is_draw", "yes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Customers c = d.toObject(Customers.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                customersArrayList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            viewAllDrawAdapter.notifyDataSetChanged();
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(ViewAllDrawActivity.this, "DRAW NOT STARTED", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewAllDrawActivity.this, "Failed to get Draw History", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    public void goBack() {
        getOnBackPressedDispatcher().onBackPressed();
    }
}