package com.nate.stctickets.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.stctickets.R;

public class BookTicket extends AppCompatActivity {

    private EditText etPassengerName;
    private EditText etFromLocation;
    private EditText etToLocation;
    private EditText etDayTime;
    private AutoCompleteTextView dayTime;
    private TextView tvDate;
    private TextView tvDay;
    private MaterialButton book;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private MaterialButton bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        bookBtn.setOnClickListener(v -> checkFields());


    }

    private void initViews(){
        etPassengerName = findViewById(R.id.passenger_name);
        etFromLocation = findViewById(R.id.from_location);
        etToLocation = findViewById(R.id.to_location);
        dayTime = findViewById(R.id.daytime);
        tvDate = findViewById(R.id.tvDate);
        tvDay = findViewById(R.id.tvDay);
        bookBtn = findViewById(R.id.book);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();


    }


    private void checkFields() {
        if (etPassengerName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Passenger Name", Toast.LENGTH_SHORT).show();
        }else if(etFromLocation.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter From Location", Toast.LENGTH_SHORT).show();
        }else if(etToLocation.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter To Location", Toast.LENGTH_SHORT).show();
        }else if(dayTime.getText().toString().isEmpty()){
            Toast.makeText(this, "Select Day Time", Toast.LENGTH_SHORT).show();
        }else if (tvDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
        }else if (tvDay.getText().toString().isEmpty()){
            Toast.makeText(this, "Select Day", Toast.LENGTH_SHORT).show();
        }else {
            bookTicket();
        }
    }

    private void bookTicket() {
        progressBar.setVisibility(View.VISIBLE);

    }


}