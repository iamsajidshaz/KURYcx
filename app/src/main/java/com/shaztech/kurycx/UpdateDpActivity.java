package com.shaztech.kurycx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateDpActivity extends AppCompatActivity {

    private Button btnUploadProfile;
    private Button btnCompleteProfile;

    // view for image view
    private ImageView profilePictureImageView;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dp);

        btnUploadProfile = findViewById(R.id.btnUploadProfilePicture);
        profilePictureImageView = findViewById(R.id.imageViewProfilePicture);

        btnCompleteProfile = findViewById(R.id.btnCompleteRegister);

        db = FirebaseFirestore.getInstance();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();

            }
        });

        btnCompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompleteProfile();

            }
        });

        // upload profile picture
        btnUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(AdminSettingsActivity.this, selectedRadioButton, Toast.LENGTH_SHORT).show();
                uploadProfileImage();
            }
        });

    }

    private void CompleteProfile() {

        Intent cplogin = new Intent(UpdateDpActivity.this, HomePageActivity.class);
        startActivity(cplogin);
        finish();
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

            // Setting image on image view using Bitmap
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePictureImageView.setImageBitmap(bitmap);
        }
    }

    private void uploadProfileImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "profile_images/"
                                    + FirebaseAuth.getInstance().getCurrentUser().getUid()
                    );

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            // Log.d("IMG",downloadUrl.toString());
                                            uploadProfilePicUrlToDb(downloadUrl);
                                        }
                                    });
                                    //  Log.d("IMG",ref.getDownloadUrl().toString());
                                    // Toast.makeText(AdminSettingsActivity.this, "URL:"+ref.getDownloadUrl(), Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(UpdateDpActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {


                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });

        }
    }

    private void uploadProfilePicUrlToDb(Uri downloadUrl) {

        db = FirebaseFirestore.getInstance();
        CollectionReference docRef = db.collection("Customers");
        Map<String, Object> updates = new HashMap<>();
        updates.put("dp_link", downloadUrl);
        // update
        docRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            btnUploadProfile.setEnabled(false);
                            btnUploadProfile.setText("Profile Picture Updated");
                            btnCompleteProfile.setVisibility(View.VISIBLE);

                        } else {

                            Toast.makeText(getApplicationContext(), "profile picture not updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}