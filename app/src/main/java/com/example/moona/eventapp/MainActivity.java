package com.example.moona.eventapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void VolleyClicked(View v)
    {
        String url = "http://webd.savonia.fi/home/ktkoiju/j2me/test_json.php?dates";
        volleyStringRequest(url);
    }

    public void volleyStringRequest(String url){

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Toast.makeText(getApplicationContext(), "Tulin tähän", Toast.LENGTH_SHORT);
                //ArrayList<Data> dataList = new ArrayList<>();
                debug.setText(response.toString());


                /*try
                {
                    for(int i = 0; i < response.length(); i++)
                    {
                        JSONObject data = null;

                        data = response.getJSONObject(i);

                        String date = data.getString("data");
                        //String name = data.getString("nimi");

                        debug.setText(date);
                        //dataList.add(new Data(name, date));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }*/

                //dataAdapter = new DataAdapter(MainActivity.this, dataList);
                //lv.setAdapter(dataAdapter);

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){

                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT);
                    }
           }

        );

        requestQueue.add(jsonArrayRequest);


    }
}
