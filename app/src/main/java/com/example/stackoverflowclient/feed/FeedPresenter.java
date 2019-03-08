package com.example.stackoverflowclient.feed;

import android.content.Context;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.network.ApiResponseListener;
import com.example.stackoverflowclient.network.ErrorModel;

public class FeedPresenter implements IFeedInterface.IFeedPresenter, ApiResponseListener {
    private final FeedInteractor interactor;
    private Context context;
    private IFeedInterface.IFeedView feedView;

    public FeedPresenter(Context context, IFeedInterface.IFeedView feedView) {
        this.context = context;
        this.feedView = feedView;

        interactor = new FeedInteractor(context, this);
    }

    /**
     * Since Stackoverflow API doesn't provide a graceful way to logout, the accessToken is invalidated and webview cache is cleared
     * References:
     * https://meta.stackexchange.com/questions/270938/how-to-logout-using-the-api
     * https://stackapps.com/questions/2831/how-to-provide-a-logout-button-for-authentication
     */
    @Override
    public void initiateLogout(String accessToken) {
        if (null != accessToken && !"".equals(accessToken)) {
            interactor.sendLogoutRequest(accessToken);
        } else {
            feedView.hideProgressView();
            feedView.showAlertMessage("Access Token is empty");
        }
    }

    @Override
    public void onSuccess(String strApiName, Object response) {
        feedView.hideProgressView();
        if (BaseConstants.LOGOUT_API_SERVICE.equals(strApiName)) {
            feedView.showAlertMessage("Logout successful");
            feedView.handleLogoutResponse();
        }
    }

    @Override
    public void onError(String strApiName, ErrorModel errorModel) {
        feedView.hideProgressView();
        if (errorModel.component1() == 401)
            feedView.showAlertMessage(context.getString(R.string.error_msg_401));
    }

    @Override
    public void onFailure(String strApiName, String message) {
        feedView.hideProgressView();
        feedView.showAlertMessage(message);
    }
}
