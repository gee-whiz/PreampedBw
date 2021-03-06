package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class SelectPostBox extends Activity    {
    private static final String LOG = "Hey Gee" ;
    private static final String TAG = "hey Gee";
    Intent localIntent;
    View back;
    ImageView imgBack;
    Spinner sp;
    EditText pobox;
    RadioButton radPostBox;
    String selecteditem,selectedPostBox;
    List<String> SpinnerArray = new ArrayList<String>();
    String PostOfficeName,GroupId;
    View backView;
    PoboxObj poboxObj;
    Button Cancel,next;
    TextView tHolderType,tName,tPaidUntil,tSize,tStartDate,tStatus,tLastPaidUntil,tRenewalAmount,tPenaltyAmount,tNextPaidUntil,tTransactionHandle;
    String HolderType,Name,PaidUntil,Size,StartDate,Status,LastPaidUntil,RenewalAmount,PenaltyAmount,NextPaidUntil,TransactionHandle,poboxID;
    private List<PoboxObj> postofficesList = new ArrayList<PoboxObj>();
    private MySQLiteFunctions mysqliteFunction;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_post_box);

        pDialog = new ProgressDialog(this);

        setFields();
        poboxObj = new PoboxObj();
        localIntent = getIntent();

        GroupId = localIntent.getStringExtra("GroupId");
        PostOfficeName = localIntent.getStringExtra("PostOfficeName");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPostBox = "PO" + pobox.getText().toString();
                if (pobox.getText().length() < 3) {
                    pobox.setError("Please enter a valid post box starting with a digit");
                    return;

                } else {
                    if(radPostBox.isChecked()){
                        selectedPostBox = "PO" + pobox.getText().toString();   pDialog.setMessage("Please wait...");
                        pDialog.show();
                        GetPostBox(PostOfficeName, GroupId);
                    }
                   else
                    {
                        selectedPostBox = "PB" + pobox.getText().toString();   pDialog.setMessage("Please wait...");
                        pDialog.show();
                        GetPostBox(PostOfficeName, GroupId);
                    }



                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPostBox.this,RenewPoBox.class);
                startActivity(intent);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPostBox.this, RenewPoBox.class);
                startActivity(intent);
            }
        });


        pobox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (pobox.getText().toString().length() > 0) {
                    Log.d(LOG, "text greater than three");
                    pobox.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                } else {
                    pobox.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
               }


           }
       });
    }



    public  void  setFields()
    {
        pobox = (EditText)findViewById(R.id.edtPoBox);
        back = findViewById(R.id.btnBack);
        next = (Button)findViewById(R.id.btnNext);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        radPostBox = (RadioButton)findViewById(R.id.POBox);

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
                selectedPostBox = adapterView.getItemAtPosition(i).toString();
                Log.d(LOG, " selected post box " + selectedPostBox);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }
    private  void PostError()
    {

        LayoutInflater inflater = LayoutInflater.from(SelectPostBox.this);
        View promptView = inflater.inflate(R.layout.post_box_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectPostBox.this);
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
                pobox.setText("");
                pobox.setFocusable(true);
                pobox.setError("Please enter a valid post box");

            }
        });


    }


    public  void GetPostBox(final  String PostOfficeName,final  String GroupId )
    {
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_RENEWPOBOX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "post box  response " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Postoffices postoffices = new Postoffices();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RenewalAmount= jsonObject.getString("RenewalAmount");
                        HolderType = jsonObject.getString("HolderType");
                        Status = jsonObject.getString("Status");
                        PaidUntil = jsonObject.getString("PaidUntil");
                        LastPaidUntil = jsonObject.getString("LastPaidUntil");
                        PenaltyAmount = jsonObject.getString("PenaltyAmount");
                        NextPaidUntil = jsonObject.getString("NextPaidUntil");
                        TransactionHandle = jsonObject.getString("TransactionHandle");
                        StartDate = jsonObject.getString("StartDate");
                        poboxID = jsonObject.getString("PostBoxId");
                        Size = jsonObject.getString("Size");
                        Name = jsonObject.getString("Name");
                        poboxObj.setName(Name);
                        poboxObj.setSize(Size);
                        poboxObj.setPostBoxId(poboxID);
                        poboxObj.setHolderType(HolderType);
                        poboxObj.setPaidUntil(PaidUntil);
                        poboxObj.setNextPaidUntil(NextPaidUntil);
                        poboxObj.setLastPaidUntil(LastPaidUntil);
                        poboxObj.setRenewalAmount(RenewalAmount);
                        poboxObj.setPenaltyAmount(PenaltyAmount);
                        poboxObj.setStatus(Status);
                        poboxObj.setGroupId(GroupId);
                        poboxObj.setTransactionHandle(TransactionHandle);
                        postofficesList.add(poboxObj);
                        Log.d(LOG,"name "+ Name);
                    } catch (JSONException e) {
                    }
                }

                     if (poboxID != null) {
                         for (int i = 0; i < postofficesList.size(); i++) {
                             SpinnerArray.add(postofficesList.get(i).getName());
                             pDialog.dismiss();
                            // setSpinners();
                             Intent intent = new Intent(SelectPostBox.this, PostBox.class);
                             intent.putExtra("HolderType", poboxObj.getHolderType());
                             intent.putExtra("Name", Name);
                             intent.putExtra("Size", poboxObj.getSize());
                             intent.putExtra("PaidUntil", poboxObj.getPaidUntil());
                             intent.putExtra("Status", poboxObj.getStatus());
                             intent.putExtra("RenewalAmount", poboxObj.getRenewalAmount());
                             intent.putExtra("PenaltyAmount", poboxObj.getPenaltyAmount());
                             intent.putExtra("poboxID", poboxObj.getPostBoxId());
                             intent.putExtra("GroupId", GroupId);

                             startActivity(intent);
                         }
                     }

                else {
                         pDialog.dismiss();
                      PostError();
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
                params.put("function", "GetPostBox");
                params.put("groupId",GroupId);
                params.put("postBoxId",selectedPostBox);
                Log.d(LOG, "values sent from the device for selecting post box  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);




    }




}
