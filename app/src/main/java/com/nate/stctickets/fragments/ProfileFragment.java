package com.nate.stctickets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.stctickets.R;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;
    private ImageView profileImage;
    private EditText username;
    private EditText gender;
    private EditText contact;
    private MaterialButton editBtn;
    private MaterialButton logoutBtn;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        initViews();


        editBtn.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Edit Profile", Toast.LENGTH_SHORT).show();
        });

        logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            getActivity().finish();
        });

        loadUserData();

        return view;
    }

    private void initViews(){
        profileImage = view.findViewById(R.id.profile_img);
        username = view.findViewById(R.id.username);
        gender = view.findViewById(R.id.gender);
        contact = view.findViewById(R.id.contact);
        editBtn = view.findViewById(R.id.edit_profile_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

    }

    private void loadUserData() {
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child(Objects.requireNonNull(firebaseAuth.getUid())).get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    username.setText(name);
                    gender.setText(snapshot.child("gender").getValue(String.class));
                    contact.setText(snapshot.child("contact").getValue(String.class));
                    Glide.with(getActivity()).load(imageUrl).into(profileImage);

                } else {
                    Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }


}
