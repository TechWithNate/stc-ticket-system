package com.nate.stctickets.services;

import com.nate.stctickets.models.MoMoRequest;
import com.nate.stctickets.models.MoMoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MoMoApiService {
    @Headers({
            "Ocp-Apim-Subscription-Key: 7c3c47680dee4ee6907fc9de3bf35f2e",
            "X-Target-Environment: sandbox", // Change to "live" in production
            "Content-Type: application/json"
    })
    @POST("/collection/v1_0/requesttopay")
    Call<MoMoResponse> requestToPay(@Body MoMoRequest request);
}
