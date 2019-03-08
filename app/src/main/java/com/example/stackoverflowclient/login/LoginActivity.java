package com.example.stackoverflowclient.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.base.BaseActivity;
import com.example.stackoverflowclient.base.BaseConstants;
import com.example.stackoverflowclient.feed.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements ILoginInterface.ILoginView {

    private Dialog loginDialog;
    private WebView loginWebView;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        presenter = new LoginPresenter(this, this);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        createAndShowLoginDialog();
    }

    private void createAndShowLoginDialog() {
        loginDialog = new Dialog(this);
        loginDialog.setCancelable(true);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.setContentView(R.layout.layout_login_web_view);
        loginDialog.setTitle(getString(R.string.login_to_stackoverflow));

        loginWebView = (WebView) loginDialog.findViewById(R.id.login_web_view);
        setUpWebView();

        loginDialog.show();
    }

    private void setUpWebView() {
        WebSettings webSettings = loginWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        loginWebView.getSettings().setBuiltInZoomControls(true);
        loginWebView.getSettings().setLoadWithOverviewMode(true);
        loginWebView.getSettings().setUseWideViewPort(true);
        loginWebView.setWebViewClient(presenter.loginWebViewClient());

        loginWebView.loadUrl(BaseConstants.LOGIN_URL);
    }

    @OnClick(R.id.btn_skip)
    public void skipLogin() {
        presenter.putDefaultTokenValue();
        goToNextScreen(this, MainActivity.class);
        finish();
    }

    @Override
    public void dismissLoginDialog() {
        if (null != loginDialog && loginDialog.isShowing())
            loginDialog.dismiss();
    }

    /**
     * Since Stackoverflow API doesn't provide a graceful way to logout, the accessToken is invalidated and webview cache is cleared
     * References:
     * https://meta.stackexchange.com/questions/270938/how-to-logout-using-the-api
     * https://stackapps.com/questions/2831/how-to-provide-a-logout-button-for-authentication
     */
    @Override
    public void goToFeedScreen() {
        loginWebView.clearCache(true);
        loginWebView.clearHistory();
        clearCookies();
        goToNextScreen(LoginActivity.this, MainActivity.class);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != loginWebView && this.loginWebView.canGoBack()) {
                this.loginWebView.goBack();
                return true;
            } else {
                dismissLoginDialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
