package com.example.stackoverflowclient.feed.datasource;

import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.model.ItemModel;
import com.example.stackoverflowclient.model.QuestionsResponseModel;
import com.example.stackoverflowclient.network.ApiResponse;
import com.example.stackoverflowclient.network.ApiResponseListener;
import com.example.stackoverflowclient.network.ApiService;
import com.example.stackoverflowclient.network.ErrorModel;
import com.example.stackoverflowclient.network.NetworkState;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;

public class ItemDataSource extends PageKeyedDataSource<Integer, ItemModel> implements BaseConstants {

    private static final int FIRST_PAGE = 1;

    private final ApiService apiService;
    private final String accessToken;
    private final String serviceName;
    private String sortType;

    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;
    private Executor retryExecutor;

    public ItemDataSource(ApiService apiService, String accessToken, String serviceName, String sortType, Executor retryExecutor) {
        this.apiService = apiService;
        this.accessToken = accessToken;
        this.serviceName = serviceName;
        this.sortType = sortType;

        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
        this.retryExecutor = retryExecutor;
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    private Call<QuestionsResponseModel> getRetrofitRequestCall(int page) {
        if (null == accessToken || "".equals(accessToken)) {
            return apiService.getQuestionsForAll(page, PAGE_SIZE, ORDER_DESCENDING, sortType, SITE, FILTER);
        } else {
            return apiService.getQuestionsForLoggedInUser(accessToken, KEY, FIRST_PAGE, PAGE_SIZE, ORDER_DESCENDING, sortType, SITE, FILTER);
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ItemModel> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        Call<QuestionsResponseModel> call = getRetrofitRequestCall(FIRST_PAGE);

        ApiResponse.sendRequest(call, serviceName, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                initialLoading.postValue(NetworkState.LOADED);
                networkState.postValue(NetworkState.LOADED);

                QuestionsResponseModel model = (QuestionsResponseModel) response;
                callback.onResult(model.component2(), null, FIRST_PAGE + 1);
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getError_id(), errorModel.getError_message(), serviceName));
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getError_id(), errorModel.getError_message(), serviceName));
            }

            @Override
            public void onFailure(String strApiName, String message) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, 600, message, serviceName));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ItemModel> callback) {
        networkState.postValue(NetworkState.LOADING);

        Call<QuestionsResponseModel> call = getRetrofitRequestCall(params.key);

        ApiResponse.sendRequest(call, serviceName, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                networkState.postValue(NetworkState.LOADED);

                Integer key = (params.key > 1) ? params.key - 1 : null;
                QuestionsResponseModel model = (QuestionsResponseModel) response;
                callback.onResult(model.component2(), key);
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getError_id(), errorModel.getError_message(), serviceName));
            }

            @Override
            public void onFailure(String strApiName, String message) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, 600, message, serviceName));
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ItemModel> callback) {
        networkState.postValue(NetworkState.LOADING);

        Call<QuestionsResponseModel> call = getRetrofitRequestCall(params.key);

        ApiResponse.sendRequest(call, serviceName, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                networkState.postValue(NetworkState.LOADED);

                QuestionsResponseModel model = (QuestionsResponseModel) response;
                Integer key = model.component1() ? params.key + 1 : null;
                callback.onResult(model.component2(), key);
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getError_id(), errorModel.getError_message(), serviceName));
            }

            @Override
            public void onFailure(String strApiName, String message) {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, 600, message, serviceName));
            }
        });
    }
}
