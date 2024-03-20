package com.shaztech.kurycx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shaztech.kurycx.adapters.CustomerRecentPaymentAdapter;
import com.shaztech.kurycx.models.CustomerPayments;

import java.util.ArrayList;
import java.util.List;

public class RecentPaymentsScreenActivity extends AppCompatActivity {
    private RecyclerView customerEntryRV;
    private FirebaseFirestore db;
    private ArrayList<CustomerPayments> customerPaymentsArrayList;
    private CustomerRecentPaymentAdapter customerRecentPaymentAdapter;
    String getCxGuid;
    ImageButton backBtn;
    private CardView emptyCv;
    private ProgressBar paymentLoadingPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_payments_screen);

        emptyCv=findViewById(R.id.cardViewEmptyView);
        paymentLoadingPb = findViewById(R.id.paymentLoadingProgressBar);

        // init
        backBtn=findViewById(R.id.rcBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        // get GUID
        Intent intent = getIntent();
        getCxGuid = intent.getStringExtra("guid");

        // rec view
        customerEntryRV = findViewById(R.id.cxRecentPaymentRecyclerView);
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();
        // creating our new array list
        customerPaymentsArrayList = new ArrayList<>();

        // setting up recview
        customerEntryRV.setHasFixedSize(true);
        customerEntryRV.setLayoutManager(new LinearLayoutManager(this));

        // adding our array list to our recycler view adapter class.
        customerRecentPaymentAdapter = new CustomerRecentPaymentAdapter(customerPaymentsArrayList, this);
        // setting adapter to our recycler view.
        customerEntryRV.setAdapter(customerRecentPaymentAdapter);

        // get data from firestore db
        getDataFromFireStoreDb();


    }

    private void getDataFromFireStoreDb() {
        db.collection("CxPayments").whereEqualTo("cxGuid", getCxGuid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            //
                            paymentLoadingPb.setVisibility(View.GONE);
                            customerEntryRV.setVisibility(View.VISIBLE);
                            emptyCv.setVisibility(View.GONE);
                            //
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                CustomerPayments customerPayments = d.toObject(CustomerPayments.class);
                                customerPaymentsArrayList.add(customerPayments);
                            }
                            customerRecentPaymentAdapter.notifyDataSetChanged();
                        } else {
                            paymentLoadingPb.setVisibility(View.GONE);
                            customerEntryRV.setVisibility(View.GONE);
                            emptyCv.setVisibility(View.VISIBLE);
                            // if the snapshot is empty we are displaying a toast message.
                            //Toast.makeText(RecentPaymentsScreenActivity.this, "No recent payment data found in DB", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        paymentLoadingPb.setVisibility(View.VISIBLE);
                        customerEntryRV.setVisibility(View.GONE);
                        emptyCv.setVisibility(View.GONE);
                       // Toast.makeText(RecentPaymentsScreenActivity.this, "Fail to get the recent payment data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void goBack() {
        getOnBackPressedDispatcher().onBackPressed();
    }
}