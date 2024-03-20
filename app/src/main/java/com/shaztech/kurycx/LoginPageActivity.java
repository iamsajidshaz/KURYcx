package com.shaztech.kurycx;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageActivity extends AppCompatActivity {

    String userName;
    String passWord;
    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private AlertDialog dialog;
    private FirebaseAuth mAuth;
    private TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // init UI
        initUi();

        // goto register page
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regGo = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regGo);
                finish();
            }
        });

        // on login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = usernameEditText.getText().toString();
                passWord = passwordEditText.getText().toString();
                // check entered values
                if (userName.length() != 10) {
                    usernameEditText.setError("Invalid Email");
                    usernameEditText.requestFocus();


                } else if (!isValidPassword(passWord)) {
                    passwordEditText.setError("Invalid Password");
                    passwordEditText.requestFocus();


                } else loginUser(userName, passWord);

            }
        });
    }

    private void loginUser(String userName, String passWord) {
        showDialogScreen();
        mAuth.signInWithEmailAndPassword(userName+"@kury.com", passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    Intent login = new Intent(getApplicationContext(), HomePageActivity.class);
                                    startActivity(login);
                                    finish();
                                }
                                if(!task.isSuccessful()){
                                    dialog.dismiss();
                                    try {
                                        throw task.getException();
                                    } catch (Exception e){
                                        usernameEditText.setError(e.toString());
                                        usernameEditText.requestFocus();
                                    }
                                }
                            }
                        }
                );
    }

    private void initUi() {
        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.buttonLogin);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerTv = findViewById(R.id.registerTextView);
    }

    private void showDialogScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    // validate email

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 7) {
            return true;
        } else return false;

    }
}