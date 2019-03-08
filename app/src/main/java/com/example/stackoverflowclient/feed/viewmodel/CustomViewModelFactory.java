package com.example.stackoverflowclient.feed.viewmodel;

import com.example.stackoverflowclient.network.ApiService;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private ApiService apiService;
    private String accessToken;
    private String serviceName;
    private String sortType;

    public CustomViewModelFactory(ApiService apiService, String accessToken, String serviceName, String sortType) {
        this.apiService = apiService;
        this.accessToken = accessToken;
        this.serviceName = serviceName;
        this.sortType = sortType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ItemViewModel(apiService, accessToken, serviceName, sortType);
    }
}
