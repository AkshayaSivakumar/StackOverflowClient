package com.example.stackoverflowclient.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiResponse {

    public static <T> void sendRequest(Call<T> call, final String strApiName, final ApiResponseListener apiListener) {

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (null != response.body())
                        apiListener.onSuccess(strApiName, response.body());
                } else {
                    if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        ErrorModel errorModel = new ErrorModel();
                        try {
                            if (response.errorBody() != null) {
                                errorModel = gson.fromJson(response.errorBody().string(), ErrorModel.class);
                                apiListener.onError(strApiName, errorModel);
                            }
                        } catch (IOException e) {
                            errorModel.setError_id(600);
                            errorModel.setError_message("Internal Error. Please try again later");
                            errorModel.setApiName(strApiName);
                            apiListener.onError(strApiName, errorModel);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                String errorMessage = (null == t) ? "Unknown Error" : t.getMessage();
                apiListener.onFailure(strApiName, errorMessage);
            }
        });
    }
}
