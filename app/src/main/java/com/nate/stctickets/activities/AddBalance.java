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

import java.io.IOException;


import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddBalance extends AppCompatActivity {

    private EditText amount;
    private EditText contact;
    private ImageView backBtn;
    private MaterialButton payBtn;
    private BalanceUpdater balanceUpdater;
    private ProgressBar progressBar;


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


        backBtn.setOnClickListener(v -> finish());
        payBtn.setOnClickListener(v -> checkFields());

    }

    private void initViews(){
        amount = findViewById(R.id.amount);
        contact = findViewById(R.id.contact);
        backBtn = findViewById(R.id.back_btn);
        payBtn = findViewById(R.id.add_balance);
        progressBar = findViewById(R.id.progress_bar);

        balanceUpdater = new BalanceUpdater(this, progressBar);
    }

    private void checkFields() {
        if (amount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter amount to deposit", Toast.LENGTH_SHORT).show();
        } else if (contact.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter your mobile money number", Toast.LENGTH_SHORT).show();
        } else {
            //balanceUpdater.addAmount(Double.parseDouble(amount.getText().toString()));
            makePayment(contact.getText().toString(), amount.getText().toString());
        }
    }

    private void makePayment(String number, String amount) {
        MoMoApiService apiService = ApiClient.getApiService();
        Payer payer = new Payer("MSISDN", number);
        MoMoRequest request = new MoMoRequest(amount, "GHS", "<external-id>", "Test Payment", "Test", payer);

        Call<MoMoResponse> call = apiService.requestToPay(request);
        call.enqueue(new Callback<MoMoResponse>() {
                         @Override
                         public void onResponse(Call<MoMoResponse> call, Response<MoMoResponse> response) {
                             if (response.isSuccessful()) {
                                 MoMoResponse momoResponse = response.body();
                                 if (momoResponse != null) {
                                     String transactionReference = momoResponse.getReferenceId();
                                     // Handle success, e.g., navigate to another page
                                 }
                             } else {
                                 // Handle the error response
                                 try {
                                     // Extract error body as a string
                                     String errorBody = response.errorBody().string();
                                     Log.e("Payment Error", errorBody);
                                     // Optionally, you can parse this error if it follows a JSON structure
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<MoMoResponse> call, Throwable t) {
                             // Handle failure
                             Log.e("Payment Failure", t.getMessage());
                         }
                     });
    }


//    private void makePayment() {
//        MoMoApiService apiService = ApiClient.getApiService();
//        Payer payer = new Payer("MSISDN", "0546651113");
//        MoMoRequest request = new MoMoRequest("100", "GHS", "<external-id>", "Test Payment", "Test", payer);
//
//        Call<MoMoResponse> call = apiService.requestToPay(request);
//        call.enqueue(new Callback<MoMoResponse>() {
//            @Override
//            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//                    ResponseBody momoResponse = response.body();
//                    // Handle success
//                } else {
//                    // Handle failure
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
//
//            }
//
////            @Override
////            public void onResponse(Call<MoMoResponse> call, Response<MoMoResponse> response) {
////                if (response.isSuccessful()) {
////                    MoMoResponse momoResponse = response.body();
////                    // Handle success
////                } else {
////                    // Handle failure
////                }
////            }
////
////            @Override
////            public void onFailure(Call<MoMoResponse> call, Throwable t) {
////                // Handle error
////            }
////        });
//        });
//    }
}