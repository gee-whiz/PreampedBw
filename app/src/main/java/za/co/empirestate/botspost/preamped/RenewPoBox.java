package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenewPoBox extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String LOG = "Hey Gee" ;
    private static final String TAG = "hey Gee";
    View backView;
    ImageButton backImage;
    Spinner sp;
    String groupId;
    Button next;
    String selecteditem,selectedPostOffice;
    List<String> SpinnerArray = new ArrayList<String>();
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ProgressDialog pDialog;
    private List<Postoffices> postofficesList = new ArrayList<Postoffices>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_po_box);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        setFields();
        pDialog.show();
        Postoffices postoffices = new Postoffices();
        if (postoffices != null){
            GetPostOffices();
        }



        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RenewPoBox.this, MainActivity.class);
                startActivity(intent);

            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RenewPoBox.this,MainActivity.class);
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RenewPoBox.this,SelectPostBox.class);
                intent.putExtra("GroupId",selecteditem);
                intent.putExtra("PostOfficeName",selectedPostOffice);
                startActivity(intent);
                finish();
            }
        });

    }



    public void setFields(){
        backView = findViewById(R.id.btnBack);
        backImage = (ImageButton)findViewById(R.id.imgBack);
        sp = (Spinner)findViewById(R.id.spPoAmount);
        next = (Button)findViewById(R.id.btnNext);

    }



    public  void setSpinners(){


        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, SpinnerArray);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp.setAdapter(spinnerAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                Postoffices postoffices = postofficesList.get(i);
                selecteditem = postoffices.getGroupID();
                selectedPostOffice = postoffices.getPostOfficeName();
                        Log.d(LOG, " selected group id " + selecteditem);
                Log.d(LOG, " selected postoffice " + selectedPostOffice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



}
    public  void GetPostOffices()
    {
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_RENEWPOBOX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                for (int i = 0; i < 140; i++) {
                     try {
                    JSONArray jsonArray = new JSONArray(response);
                    Postoffices postoffices = new Postoffices();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("PostOfficeName");
                    String id = jsonObject.getString("GroupId");
                    postoffices.setGroupID(id);
                    postoffices.setPostOfficeName(name);
                    postofficesList.add(postoffices);
                    } catch (JSONException e) {
                     }
                }
                for (int i = 0; i < postofficesList.size(); i++) {

                    SpinnerArray.add(" " +postofficesList.get(i).getPostOfficeName());

                }
                pDialog.dismiss();
                setSpinners();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("function", "GetPostOffices");
                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);




    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        Toast.makeText(
                getApplicationContext(),
                parent.getItemAtPosition(i).toString() + " Selected" ,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Log.d(LOG,"Nothing");

    }
}
