package com.example.stackoverflowclient.network;

public interface ApiResponseListener {
    void onSuccess(String strApiName, Object response);
    void onError(String strApiName, ErrorModel errorModel);
    void onFailure(String strApiName, String message);
}
