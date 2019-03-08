package com.example.stackoverflowclient.login;

import android.content.Context;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.stackoverflowclient.utils.PreferenceHelper;
import com.example.stackoverflowclient.utils.StringConstants;

class LoginInteractor implements ILoginInterface.ILoginInteractor {
    private PreferenceHelper preference;
    private LoginPresenter presenter;

    LoginInteractor(Context context, LoginPresenter presenter) {
        preference = PreferenceHelper.getInstance(context);
        this.presenter = presenter;
    }

    @Override
    public LoginWebViewClient getWebViewClientInstance() {
        return new LoginWebViewClient();
    }

    class LoginWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            return shouldOverrideUrlLoading(uri.toString());
        }

        private boolean shouldOverrideUrlLoading(final String url) {
            return url.contains("access_token");
        }

        public void onPageFinished(WebView view, String url) {
            if (url.contains("access_token")) {
                presenter.onSuccess();

                String accessToken = getAccessTokenFromURL(url);
                storeDataInSharedPreference(StringConstants.ACCESS_TOKEN, accessToken);
            }
        }
    }

    private String getAccessTokenFromURL(String url) {
        String[] separated = url.split("=");
        return separated[1].trim();
    }

    @Override
    public void storeDataInSharedPreference(String key, String accessToken) {
        preference.put(key, accessToken);
    }
}
