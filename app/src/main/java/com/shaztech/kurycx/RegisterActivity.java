package com.shaztech.kurycx;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView gotoLoginTv;
    EditText nameEt, passwordEt, confirmPasswordEt, phoneEt, locationEt, schemeCodeEt;
    Button registerBtn;
    String name, password, confirmPassword, phone, location, schemeCode;
    private AlertDialog dialog;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    // for serial number
    private static final String NUMBER_COLLECTION = "numbers";
    private static final String NUMBER_DOCUMENT = "incrementing_number";
    int inc_number_in_db;
    int storing_inc_number_to_db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        initUI();

        //goto login page
        gotoLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginPageActivity.class);
                startActivity(i);
                finish();
            }
        });
        //------------------------------

        // register button click
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogScreen();
                //
                name = nameEt.getText().toString();
                password = passwordEt.getText().toString();
                confirmPassword = confirmPasswordEt.getText().toString();
                phone = phoneEt.getText().toString();
                location = locationEt.getText().toString();
                schemeCode = schemeCodeEt.getText().toString();

                // validate email n password
                if (phone.length()<10) {

                    phoneEt.setError("Enter 10 digit number");
                    phoneEt.requestFocus();
                    dialog.dismiss();

                } else if (!isValidPassword(password)) {
                    passwordEt.setError("Invalid Password");
                    passwordEt.requestFocus();
                    dialog.dismiss();

                } else if (!password.equals(confirmPassword)) {
                    confirmPasswordEt.setError("Password does not match");
                    confirmPasswordEt.requestFocus();
                    dialog.dismiss();
                } else if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty() || location.isEmpty() || schemeCode.isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Fields cannot be blank", Toast.LENGTH_LONG).show();
                } else checkSchemeCode(schemeCode);

            }
        });

    }

    private void checkSchemeCode(String schemeCode) {
        String scode = schemeCode;
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("app_admin_settings");
        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String getCode = document.getString("schemeCode");
                        if (getCode.equals(scode)) {
                            registerUser();
                        } else {
                            dialog.dismiss();
                            schemeCodeEt.setError("Invalid Code");
                            schemeCodeEt.requestFocus();

                        }

                    }

                }
            }
        });

    }

    private void registerUser() {
        //Toast.makeText(RegisterActivity.this, "READY", Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(phone+"@kury.com", password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //  Toast.makeText(RegisterActivity.this, "account created", Toast.LENGTH_SHORT).show();
//                                    Intent login = new Intent(getApplicationContext(), HomePageActivity.class);
//                                    startActivity(login);
//                                    finish();
                                    //createUserprofile();
                                    init_s_n();
                                }
                                if (!task.isSuccessful()) {
                                    dialog.dismiss();
                                    try {
                                        throw task.getException();
                                    } catch (Exception e){


                                        Toast.makeText(RegisterActivity.this, "ERROR: "+ e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                );

    }
    private void init_s_n() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = rootRef.collection(NUMBER_COLLECTION);

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        inc_number_in_db = document.getLong("current_number").intValue();
                        storing_inc_number_to_db = inc_number_in_db + 1;
                        updateNextIncNumber(storing_inc_number_to_db);

                    }

                }
            }
        });

    }
    private void updateNextIncNumber(int i) {


        db = FirebaseFirestore.getInstance();
        CollectionReference docRef = db.collection(NUMBER_COLLECTION);
        Map<String, Object> updates = new HashMap<>();
        updates.put("current_number", i);

        // ----------------
        docRef.document(NUMBER_DOCUMENT).update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   // Update successful
                                                   Toast.makeText(getApplicationContext(), "SN UPDATED", Toast.LENGTH_SHORT).show();
                                                   createUserprofile(i);
                                               } else {
                                                   Toast.makeText(getApplicationContext(), "SN NOT UPDATED", Toast.LENGTH_SHORT).show();

                                               }
                                           }
                                       }

                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                    }
                });


        // --------------------


    }

    private void createUserprofile(int i) {
        String gUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

// Get a reference to the document you want to update
        CollectionReference docRef = db.collection("Customers");
// Create a map to store the field and its new value
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("address", location);
        updates.put("phone", phone);
        updates.put("guid", gUid);
        updates.put("is_draw", "no");
        updates.put("draw_week", "NA");
        updates.put("paid", "0");
        updates.put("balance", "100000");
        updates.put("current_week_balance", "0");
        updates.put("dp_link", "NA");
        updates.put("serial_number", i);
        // update
        docRef.document(gUid).set(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update successful
                           // Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            //goto profile update
                            Intent gotoHome = new Intent(RegisterActivity.this, UpdateDpActivity.class);
                            startActivity(gotoHome);
                            finish();

                        } else {
                            dialog.dismiss();
                            // Update failed
                            Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void initUI() {
        mAuth = FirebaseAuth.getInstance();
        nameEt = findViewById(R.id.editTextName);
        passwordEt = findViewById(R.id.editTextPassword);
        confirmPasswordEt = findViewById(R.id.editTextConfirmPassword);
        phoneEt = findViewById(R.id.editTextPhone);
        locationEt = findViewById(R.id.editTextLocation);
        schemeCodeEt = findViewById(R.id.editTextSchemeCode);
        registerBtn = findViewById(R.id.buttonRegister);
        gotoLoginTv = findViewById(R.id.textviewGotoLogin);

    }


    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 7) {
            return true;
        } else return false;

    }

    private void showDialogScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
}