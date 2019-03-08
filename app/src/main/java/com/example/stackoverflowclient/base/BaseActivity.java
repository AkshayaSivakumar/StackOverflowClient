package com.example.stackoverflowclient.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.CookieManager;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.utils.StringConstants;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isNetworkAvailable()) {
            showAlertDialog(getString(R.string.alert_title), getString(R.string.network_error), getString(R.string.neutral_btn_text));
        }
    }

    protected void goToNextScreen(Context context, Class destinationClass, Bundle bundle) {
        Intent intent = new Intent(context, destinationClass);
        if (null != bundle)
            intent.putExtra(getString(R.string.bundle_key), bundle);
        startActivityForResult(intent, StringConstants.REQUEST_CODE);

    }

    protected void goToNextScreen(Context context, Class destinationClass) {
        Intent intent = new Intent(context, destinationClass);
        startActivity(intent);
    }

    protected void showAlertDialog(String alertTitle, String alertMessage, String positiveBtn) {
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setCancelable(false)
                .setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void showExitAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert_title))
                .setMessage(getString(R.string.exit_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.positive_btn_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.negative_btn_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void showAlertDialogWithTwoButtons(String alertTitle, String alertMessage, String positiveBtn, String negativeBtn) {
        new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setCancelable(false)
                .setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialogPositiveClicked();
                    }
                })
                .setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    protected void alertDialogPositiveClicked() {

    }

    protected void showProgress(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void hideProgress() {
        if (null != progressDialog && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    protected void logout() {
        showAlertDialogWithTwoButtons(getString(R.string.alert_title), getString(R.string.logout_msg), getString(R.string.positive_btn_text), getString(R.string.negative_btn_text));
    }

    protected void clearCookies() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
