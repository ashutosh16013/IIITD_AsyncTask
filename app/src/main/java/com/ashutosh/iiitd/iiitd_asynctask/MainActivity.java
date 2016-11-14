package com.ashutosh.iiitd.iiitd_asynctask;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // URL Address
    String url = "https://iiitd.ac.in/about";
    ProgressDialog mProgressDialog;
    static final String m_key= "KEY_FOR_TEXT";
    String desc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        // Locate the Buttons in activity_main.xml
        Button descbutton = (Button) findViewById(R.id.descbutton);

        // Capture button click
        descbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // Execute Description AsyncTask
                boolean isConnected = checkConnectivity();
                if(isConnected==false)
                {
                    Toast.makeText(getApplicationContext(),"You are not connected", Toast.LENGTH_SHORT).show();
                }
                else
                    new Description().execute();
            }
        });

        if(savedInstanceState!=null) {
            Log.d(m_key, "IsNotNull");
            desc = savedInstanceState.getString(m_key);
            TextView txtdesc = (TextView) findViewById(R.id.desctxt);
            txtdesc.setText(desc);

        }

    }

    // Description AsyncTask
    private class Description extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("IIITD Async Task");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Using Elements to get the Meta data
                Elements description = document.getElementsByTag("p");
                desc =  description.get(6).text() + description.get(7).text() + description.get(8).text() + description.get(9).text();
                Log.i("INFO",description.text());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            TextView txtdesc = (TextView) findViewById(R.id.desctxt);
            txtdesc.setText(desc);
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        // Save the user's current game state
        //super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(m_key, desc);
        super.onSaveInstanceState(savedInstanceState);

    }

    public boolean checkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}