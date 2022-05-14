package com.example.hired;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;
import java.util.ArrayList;

@SuppressLint("SetJavaScriptEnabled")
public class VideoDisplay extends Activity
{
    private WebView mWebView;
    private boolean mIsPaused;
    private Button userBut;
    private Button previous;
    private Button advance;
    private int url;
    private ArrayList<String> urls;
    private DatabaseReference ref;
    private FirebaseAuth auth;

    /**
     * Initializes video class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mIsPaused = false;
        url = 0;
        urls = new ArrayList<String>();
        auth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();

        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snappingTurtle : snapshot.getChildren()){
                    Company company = snappingTurtle.getValue(Company.class);
                    urls.add(company.getUrl());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                urls.add("www.youtube.com");
            }
        });*/

        /*for (int i = 0; i<ProfileActivity.getCompArray().size();i++)
            urls.add(ProfileActivity.getCompArray().get(i).getUrl());

        if (urls.isEmpty())
            urls.add("youtube.com");*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        userBut = (Button) findViewById(R.id.back_user);
        previous = (Button) findViewById(R.id.previous_video);
        advance = (Button) findViewById(R.id.proceed);

        //Button Methods
        userBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }});
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousScreen(urls);
            }});
        advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advanceScreen(urls);
            }});


        mWebView = (WebView) findViewById(R.id.webviewer);

        final WebView webview = (WebView) findViewById(R.id.webviewer);
        webview.setWebViewClient(new WebViewClient());

        WebSettings websetting = webview.getSettings();
        websetting.setJavaScriptEnabled(true);
        websetting.setDomStorageEnabled(true);
        mIsPaused = true;
        resumeBrowser();
        webview.loadUrl(urls.get(url));

    }

    /**
     * Plays the next video
     * @param url_list list of youtube urls
     */
    public void advanceScreen(ArrayList<String> url_list)
    {
        resumeBrowser();
        url++;
        if (url<url_list.size()-1)
        mWebView.loadUrl(url_list.get(url));
        //eventually add a message saying "you've reached the end"
        //or don't show the next button on the last video
    }

    /**
     * Plays the previous video
     * @param url_list list of youtube urls
     */
    public void previousScreen(ArrayList<String> url_list)
    {
        resumeBrowser();
        url--;

        if (url>-1)
        mWebView.loadUrl(url_list.get(url));

        else{
            Toast.makeText(getApplicationContext(),"hey bestie this is the first vid ",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Pauses the screen
     */
    @Override
    protected void onPause()
    {
        pauseBrowser();
        super.onPause();
    }


    /**
     * Resumes the screen
     */
    @Override
    protected void onResume()
    {
        resumeBrowser();
        super.onResume();
    }

    /**
     * Hidden method to pause webpage
     */
    private void pauseBrowser()
    {
        if (!mIsPaused)
        {
            // pause flash and javascript etc
            callHiddenWebViewMethod(mWebView, "onPause");
            mWebView.pauseTimers();
            mIsPaused = true;
        }
    }

    /**
     * Hidden method to resume webpage
     */
    private void resumeBrowser()
    {
        if (mIsPaused)
        {
            //resume flash and javascript etc
            callHiddenWebViewMethod(mWebView, "onResume");
            mWebView.resumeTimers();
            mIsPaused = false;
        }
    }

    /**
     * Calls hidden method
     * @param wv webview
     * @param name name of method call
     */
    private void callHiddenWebViewMethod(final WebView wv, final String name)
    {
        try
        {
            final Method method = WebView.class.getMethod(name);
            method.invoke(mWebView);
        } catch (final Exception e)
        {}
    }
}