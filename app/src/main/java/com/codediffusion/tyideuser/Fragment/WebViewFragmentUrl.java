package com.codediffusion.tyideuser.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;


import com.codediffusion.tyideuser.R;

import im.delight.android.webview.AdvancedWebView;


public class WebViewFragmentUrl extends Fragment implements AdvancedWebView.Listener{
    private String my_url,UrlLinks;
    private AdvancedWebView mWebView;
    private ProgressBar progressBar;
    public WebViewFragmentUrl() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_url, container, false);

            // UrlLinks="http://2in1taxi.com/privacy_policy.php";
             UrlLinks="https://www.tatd.in/terms-and-conditions.php";


        Initialization(view);
    return view;
    }

    private void Initialization(View view) {
        getUrl(view);


    }

    private void getUrl(View view) {
        progressBar=view.findViewById(R.id.progressBar);
       /* if (getIntent()!=null){
            if (getIntent().hasExtra(Commn.web_Url)){
                my_url=getIntent().getStringExtra(Commn.web_Url);
                Log.d("Url",my_url);
                loadPage();
            }
        }*/

        //my_url="https://bhagwadgita.jagatgururampalji.org/";
        my_url=UrlLinks;
        Log.d("Url",my_url);
        loadPage(view);


    }

    private void loadPage(View view) {
        mWebView = (AdvancedWebView) view.findViewById(R.id.webview);
        mWebView.clearCache(true);
        //mWebView.setListener(getActivity(), (AdvancedWebView.Listener) getActivity());
        mWebView.loadUrl(my_url);
    }
    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        progressBar.setVisibility(View.VISIBLE);

        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

   /* @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.getActivity().onBackPressed();
    }*/



    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }




}