package com.example.stackoverflowclient.feed.datasource;

import com.example.stackoverflowclient.network.ApiService;

import java.util.concurrent.Executor;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class ItemDataSourceFactory extends DataSource.Factory {

    private final ApiService apiService;
    private final String accessToken;
    private final String serviceName;
    private String sortType;

    private MutableLiveData<ItemDataSource> mutableLiveData;

    private Executor executor;

    public ItemDataSourceFactory(ApiService apiService, String accessToken, String serviceName, String sortType, Executor executor) {
        this.apiService = apiService;
        this.accessToken = accessToken;
        this.serviceName = serviceName;
        this.sortType = sortType;

        this.mutableLiveData = new MutableLiveData<>();

        this.executor = executor;
    }

    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(apiService, accessToken, serviceName, sortType, executor);
        mutableLiveData.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<ItemDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

}
