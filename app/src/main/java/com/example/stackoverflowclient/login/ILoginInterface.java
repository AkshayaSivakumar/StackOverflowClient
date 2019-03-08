package com.example.stackoverflowclient.login;

public interface ILoginInterface {
    public interface ILoginView{
        void dismissLoginDialog();

        void goToFeedScreen();
    }

    public interface ILoginPresenter {
        void onSuccess();

        void onError();

        void putDefaultTokenValue();
    }

    public interface ILoginInteractor{
        LoginInteractor.LoginWebViewClient getWebViewClientInstance();

        void storeDataInSharedPreference(String key, String value);
    }
}
