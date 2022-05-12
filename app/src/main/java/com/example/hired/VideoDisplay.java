package com.example.hired;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private boolean mIsPaused = false;
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
        url = 0;
        urls = new ArrayList<String>();
        auth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference();

       /* Task<DataSnapshot> tasky = ref.getRoot().get();

        tasky.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                DataSnapshot snapshot = (DataSnapshot) tasky.getResult();
                for (DataSnapshot snappingTurtle : snapshot.getChildren()){
                    Company company = snappingTurtle.getValue(Company.class);
                    urls.add(company.getUrl());
                }

            }});

        tasky.addOnFailureListener(new OnFailureListener(){
            public void onFailure(Exception e){
                urls.add("youtube.com");
            }
        });*/

       /* ArrayList <Company> comps = new ArrayList<Company>();
        CompanyProfile prof = new CompanyProfile();

        //comps = prof.getCompanies();

        for (int i = 0; i<comps.size();i++)
            urls.add(comps.get(i).getUrl());

        if (urls.isEmpty())
            urls.add("youtube.com");*/

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("ArrayCheck", "In the method.");
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Company comp = snap.getValue(Company.class);
                    urls.add(comp.getUrl());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        urls.add("www.youtube.com");

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
        else{
            Toast.makeText(getApplicationContext(),"This is the last video.",
                    Toast.LENGTH_SHORT).show();
        }

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
            Toast.makeText(getApplicationContext(),"This is the first video.",
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