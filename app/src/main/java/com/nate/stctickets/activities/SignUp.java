package com.nate.stctickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.nate.stctickets.R;

public class SignUp extends AppCompatActivity {

    private EditText email;
    private AutoCompleteTextView roleAutoComplete;
    private EditText password;
    private EditText confirmPassword;
    private MaterialButton createAccount;
    private TextView login;

    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();



        createAccount.setOnClickListener(v -> {
            checkFields();
        });
        login.setOnClickListener(v -> loginUser());

    }


    private void initViews(){
        email = findViewById(R.id.email);
        ///roleAutoComplete = findViewById(R.id.role);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.c_password);
        createAccount = findViewById(R.id.create_btn);
        login = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void checkFields() {
        if (email.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        }else if (password.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }else if (password.getText().length() < 8){
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
        }else if (!confirmPassword.getText().toString().equals(password.getText().toString())){
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }else {
            createUser(email.getText().toString(), password.getText().toString());
        }

    }

    private void createUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                createProfile();
            }else {
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser() {
        startActivity(new Intent(SignUp.this, Login.class));
    }

    private void createProfile(){
        Intent intent = new Intent(SignUp.this, CreateProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}