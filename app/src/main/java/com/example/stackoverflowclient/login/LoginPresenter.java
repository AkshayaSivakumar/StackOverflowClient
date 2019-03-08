package com.example.stackoverflowclient.login;

import android.content.Context;
import android.webkit.WebViewClient;

import com.example.stackoverflowclient.utils.StringConstants;

class LoginPresenter implements ILoginInterface.ILoginPresenter {
    private LoginInteractor interactor;
    private ILoginInterface.ILoginView loginView;

    public LoginPresenter(Context context, ILoginInterface.ILoginView loginView) {
        interactor = new LoginInteractor(context, this);
        this.loginView = loginView;
    }

    public WebViewClient loginWebViewClient() {
        return interactor.getWebViewClientInstance();
    }

    @Override
    public void onSuccess() {
        loginView.dismissLoginDialog();
        loginView.goToFeedScreen();
    }

    @Override
    public void onError() {

    }

    @Override
    public void putDefaultTokenValue() {
        interactor.storeDataInSharedPreference(StringConstants.ACCESS_TOKEN, null);
    }
}
