package com.example.moona.eventapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private KeywordAdapter keywordAdapter;
    private ProgressDialog progressDialog;
    private String locationUrl = "https://api.hel.fi/linkedevents/v1/place/?format=json&show_all_places=false&sort=name";
    private String keyWordUrl = "http://api.hel.fi/linkedevents/v0.1/keyword/?format=json";
    private String searchUrl = "https://api.hel.fi/linkedevents/v1/event/?format=json&include=";
    private static String status = "";
    private Spinner keywordSpinner;
    private static String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        new MyTask().execute(keyWordUrl, "keywords");
    }

    public void SearchClicked(View v)
    {
        searchUrl = "https://api.hel.fi/linkedevents/v1/event/?format=json&keyword=" + keyword;
        Toast.makeText(MainActivity.this, searchUrl, Toast.LENGTH_LONG).show();
        new MyTask().execute(searchUrl, "search");
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

            JSONObject result = null;
            status = params[1];
            try
            {
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
            }
            catch (Exception e)
            {
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

            if (status == "search")
            {
                JSONObject obj = null;
                JSONArray array = null;

                try
                {
                    obj = new JSONObject(String.valueOf(result));
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
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                dataAdapter = new DataAdapter(MainActivity.this, dataList);
                lv.setAdapter(dataAdapter);
            }

            if (status == "locations")
            {
                JSONObject obj = null;
                JSONArray array = null;

                try
                {
                    obj = new JSONObject(String.valueOf(result));
                    array = obj.getJSONArray("data");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                ArrayList<String> locations = new ArrayList<>();

                for (int i = 0; i < array.length(); i++)
                {
                    try
                    {
                        JSONObject data = null;
                        data = array.getJSONObject(i);
                        JSONObject desc = data.getJSONObject("address_locality");
                        String en_desc = desc.getString("en");

                        if (!locations.contains(en_desc))
                        {
                            locations.add(en_desc);
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

                Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);



                locationSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                locations));

            }

            if (status == "keywords")
            {
                JSONObject obj = null;
                JSONArray array = null;

                try
                {
                    obj = new JSONObject(String.valueOf(result));
                    array = obj.getJSONArray("data");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                ArrayList<Keyword> keywords = new ArrayList<>();

                for (int i = 0; i < array.length(); i++)
                {
                    try
                    {
                        JSONObject data = null;
                        data = array.getJSONObject(i);
                        String keywordId = data.getString("id");
                        JSONObject keyword = data.getJSONObject("name");
                        String en_keyword = keyword.getString("en");

                        if (!keywords.contains(en_keyword))
                        {
                            keywords.add(new Keyword(en_keyword, keywordId));
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

                keywordSpinner = findViewById(R.id.keywordSpinner);
                keywordAdapter = new KeywordAdapter(MainActivity.this, R.layout.spinner_item, keywords);
                keywordSpinner.setAdapter(keywordAdapter);
                keywordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        keyword = ((TextView)view.findViewById(R.id.max_discount_txt)).getText().toString();
                        searchUrl = "";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                    //keywordSpinner.setSelection(keywordAdapter.getPosition(myItem));

                //new MyTask().execute(locationUrl, "locations");

            }

        }
    }

}
