package com.shaztech.kurycx;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button cxIsDrawBtn, cxLocationBtn;
    private TextView cxNameTv, cxPhoneTv;
    FirebaseUser currentFirebaseUser;
    String currentUserGuid;
    String profileUrl;
    private ImageView profilePictureIv;
    private ImageView paymentCv;
    private ImageView allDrawCv;
    private ImageView showWinnerCv;
    private ImageView tAndCCv;
    private ImageView profileCv;
    private ImageView supportCv;
    private AlertDialog dialog;
    private AlertDialog drawDialog;
    private ImageButton logoutBtn;
    private ViewFlipper viewFlipper;
    MaterialButton paidBtn, balanceBtn;

    private TextView snumberTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
/*
        // for crash -----------------------------------------------

        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        /// -----------------------------------------------------------

 */
        init_cx_ui();
        updateCxUi();
        loadSlideImagesFromUrl();

        // balance and paid button click
        paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePageActivity.this, "Your total paid Amount!!!", Toast.LENGTH_SHORT).show();
            }
        });
        balanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePageActivity.this, "Your total balance Amount!!!", Toast.LENGTH_SHORT).show();

            }
        });

        // on payment click
        paymentCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto payment view screen
                Intent login = new Intent(HomePageActivity.this, RecentPaymentsScreenActivity.class);
                login.putExtra("guid", currentUserGuid);
                startActivity(login);


            }
        });
        // on all draw click
        allDrawCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto payment view screen
                Intent login = new Intent(HomePageActivity.this, ViewAllDrawActivity.class);
                startActivity(login);


            }
        });
        // show winner click
        showWinnerCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDrawGenerateScreen();
               // startThread();
                Intent showDraw = new Intent(HomePageActivity.this, ShowDrawGeneratorActivity.class);
                startActivity(showDraw);


            }
        });

        // logout button click
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationPopup();

            }
        });
        // terms and condition page
        tAndCCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(HomePageActivity.this, AppTandCPageActivity.class);
                startActivity(login);
            }
        });
        // profile page
        profileCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(HomePageActivity.this, ProfilePageActivity.class);
                startActivity(login);
            }
        });
        // support
        supportCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent supportlogin = new Intent(HomePageActivity.this, SupportScreenActivity.class);
                startActivity(supportlogin);
               // showAdminContactScreen();
            }
        });
    }

    private void loadSlideImagesFromUrl() {
        String[] imageURLs = {
                "https://firebasestorage.googleapis.com/v0/b/kurycx-83189.appspot.com/o/banner_images%2Fone?alt=media&token=db570472-ae8b-46c1-9cb4-b72dd8f67032",
                "https://firebasestorage.googleapis.com/v0/b/kurycx-83189.appspot.com/o/banner_images%2Ftwo?alt=media&token=79ef7f76-417e-4b7b-ac13-bb6dd6357086",
                "https://firebasestorage.googleapis.com/v0/b/kurycx-83189.appspot.com/o/banner_images%2Fthree?alt=media&token=3c6b4fb3-ac53-4f10-8673-a34c10f4375a"

        };
        for (String url : imageURLs) {
            // Load images from URLs
            new DownloadImageTask().execute(url);
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Add bitmap to ImageView and add it to the ViewFlipper
                ImageView imageView = new ImageView(HomePageActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(result);
                viewFlipper.addView(imageView);
            }
        }
    }

    private void startThread() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                drawDialog.dismiss();
                getDrawDetails();
                // Your database code here
                //       Toast.makeText(getApplicationContext(), "READY TO GO", Toast.LENGTH_LONG).show();
                // Toast.makeText(getApplicationContext(), "RESULT IS READY SUCCESS", Toast.LENGTH_LONG).show();

            }
        }, 5000);
    }

    private void showDrawGenerateScreen() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.draw_generate_loading, null));
        builder.setCancelable(false);
        // builder.setCancelable(true);

        drawDialog = builder.create();
        drawDialog.show();

    }

    private void showAdminContactScreen() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.contact_admin_layout, null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();

    }
    private void showLogoutConfirmationPopup() {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Yes' Button
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomePageActivity.this, LoginPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'No' Button
                        Toast.makeText(getApplicationContext(), "Logout Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("LOGOUT");
        alert.show();


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
                                String winnerWeek = document.getString("week");
                                String isWinnerAnnounced = document.getString("winner_announced_for_current_month");
                                if (Objects.equals(isWinnerAnnounced, "yes")) {
                                    Intent i = new Intent(HomePageActivity.this, DrawCompletedScreenActivity.class);
                                    i.putExtra("winner_guid", winnerGuid);
                                    i.putExtra("week", winnerWeek);
                                    startActivity(i);

                                } else {
                                    Intent i = new Intent(HomePageActivity.this, DrawWaitingActivity.class);

                                    startActivity(i);

                                }


                            }
                        } else {
                            // Log.d("TAGviewcx", "Error getting documents: ", task.getException());
                            Toast.makeText(getApplicationContext(), "error in finding winner", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void updateCxUi() {
        db = FirebaseFirestore.getInstance();
        db.collection("Customers")
                .whereEqualTo("guid", currentUserGuid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                snumberTv.setText("Serial Number: 0" +String.valueOf(document.getLong("serial_number").intValue()));
                                cxNameTv.setText(document.getString("name"));
                                cxPhoneTv.setText(document.getString("phone"));
                                cxIsDrawBtn.setText("Draw Week: "+document.getString("draw_week"));
                                cxLocationBtn.setText(document.getString("address"));
                                paidBtn.setText("Total Paid: "+document.getString("paid"));
                                balanceBtn.setText("Kury Balance: "+document.getString("balance"));
                                profileUrl = document.getString("dp_link");
                                updateProfilePicture(profileUrl);




                            }
                        } else {

                            Toast.makeText(getApplicationContext(), "Error getting cx details", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void updateProfilePicture(String profileUrl) {
      //  Toast.makeText(this, "dp"+profileUrl, Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(profileUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.user_1) // Placeholder image
                        .error(R.drawable.user_1) // Error image in case of loading failure
                )
                .into(profilePictureIv);
    }

    private void init_cx_ui() {
        snumberTv = findViewById(R.id.tv_cxSnNumber);
        viewFlipper = findViewById(R.id.viewFlipper);
        cxIsDrawBtn = findViewById(R.id.bt_isdraw);
        cxLocationBtn = findViewById(R.id.bt_cxlocation);
        cxNameTv = findViewById(R.id.tv_cxname);
        cxPhoneTv = findViewById(R.id.tv_cxphone);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentFirebaseUser != null;
        currentUserGuid = currentFirebaseUser.getUid();
        paymentCv = findViewById(R.id.paymentCardView);
        allDrawCv = findViewById(R.id.allDrawCardView);
        supportCv = findViewById(R.id.supportCardView);
        tAndCCv = findViewById(R.id.tcCardView);
        profileCv = findViewById(R.id.profileCardView);
        showWinnerCv = findViewById(R.id.showWinnerCardView);
        logoutBtn = findViewById(R.id.logOutButton);
        paidBtn = findViewById(R.id.paidAmountButton);
        balanceBtn = findViewById(R.id.balanceAmountButton);

        profilePictureIv = findViewById(R.id.profileImageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCxUi();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateCxUi();
    }
}