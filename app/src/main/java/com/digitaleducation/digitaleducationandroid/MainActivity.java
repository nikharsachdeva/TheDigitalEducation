package com.digitaleducation.digitaleducationandroid;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
    WebView wb;
    Dialog dialog;
    Boolean isavailable;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isavailable = checkInternet();
        if (isavailable) {

            wb = (WebView) findViewById(R.id.webView1);
            initDialog();
            wb.getSettings().setJavaScriptEnabled(true);
            wb.getSettings().setLoadWithOverviewMode(true);
            wb.getSettings().setUseWideViewPort(true);
            wb.getSettings().setSupportZoom(true);
            wb.getSettings().setBuiltInZoomControls(false);
            wb.getSettings().setPluginState(WebSettings.PluginState.ON);
            wb.setWebViewClient(new HelloWebViewClient());
            wb.loadUrl("https://www.thedigitaleducation.org/");

        } else {
            Toast.makeText(this, "Please Check Your Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    private Boolean checkInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogwholeapp);
        ImageView imgtoberotated = (ImageView) dialog.findViewById(R.id.imagetoberotated);
        ImageView closebtnimg = dialog.findViewById(R.id.closebtnimg);
        ObjectAnimator animation = ObjectAnimator.ofFloat(imgtoberotated, "rotationY", 0.0f, 360f);
        animation.setDuration(1500);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
//        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        rotation.setFillAfter(true);
//        imgtoberotated.startAnimation(rotation);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        closebtnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (url.startsWith("tel:") || url.startsWith("whatsapp:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wb.canGoBack()) {
                        wb.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}