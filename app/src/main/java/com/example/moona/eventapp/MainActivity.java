package com.example.moona.eventapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
    private LocationAdapter locationAdapter;
    private ProgressDialog progressDialog;
    private String locationUrl = "https://api.hel.fi/linkedevents/v1/place/?format=json";
    private String keyWordUrl = "http://api.hel.fi/linkedevents/v0.1/keyword/?format=json";
    private String noImageAvailable = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/No_image_available_600_x_200.svg/2000px-No_image_available_600_x_200.svg.png";
    private String searchUrl;
    private static String status = "";
    private Spinner keywordSpinner;
    private Spinner locationSpinner;
    private static String keyword = "";
    private static String location = "";
    private String imageUrl;
    private String photographer;
    private String en_locationInfo;
    private String en_url;
    private TextView txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);
        txtDate = findViewById(R.id.txtDate);

        // HAETAAN SPINNERIIN HETI OHJELMAN ALKAESSA KEYWORDIT
        new MyTask().execute(keyWordUrl, "keywords");
    }

    public void SearchClicked(View v)
    {
        if (locationSpinner.getSelectedItem().toString().length() == 0) // TODO: EI TOIMI, KORJAA
        {
            searchUrl = "https://api.hel.fi/linkedevents/v1/event/?format=json&include=division,keyword&division=" + location + "&keyword=" + keyword;
            new MyTask().execute(searchUrl, "search");
        }
        else
        {
            Toast.makeText(MainActivity.this, locationSpinner.getSelectedItem().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class MyTask extends AsyncTask<String, Integer, JSONObject> {

        // TEHDÄÄN ENNEN PYYNTÖÄ
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        // PYÖRII TAUSTALLA, HAKEE JSONIA
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

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // TÄMÄ ALKAA, KUNNES BACKGROUND-PROSESSIT LOPPUU, JSONIN PARSINTA
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

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

            if (status == "search")
            {
                final ArrayList<Data> dataList = new ArrayList<>();

                for (int i = 0; i < array.length(); i++)
                {
                    try
                    {
                        JSONObject data = null;
                        data = array.getJSONObject(i);
                        String  en_name = data.getJSONObject("name").getString("en");
                        String en_desc = data.getJSONObject("short_description").getString("en");
                        String en_fullDesc = FormatText(data.getJSONObject("description").getString("en"));
                        String eventId = data.getString("id");
                        String date = data.getString("start_time");
                        JSONArray images = data.getJSONArray("images");

                        // PITÄÄ TEHDÄ, KOSKA MUUTEN JÄÄ HAKUTULOKSET VÄHÄISIKSI
                        try
                        {
                            en_locationInfo = "at " + data.getJSONObject("location_extra_info").getString("en");
                        }
                        catch (Exception e)
                        {
                            en_locationInfo = "";
                        }

                        try
                        {
                            en_url = data.getJSONObject("info_url").getString("en");
                        }
                        catch (Exception e)
                        {
                            en_url = "-";
                        }

                        // JSONIN SISÄLLÄ TAULUKKO, JOTEN PITÄÄ PYÖRITTÄÄ SILMUKASSA
                        if (images != null)
                        {
                            // JOS KUVAA EI OLE, KORVATAAN EI KUVAA-KUVALLA
                            imageUrl = noImageAvailable;
                            photographer = "-";

                            for (int j = 0; j < images.length(); j++)
                            {
                                JSONObject imageData = images.getJSONObject(j);

                                if (imageData != null)
                                {
                                    imageUrl = imageData.getString("url");
                                    photographer = imageData.getString("photographer_name");
                                }
                            }
                        }
                        dataList.add(new Data(en_name, FormatDates(date) + " " + en_locationInfo, en_desc, imageUrl, eventId, photographer, en_url, en_fullDesc));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                dataAdapter = new DataAdapter(MainActivity.this, dataList);
                lv.setAdapter(dataAdapter);

                // KUN KÄYTTÄJÄ VALITSEE JONKUN TAPAHTUMAN, AVAUTUU DIALOG
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int index, long id) {

                        final int selectedPosition = index;

                        LayoutInflater linf = LayoutInflater.from(MainActivity.this);
                        final View inflator = linf.inflate(R.layout.dialog, null);

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                        builder.setView(inflator);

                        // TYRKÄTÄÄN DATAA CUSTOM-DIALOGIIN
                        TextView dialogTitle = inflator.findViewById(R.id.dialogTitle);
                        dialogTitle.setText(dataAdapter.getItem(index).getmName());
                        TextView dialogDateAndLocation = inflator.findViewById(R.id.dialogDateAndLocation);
                        dialogDateAndLocation.setText(dataAdapter.getItem(index).getmDateAndLocation());
                        TextView dialogDescription = inflator.findViewById(R.id.dialogDescription);
                        dialogDescription.setText(dataAdapter.getItem(index).getmFullDescription());
                        TextView dialogPhotoInfo = inflator.findViewById(R.id.dialogPhotoInfo);
                        dialogPhotoInfo.setText(dataAdapter.getItem(index).getmPhotographer());
                        TextView dialogInfoUrl = inflator.findViewById(R.id.dialogUrl);
                        dialogInfoUrl.setText(dataAdapter.getItem(index).getmUrl());

                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
                    }
                });
            }
            else if (status == "locations")
            {
                ArrayList<Location> locations = new ArrayList<>();
                locations.add(new Location(""));

                for (int i = 0; i < array.length(); i++)
                {
                    String neighborhood = "";

                    try
                    {
                        JSONObject data = null;
                        data = array.getJSONObject(i);
                        JSONArray divisions = data.getJSONArray("divisions");

                        if (divisions != null)
                        {
                            for (int j = 0; j < divisions.length(); j++)
                            {
                                JSONObject divisionData = divisions.getJSONObject(j);

                                if (divisionData != null)
                                {
                                    String type = divisionData.getString("type");

                                    if (type.contains("neighborhood"))
                                    {
                                        // TÄMÄ VIRITYS, KOSKA JSONISSA ERI KOHDISSA TIETOJA
                                        if (type.contains("neighborhood"))
                                        {
                                            JSONObject location = divisionData.getJSONObject("name");
                                            neighborhood += location.getString("fi");
                                        }
                                        else if (type.contains("sub_district"))
                                        {
                                            JSONObject location = divisionData.getJSONObject("name");
                                            neighborhood += location.getString("fi");
                                        }
                                        else
                                        {
                                            JSONObject location = divisionData.getJSONObject("name");
                                            neighborhood += location.getString("fi");
                                        }

                                        if (!Contains(locations, neighborhood))
                                        {
                                            locations.add(new Location(neighborhood));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

                locationSpinner = findViewById(R.id.locationSpinner);
                locationAdapter = new LocationAdapter(MainActivity.this, R.layout.location_spinner, locations);
                locationSpinner.setAdapter(locationAdapter);
                locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        location = locationAdapter.getItem(pos).getmLocation();
                        searchUrl = "";
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        location = "";
                        searchUrl = "";
                    }
                });
            }
            else if (status == "keywords")
            {
                ArrayList<Keyword> keywords = new ArrayList<>();
                keywords.add(new Keyword("", ""));

                for (int i = 0; i < array.length(); i++)
                {
                    try
                    {
                        JSONObject data = null;
                        data = array.getJSONObject(i);
                        String keywordId = data.getString("id");
                        String en_keyword = data.getJSONObject("name").getString("en");

                        keywords.add(new Keyword(en_keyword, keywordId));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                keywordSpinner = findViewById(R.id.keywordSpinner);
                keywordAdapter = new KeywordAdapter(MainActivity.this, R.layout.keyword_spinner, keywords);
                keywordSpinner.setAdapter(keywordAdapter);
                keywordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        keyword = keywordAdapter.getItem(pos).getmKeywordId();
                        searchUrl = "";
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        keyword = "";
                        searchUrl = "";
                    }
                });

                // ALOITETAAN LOCATION-SPINNERIN TÄYTTÖ, KUNHAN EKA SPINNERI ON TÄYTETTY
                new MyTask().execute(locationUrl, "locations");
            }
        }
    }

    // FUNKTIO MUOTOILEE PÄIVÄMÄÄRÄT JÄRKEVIKSI
    public String FormatDates(String date)
    {
        return date.substring(0, 10);
    }

    // FUNKTIO, JOKA TARKISTAA ONKO LISTASSA JO JOKU
    public boolean Contains(ArrayList<Location> list, String name)
    {
        for (Location item : list)
        {
            if (item.getmLocation().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public String FormatText(String desc)
    {
        String ret = desc.replace("<p>", "");
        return ret.replace("</p>", "");
    }
}

