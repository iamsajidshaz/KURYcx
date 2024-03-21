package com.shaztech.kurycx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DrawWaitingActivity extends AppCompatActivity {

    ImageButton backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_waiting);
        backBtn=findViewById(R.id.tCBackBuasasasatton);

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