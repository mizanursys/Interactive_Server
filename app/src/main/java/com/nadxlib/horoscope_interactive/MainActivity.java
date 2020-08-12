package com.nadxlib.horoscope_interactive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<JSONObject> all_list;
    RecyclerView list_all_recyclerview;




    @SuppressLint({"ResourceAsColor", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        list_all_recyclerview = findViewById(R.id.list);

        list_all_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        all_list = new ArrayList<>();

        loadalldata();
        setupWindowAnimations();

    }


    @SuppressLint("NewApi")
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }



    public void loadalldata() {
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                //UrlsInterface.Mypost+"&u_api_token="+preferenceHelper.getApi_token(),
                "http://192.168.0.107/interactive/test_api.php?action=fetch_horoscope_list",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        all_list.clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray data = jsonObject.getJSONArray("horoscope");

                            Log.e("Responce From URL", String.valueOf(jsonObject));

                            Log.e("all_data", String.valueOf(data));


//                                JSONArray emailData = data.getJSONArray("post");


                            for (int i = 0; i < data.length(); i++) {
                                Log.e("employee_name", data.get(i).toString());

                                //getting all employees data
                                JSONObject dat = (JSONObject) data.get(i);

                                //RecyclerView Adapter
                                AllListAdapter adapter = new AllListAdapter(MainActivity.this, all_list);

                                list_all_recyclerview.setAdapter(adapter);

                                //insserting all data inside array list
                                all_list.add(dat);

                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERRROR>>>>>>>", error.toString());

                    }
                });

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

}