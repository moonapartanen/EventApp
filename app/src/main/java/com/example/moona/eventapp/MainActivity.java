package com.example.moona.eventapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private DataAdapter dataAdapter;
    private TextView debug;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lv = (ListView) findViewById(R.id.lv);
        debug = (TextView) findViewById(R.id.debug);

    }

    public void SearchClicked(View v)
    {
        String url = "http://api.hel.fi/linkedevents/v0.1/event/?end=2014-01-20&format=json&start=2014-01-15";
        new MyTask().execute(url);
    }

    private class MyTask extends AsyncTask<String, Integer, JSONObject> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected JSONObject doInBackground(String... params) {
            // get the string from params, which is an array
            // First try to retrieve the url
            JSONObject result = null;  //initializing the result we'll return
            try{
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();


                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK)
                {

                    InputStream is = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String currentLine = "";

                    while((currentLine = br.readLine()) != null){

                        sb.append(currentLine);
                    }

                    result = new JSONObject(sb.toString());

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return result;

        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            debug.setText(result.toString());
            // Do things like hide the progress bar or change a TextView
        }
    }

}
