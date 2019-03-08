package com.example.stackoverflowclient.feed;

import android.content.Context;

public interface IFeedInterface {

    interface IFeedView {
        void hideProgressView();

        void showAlertMessage(String message);

        void handleLogoutResponse();
    }

    interface IFeedPresenter {
        void initiateLogout(String accessToken);
    }

    interface IFeedInteractor{
        void sendLogoutRequest(String accessToken);
    }
}
