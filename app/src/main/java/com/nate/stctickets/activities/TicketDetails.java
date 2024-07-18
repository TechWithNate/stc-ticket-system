package com.nate.stctickets.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.nate.stctickets.R;
import com.nate.stctickets.models.BookingModel;

public class TicketDetails extends AppCompatActivity {

    private TextView passengerName;
    private TextView fromLocation;
    private TextView toLocation;
    private ImageView qrCode;
    private TextView ticketNumber;
    private TextView seatNumber;
    private MaterialButton downloadBtn;
    private MaterialButton cancelBtn;
    private BookingModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        model = getIntent().getParcelableExtra("ticket");
        if (model != null){
            passengerName.setText(model.getPassengerName());
            fromLocation.setText(model.getFrom());
            toLocation.setText(model.getTo());
            ticketNumber.setText(model.getTicketID());
        }

    }

    private void initViews(){
        passengerName = findViewById(R.id.passenger_name);
        fromLocation = findViewById(R.id.from_location);
        toLocation = findViewById(R.id.to_location);
        ticketNumber = findViewById(R.id.ticket_number);
        seatNumber = findViewById(R.id.seat_no);
        downloadBtn = findViewById(R.id.download_ticket);
        cancelBtn = findViewById(R.id.cancel);
    }

}