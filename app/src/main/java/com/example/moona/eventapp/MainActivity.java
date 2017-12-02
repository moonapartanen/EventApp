package com.example.moona.eventapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
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
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);
        debug = (TextView) findViewById(R.id.debug);


    }

    public void SearchClicked(View v)
    {
        String url = "https://api.hel.fi/linkedevents/v1/search/?format=json&q=helsinki&type=event";
        new MyTask().execute(url);
    }

    private class MyTask extends AsyncTask<String, Integer, JSONObject> {

        // TEHDÄÄN ENNEN PYYNTÖÄ
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog= new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        // PYÖRII TAUSTALLA
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

        // TÄMÄ ALKAA, KUNNES BACKGROUND-PROSESSIT LOPPUU
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            JSONObject obj = null;
            JSONArray array = null;

            try
            {
                obj = new JSONObject(String.valueOf(result));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            try
            {
                array = obj.getJSONArray("data");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            ArrayList<Data> dataList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++)
            {

                try
                {

                    JSONObject data = null;

                    data = array.getJSONObject(i);


                    JSONObject name = data.getJSONObject("name");
                    JSONObject desc = data.getJSONObject("short_description");

                    String en_name = name.getString("en");
                    String en_desc = desc.getString("en");

                    dataList.add(new Data(en_name, en_desc));


                    debug.setText(name + " " + desc);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            dataAdapter = new DataAdapter(MainActivity.this, dataList);
            lv.setAdapter(dataAdapter);
        }
    }

}
