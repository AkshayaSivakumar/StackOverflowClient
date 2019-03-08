package com.example.stackoverflowclient.feed.viewmodel;

import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.feed.datasource.ItemDataSource;
import com.example.stackoverflowclient.feed.datasource.ItemDataSourceFactory;
import com.example.stackoverflowclient.model.ItemModel;
import com.example.stackoverflowclient.network.ApiService;
import com.example.stackoverflowclient.network.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class ItemViewModel extends ViewModel {
    private LiveData<PagedList<ItemModel>> itemPagedList;

    private LiveData<NetworkState> networkStateLiveData;
    private LiveData<ItemDataSource> dataSource;
    private Executor executor;

    public ItemViewModel(ApiService apiService, String accessToken, String serviceName, String sortType) {

        executor = Executors.newFixedThreadPool(5);

        ItemDataSourceFactory factory = new ItemDataSourceFactory(apiService, accessToken, serviceName, sortType, executor);
        dataSource = factory.getMutableLiveData();

        networkStateLiveData = Transformations.switchMap(factory.getMutableLiveData(), new Function<ItemDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(ItemDataSource source) {
                return source.getNetworkState();
            }
        });

        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(BaseConstants.PAGE_SIZE)
                .build();

        itemPagedList = (new LivePagedListBuilder<Integer, ItemModel>(factory, pageConfig))
                .setFetchExecutor(executor)
                .build();

    }

    public LiveData<PagedList<ItemModel>> getItemPagedList() {
        return itemPagedList;
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

}
