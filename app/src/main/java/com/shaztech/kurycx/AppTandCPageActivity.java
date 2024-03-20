package com.shaztech.kurycx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AppTandCPageActivity extends AppCompatActivity {

    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tand_cpage);

        //init
        backBtn=findViewById(R.id.tCBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }
    public void goBack() {
        getOnBackPressedDispatcher().onBackPressed();
    }
}