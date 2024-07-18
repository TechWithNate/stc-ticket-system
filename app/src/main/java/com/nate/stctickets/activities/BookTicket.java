package com.nate.stctickets.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nate.stctickets.R;
import com.nate.stctickets.models.BookingModel;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

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
    private String dayt;
    private int selectedHour;
    private int selectedMinute;
    private Calendar calendar;
    private MaterialCardView dateLayout;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;



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

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String[] daytimeOption = {"Morning", "Afternoon", "Night"};
        ArrayAdapter<String> daytimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, daytimeOption);
        dayTime.setAdapter(daytimeAdapter);
        dayTime.setOnItemClickListener((parent, view, position, id) -> {
            dayt = (String) parent.getItemAtPosition(position);
        });


        dateLayout.setOnClickListener(v -> showDatePickerDialog(tvDate, tvDay, year, month, day));



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
        dateLayout = findViewById(R.id.date_layout);

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


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String ticketID = databaseReference.push().getKey();
        BookingModel model = new BookingModel(ticketID, firebaseAuth.getUid(), etPassengerName.getText().toString(), etFromLocation.getText().toString(), etToLocation.getText().toString(), dayt, tvDate.getText().toString(), tvDay.getText().toString(), null, "A20");

        // Generate the QR code
        String ticketData = model.toString();

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        //int smallerDimension = Math.min(imageView.getWidth(), imageView.getHeight());
        QRGEncoder qrgEncoder = new QRGEncoder(ticketData, null, QRGContents.Type.TEXT, dimen);
        try {
            Bitmap bitmap = qrgEncoder.getBitmap();
            String qrCodeBase64 = encodeBitmapToBase64(bitmap);
            model.setQrCode(qrCodeBase64);
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "QR code generation failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        if (ticketID != null){
            if (firebaseAuth.getUid() != null){
                databaseReference.child(firebaseAuth.getUid()).child("tickets").child(ticketID)
                        .setValue(model).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                viewTicket(model);
                                Toast.makeText(BookTicket.this, "Ticket Created", Toast.LENGTH_SHORT).show();
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(BookTicket.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(BookTicket.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }else {
            Toast.makeText(this, "Null ticket ID, Try Again!", Toast.LENGTH_SHORT).show();
        }




    }

    private void viewTicket(BookingModel model) {
        Intent intent = new Intent(BookTicket.this, TicketDetails.class);
        intent.putExtra("ticket", model);
        startActivity(intent);
        finish();
    }


    private void showDatePickerDialog(TextView dateField, TextView dayField, int year, int month, int day) {
        DatePickerDialog.OnDateSetListener setListener = (view, yearSelected, monthSelected, dayOfMonthSelected) -> {
            calendar.set(yearSelected, monthSelected, dayOfMonthSelected);
            Date date = calendar.getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

            String formattedDate = dateFormat.format(date);
            String formattedDay = dayFormat.format(date);

            dateField.setText(formattedDate);
            dayField.setText(formattedDay);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                BookTicket.this, android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day
        );
        Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}