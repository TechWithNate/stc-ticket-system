package com.nate.stctickets.activities;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.stctickets.R;
import com.nate.stctickets.fragments.HomeFragment;
import com.nate.stctickets.models.BookingModel;

public class TicketDetails extends AppCompatActivity {

    private TextView passengerName;
    private TextView fromLocation;
    private TextView toLocation;
    private TextView departureTime;
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
            seatNumber.setText(model.getSeatNumber());
            departureTime.setText(model.getTime());
            displayQRCode(model.getQrCode());
        }

        cancelBtn.setOnClickListener(v -> {
            deleteTicket();
        });

    }

    private void initViews(){
        passengerName = findViewById(R.id.passenger_name);
        fromLocation = findViewById(R.id.from_location);
        toLocation = findViewById(R.id.to_location);
        ticketNumber = findViewById(R.id.ticket_number);
        seatNumber = findViewById(R.id.seat_no);
        downloadBtn = findViewById(R.id.download_ticket);
        cancelBtn = findViewById(R.id.cancel);
        qrCode = findViewById(R.id.qr_code);
        departureTime = findViewById(R.id.departure_time);
    }

    private void displayQRCode(String qrCodeBase64) {
        Bitmap qrCodeBitmap = decodeBase64ToBitmap(qrCodeBase64);
        qrCode.setImageBitmap(qrCodeBitmap);
    }

    private Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public void deleteTicket() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //BookingModel bookingModel = bookingModels.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getUid()).child("tickets").child(model.getTicketID());
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(TicketDetails.this, HomeFragment.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Ticket deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete ticket", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}