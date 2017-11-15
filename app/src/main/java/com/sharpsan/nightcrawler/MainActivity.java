package com.sharpsan.nightcrawler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.lang.String;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Thread downloadThread = new Thread() {
            public void run() {
                Document googleSearchResults;
                try {
                    googleSearchResults = Jsoup.connect("https://www.google.com/search?hl=en&q=linkin+park+intitle%3A\"index.of\"+\"parent+directory\"+\"size\"+\"last+modified\"+\"description\"+%5Bsnd%5D+%28mp4%7Cmp3%7Cavi%29+-inurl%3A%28jsp%7Cphp%7Chtml%7Caspx%7Chtm%7Ccf%7Cshtml%7Clyrics%7Cmp3s%7Cmp3%7Cindex%29+-gallery+-intitle:\"last+modified\"+-intitle:(intitle|mp3)").get();
                    Elements serverIndexUrls = googleSearchResults.select("cite[class$=_Rm]");
                    for (Element serverIndexUrl : serverIndexUrls) {
                        String serverIndexUrlStr = serverIndexUrl.text();
                        Log.d("gss", serverIndexUrlStr);

                        Document serverIndexResults;
                        try {
                            serverIndexResults = Jsoup.connect("http://" + serverIndexUrlStr).get();
                            Elements anchors = serverIndexResults.select("a");
                            for (Element anchor : anchors) {
                                String songFileUrl = anchor.attr("href");
                                //if(songFileUrl.contains("linkin park"))
                                Log.d("gss", songFileUrl);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d("gss", "--------");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        downloadThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}