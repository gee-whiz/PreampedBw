package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class UpdateMeterNumber extends Activity {
    private static final String LOG = "Hey George" ;
    View backView;
    ImageButton backImage;
    EditText newMeterNumber;
    String meterNumber;
    String smeterNumber,curMeterNumber;
    ImageButton update,Delete;
    int num;
    String phone,email;
    Long deleteId;
    String toDelete;
    Spinner deleteSp;
    Context ctx;
    String newPhone;
    private MySQLiteFunctions mysqliteFunction;
    private  String[] mmeters;
    private ProgressDialog pDialog;
    private String tag_json_obj = "hey gee";
    private String TAG = "hey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meter_number);
        ctx = getApplication();
        this.mysqliteFunction = new MySQLiteFunctions(this);
        pDialog = new ProgressDialog(UpdateMeterNumber.this);
        setFields();
        setSpinners();
        smeterNumber = mysqliteFunction.getMeterNumber();
         curMeterNumber = mysqliteFunction.getMeterNumber();
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meterNumber = newMeterNumber.getText().toString();
                if (meterNumber.isEmpty())
                {
                    newMeterNumber.setError("Meter number can not be empty");
                    return;
                }
                if (meterNumber.length() < 11)
                {
                    newMeterNumber.setError("Meter number should be 11 digits");
                    return;
                }
                else {
                    pDialog.setMessage("Please wait");
                    pDialog.show();
                    verifyMeterNumber(meterNumber);
                }

            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toDelete.equalsIgnoreCase(smeterNumber))
                {
                    ShowPrimaryMeterError();

                }
                else {
                    ShowDeleteAlert(deleteId);
                }
            }
        });
    }
    public  void setFields(){
        backView = findViewById(R.id.btnBack);
        backImage = (ImageButton)findViewById(R.id.imgBack);
        newMeterNumber = (EditText)findViewById(R.id.edtNewMeter);
        update = (ImageButton)findViewById(R.id.btnUpdate);
        Delete = (ImageButton)findViewById(R.id.btnDelete);
        deleteSp = (Spinner)findViewById(R.id.spDeleteMeter);

    }
    public void  setSpinners(){
        mysqliteFunction.getAllMeterNumbers();
        mmeters = mysqliteFunction.getAllM();
        Log.d(LOG, "All meter numbers " + mmeters);
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0;i < mmeters.length;i++){
            Log.e(LOG,"nuuuuu"+mmeters[i].length());
            if (mmeters[i].length() > 9) {
                tmp.add(mmeters[i]);
            }
        }

        tmp.removeAll(Collections.singleton(null));
        Log.d(LOG, "All meter numbers " + mmeters);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_custom_spinner, tmp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deleteSp.setAdapter(adapter);
        num = 0;
        deleteSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                toDelete = deleteSp.getSelectedItem().toString();

                num += 1;
                if (num > 1) {
                    deleteId = id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private  void AddNewMeterNumber(final String email,final  String phone,final String newMeterNumber ){
        final StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_BOTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "Add new meter Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("phone", phone);
                params.put("newMeterNumber",newMeterNumber);
                Log.e(LOG, "Values from the device " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


    public  void ShowDeleteAlert(Long id)
    {
        if (deleteId == null) {
            long idd = 1;
            deleteId = idd;
        } else {
            deleteId = id + 1;
        }
        LayoutInflater inflater = LayoutInflater.from(UpdateMeterNumber.this);
        View promptView = inflater.inflate(R.layout.exit_alert, null);
               AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMeterNumber.this);
               builder.setView(promptView);
               builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
               });
               builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                mysqliteFunction.delete_byID(deleteId);
                phone = mysqliteFunction.getPhone();
                email = mysqliteFunction.getEmail();
                newPhone = phone.substring(1);
                DeleteMeterNumber(email, newPhone, toDelete);
                dialog.dismiss();
                finish();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }



    private  void verifyMeterNumber(final  String meterNumber){
        final StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_VERIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "`eskom  Response: " + response.toString());
                String res = response.toString();
               if (res.equalsIgnoreCase("success") )
               {
                   pDialog.dismiss();
                   Log.d(LOG, "Sucessssss" + response);
                   Log.d(LOG, "meter number " + meterNumber);
                   phone = mysqliteFunction.getPhone();
                   if (curMeterNumber.length() < 10){
                       mysqliteFunction.updateMeterNumber(meterNumber);
                       mysqliteFunction.AddNewMeterNumber(meterNumber);
                       email = mysqliteFunction.getEmail();
                       newPhone = phone.substring(1);
                       pDialog.setMessage("Updating Please wait");
                       pDialog.show();
                       UpdateUser(meterNumber, email, newPhone, newPhone);
                       AddNewMeterNumber(email,newPhone,meterNumber);
                       Intent intent = new Intent(ctx,AccountDetailsActivity.class);
                       startActivity(intent);
                       finish();
                   }
                   else {
                       mysqliteFunction.AddNewMeterNumber(meterNumber);
                       email = mysqliteFunction.getEmail();
                       newPhone = phone.substring(1);

                       AddNewMeterNumber(email, newPhone, meterNumber);
                       Intent intent = new Intent(ctx, MainActivity.class);
                       startActivity(intent);
                       finish();
                   }
               }

                else
               {
                   Log.d(LOG,"errorrrrrrr"+ response);
                   ShowMeterError();
                   pDialog.dismiss();
               }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("meter_number",meterNumber);
                Log.e(LOG, "Values from the device " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }



    private  void ShowMeterError()
    {

        LayoutInflater inflater = LayoutInflater.from(UpdateMeterNumber.this);
        View promptView = inflater.inflate(R.layout.meter_alert, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMeterNumber.this);
        builder.setView(promptView);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private  void ShowPrimaryMeterError()
    {

        LayoutInflater inflater = LayoutInflater.from(UpdateMeterNumber.this);
        View promptView = inflater.inflate(R.layout.primary_meter_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMeterNumber.this);
        builder.setView(promptView);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    private  void DeleteMeterNumber(final String email,final  String phone,final String newMeterNumber){


        final StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_DELETE_METER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "Delete meter Response: " + response.toString());
                String res = response.toString();
                if (res.equalsIgnoreCase("success") ) {
                    pDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("",email);
                params.put("phone",  phone);
                params.put("newMeterNumber",newMeterNumber);
                Log.e(LOG, "Values from the device " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }




    public  void  UpdateUser(final String meterNumber,final String email, final  String phone,final  String newPhone){
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "update meter  response " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (response != null){
                            pDialog.dismiss();
                        }


                    } catch (JSONException e) {
                    }
                }


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
                params.put("tag", "update_details");
                params.put("meter_number",meterNumber);
                params.put("email",email);
                params.put("phone",phone);
                params.put("cur_phone",newPhone);

                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
}
