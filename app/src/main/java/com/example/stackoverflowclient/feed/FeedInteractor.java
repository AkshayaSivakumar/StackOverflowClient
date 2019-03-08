package com.example.stackoverflowclient.feed;

import android.content.Context;

import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.model.QuestionsResponseModel;
import com.example.stackoverflowclient.network.ApiResponse;
import com.example.stackoverflowclient.network.ApiResponseListener;
import com.example.stackoverflowclient.network.ApiService;
import com.example.stackoverflowclient.network.RestApiClient;

import retrofit2.Call;

public class FeedInteractor implements IFeedInterface.IFeedInteractor {
    private Context context;
    private ApiResponseListener responseListener;

    public FeedInteractor(Context context, ApiResponseListener responseListener) {
        this.context = context;
        this.responseListener = responseListener;
    }

    /**
     * Since Stackoverflow API doesn't provide a graceful way to logout, the accessToken is invalidated and webview cache is cleared
     * References:
     * https://meta.stackexchange.com/questions/270938/how-to-logout-using-the-api
     * https://stackapps.com/questions/2831/how-to-provide-a-logout-button-for-authentication
     */
    @Override
    public void sendLogoutRequest(String accessToken) {
        // /access-tokens/{accessTokens}/invalidate
        ApiService apiService = RestApiClient.getClientInstance().getApiService();

        Call<QuestionsResponseModel> call = apiService.invalidateAccessToken(accessToken);
        ApiResponse.sendRequest(call, BaseConstants.LOGOUT_API_SERVICE, responseListener);
    }
}
