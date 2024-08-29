package com.nate.stctickets.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.nate.stctickets.R;
import com.nate.stctickets.models.BalanceUpdater;
import com.nate.stctickets.models.MoMoRequest;
import com.nate.stctickets.models.MoMoResponse;
import com.nate.stctickets.models.Payer;
import com.nate.stctickets.services.ApiClient;
import com.nate.stctickets.services.MoMoApiService;
import com.paystack.android.core.Paystack;
import com.paystack.android.ui.paymentsheet.PaymentSheet;
import com.paystack.android.ui.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class AddBalance extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private static final String PAYSTACK_SECRET_KEY = "sk_test_1fa583b4575700d23a2c086fed3aabba1f815b9b";
    private static final String PAYMENT_URL = "https://api.paystack.co/transaction/initialize";


    private EditText amount;
    private EditText contact;
    private ImageView backBtn;
    private MaterialButton payBtn;
    private BalanceUpdater balanceUpdater;
    private ProgressBar progressBar;
    private PaymentSheet paymentSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_balance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        Paystack.builder()
                .setPublicKey("pk_test_1724ec4330298e549b8380cb539c4f7c03fdc2cb")
                .setLoggingEnabled(true)
                .build();


        paymentSheet = new PaymentSheet(this, this::paymentComplete);

        backBtn.setOnClickListener(v -> finish());
        payBtn.setOnClickListener(v -> {
            try {
                checkFields();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void initViews(){
        amount = findViewById(R.id.amount);
        contact = findViewById(R.id.contact);
        backBtn = findViewById(R.id.back_btn);
        payBtn = findViewById(R.id.add_balance);
        progressBar = findViewById(R.id.progress_bar);

        balanceUpdater = new BalanceUpdater(this, progressBar);
    }

    private void checkFields() throws IOException {
        if (amount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter amount to deposit", Toast.LENGTH_SHORT).show();
        } else if (contact.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter your mobile money number", Toast.LENGTH_SHORT).show();
        } else {
            //balanceUpdater.addAmount(Double.parseDouble(amount.getText().toString()));
//            makePayment();
            returnID();
        }
    }

    private void pay() {
        Paystack.builder()
                .setPublicKey("pk_test_1724ec4330298e549b8380cb539c4f7c03fdc2cb")
                .setLoggingEnabled(true)
                .build();


        paymentSheet = new PaymentSheet(this, this::paymentComplete);
//
    }


    private void returnID() throws IOException {


        final String[] testCode = new String[1];
        final String[] reference = new String[1];
        OkHttpClient client = new OkHttpClient();

        // Define media type and request body
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{ \"email\": \"dzrekenathan2002@gmail.com\", \"amount\": \"500000\" }");

        RequestBody body = RequestBody.create(mediaType, "{ \"amount\": 100,\r\n      \"email\": \"customer@email.com\",\r\n      \"currency\": \"GHS\",\r\n      \"mobile_money\": {\r\n        \"phone\" : \"0551234987\",\r\n        \"provider\" : \"mtn\"\r\n      }\r\n    }");
        // Build the request
        Request request = new Request.Builder()
                .url(PAYMENT_URL)
                .post(body) // Use POST method
                .addHeader("Authorization", "Bearer " + PAYSTACK_SECRET_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and handle the response
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
                Toast.makeText(AddBalance.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Parse the response body
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Extract status, message, and data
                        boolean status = jsonResponse.getBoolean("status");
                        String message = jsonResponse.getString("message");
                        JSONObject data = jsonResponse.getJSONObject("data");
                        String authorizationUrl = data.getString("authorization_url");
                        String accessCode = data.getString("access_code");
                        String reference = data.getString("reference");

                        // Log or use the extracted values
                        paymentSheet.launch(accessCode);

                        // Navigate to the URL or handle it as needed
                        // For example, open the authorization URL in a browser
                        // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl));
                        // startActivity(browserIntent);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e(TAG, "Response unsuccessful: " + response.message());
                }
            }
        });
    }

    private void makePayment() {
        // Pass access_code from transaction initialize call on the server
        paymentSheet.launch("br6cgmvflhn3qtd");
    }

    private void paymentComplete(PaymentSheetResult paymentSheetResult) {
        String message;

        if (paymentSheetResult instanceof PaymentSheetResult.Cancelled) {
            message = "Cancelled";
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            Log.e("Payment failed",
                    failedResult.getError().getMessage() != null ? failedResult.getError().getMessage() : "Failed",
                    failedResult.getError());
            message = failedResult.getError().getMessage() != null ? failedResult.getError().getMessage() : "Failed";
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d("Payment successful",
                    ((PaymentSheetResult.Completed) paymentSheetResult).getPaymentCompletionDetails().toString());
            message = "Successful";
        } else {
            message = "You shouldn't be here";
        }

        Toast.makeText(this, "Payment " + message, Toast.LENGTH_SHORT).show();
    }


}