package com.example.admin.theoji;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.ModelClass.Student_Perform_Model;

public class Performance_month_act extends AppCompatActivity {

    Student_Perform_Model student_perform_model;
    WebView perform_web;
    String PerformUserID;
    WebChromeClient webChromeClient;
     String url="https://jntrcpl.com/theoji/index.php/Api/performance?id=" ;

    //Url url="https://jntrcpl.com/theoji/index.php/Api/performance?id=";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.performance_month_table);

        student_perform_model=(Student_Perform_Model)getIntent().getSerializableExtra("Student_perform_model") ;
        PerformUserID=getIntent().getStringExtra("PerformUserID");
       // Toast.makeText(this, "per ID "+PerformUserID, Toast.LENGTH_SHORT).show();

        perform_web = (WebView) findViewById(R.id.perform_web);
        perform_web.getSettings().setJavaScriptEnabled(true);
        perform_web.getSettings().setSupportZoom(true);
        perform_web.getSettings().setBuiltInZoomControls(true);
        perform_web.setVerticalScrollBarEnabled(true);
        perform_web.loadUrl("https://jntrcpl.com/theoji/index.php/Api/performance?id="+getIntent().getStringExtra("PerformUserID"));
        // OpenPerform_Graph();

        perform_web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

        });

        perform_web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {

            }

        });


//**************************************************************************
        TextView jan = (TextView) findViewById(R.id.january);
        TextView feb = (TextView) findViewById(R.id.feb);
        TextView march = (TextView) findViewById(R.id.march);
        TextView april = (TextView) findViewById(R.id.april);
        TextView may = (TextView) findViewById(R.id.may);
        TextView june = (TextView) findViewById(R.id.june);
        TextView july = (TextView) findViewById(R.id.july);
        TextView august = (TextView) findViewById(R.id.august);
        TextView sep = (TextView) findViewById(R.id.sep);
        TextView oct = (TextView) findViewById(R.id.oct);
        TextView nov = (TextView) findViewById(R.id.nov);
        TextView dec = (TextView) findViewById(R.id.dec);

        jan.setText(student_perform_model.getJanuary());
        feb.setText(student_perform_model.getFebruary());
        march.setText(student_perform_model.getMarch());
        april.setText(student_perform_model.getApril());
        may.setText(student_perform_model.getMay());
        june.setText(student_perform_model.getJune());
        july.setText(student_perform_model.getJuly());
        august.setText(student_perform_model.getAugust());
        sep.setText(student_perform_model.getSeptember());
        oct.setText(student_perform_model.getOctober());
        nov.setText(student_perform_model.getNovember());
        dec.setText(student_perform_model.getDecember());
//**************************************************************************************

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void OpenPerform_Graph() {

        perform_web.getSettings().setJavaScriptEnabled(true);
        perform_web.getSettings().setDomStorageEnabled(true);
        perform_web.getSettings().setSupportZoom(true);
        perform_web.getSettings().setBuiltInZoomControls(true);
        perform_web.getSettings().setUseWideViewPort(true);
        perform_web.getSettings().setAppCacheEnabled(true);
        perform_web.getSettings().setLoadsImagesAutomatically(true);
        perform_web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        perform_web.getSettings().setAllowFileAccessFromFileURLs(true);
        perform_web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        perform_web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        String url1=url+getIntent().getStringExtra("PerformUserID");

        perform_web.loadUrl("https://jntrcpl.com/theoji/index.php/Api/performance?id="+getIntent().getStringExtra("PerformUserID"));
       // perform_web.loadDataWithBaseURL(url, url+PerformUserID, "text/html", "UTF-8", null);

        Log.e("webView",url1);

    }
}
