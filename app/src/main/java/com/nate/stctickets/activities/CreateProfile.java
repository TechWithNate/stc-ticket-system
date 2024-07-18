package com.nate.stctickets.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nate.stctickets.R;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    private ImageView userImage;
    private EditText username;
    private EditText contact;
    private MaterialButton createProfile;
    private ProgressBar progressBar;
    private AutoCompleteTextView genderAutoComplete;
    private String gender;
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE = 100;
    private String imageUriAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();


        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genderOptions);
        genderAutoComplete.setAdapter(adapter);
        genderAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            gender = (String) parent.getItemAtPosition(position);
        });


        userImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });


        createProfile.setOnClickListener(v -> checkFields());


    }


    private void initViews(){
        userImage = findViewById(R.id.profile_img);
        username = findViewById(R.id.username);
        genderAutoComplete = findViewById(R.id.gender);
        contact = findViewById(R.id.tel);
        createProfile = findViewById(R.id.create_profile_btn);
        progressBar = findViewById(R.id.progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    private void checkFields() {
        if (imageUri == null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
        } else if (username.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }else if (contact.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Contact", Toast.LENGTH_SHORT).show();
        }else if (gender == null){
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
        }else if (imageUriAccessToken == null){
            Toast.makeText(this, "Upload Image", Toast.LENGTH_SHORT).show();
        }else if (contact.getText().toString().length() < 10){
            Toast.makeText(this, "Invalid Contact Number", Toast.LENGTH_SHORT).show();
        }else {
            uploadImage();
        }
    }

    private void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference("images");
        StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
        imageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUriAccessToken = uri.toString();
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    createUser();
                });
            }
        });
    }

    private void createUser() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", username.getText().toString());
        userMap.put("gender", genderAutoComplete.getText().toString());
        userMap.put("contact", contact.getText().toString());
        userMap.put("image", imageUriAccessToken);
        sendUserData(userMap);
    }

    private void sendUserData(Map<String, Object> userMap) {
        databaseReference.child("users").child(firebaseUser.getUid()).setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();
                navigateToHome();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CreateProfile.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void navigateToHome() {
        startActivity(new Intent(CreateProfile.this, Home.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            userImage.setImageURI(imageUri);
        }
    }

}

