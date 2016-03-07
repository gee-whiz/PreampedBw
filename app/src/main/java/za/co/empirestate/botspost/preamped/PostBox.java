package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.Pipe;
import java.util.HashMap;
import java.util.Map;

public class PostBox extends Activity {
    private static final String LOG = "Hey Gee" ;
    private static final String TAG = "hey Gee";
    Intent localIntent;
    View back;
    String PostOfficeName,GroupId;
    View backView;
    PoboxObj poboxObj;
    Button Cancel;
    TextView tHolderType,tName,tPaidUntil,tSize,tStartDate,tStatus,tLastPaidUntil,tRenewalAmount,tPenaltyAmount,tNextPaidUntil,tTransactionHandle;
    String HolderType,Name,PaidUntil,Size,StartDate,Status,LastPaidUntil,RenewalAmount,PenaltyAmount,NextPaidUntil,TransactionHandle;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_box);
        setFields();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.show();
        poboxObj = new PoboxObj();
        localIntent = getIntent();
        if (poboxObj != null){
            updateFields();
           // pDialog.dismiss();
        }
        GroupId = localIntent.getStringExtra("GroupId");
        PostOfficeName = localIntent.getStringExtra("PostOfficeName");
        GetPostBox(GroupId, PostOfficeName);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostBox.this, RenewPoBox.class);
                startActivity(intent);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostBox.this, RenewPoBox.class);
                startActivity(intent);

            }
        });
    }



public  void  setFields()
{

    tName =  (TextView)findViewById(R.id.txtName);
    tHolderType = (TextView)findViewById(R.id.txtHolderType);
    tPenaltyAmount = (TextView)findViewById(R.id.txtPenalty);
    tRenewalAmount = (TextView)findViewById(R.id.txtRenewalAmount);
    tSize = (TextView)findViewById(R.id.txtSize);
    tStatus = (TextView)findViewById(R.id.txtStatus);
    tPaidUntil =(TextView)findViewById(R.id.txtUntil);
    tTransactionHandle = (TextView)findViewById(R.id.txtFee);
    Cancel = (Button)findViewById(R.id.btnCancel);
     back = (View)findViewById(R.id.btnBack);

}

    public  void updateFields(){


        tName.setText(poboxObj.getName());
        tPenaltyAmount.setText("P"+poboxObj.getPenaltyAmount()+".00");
        tRenewalAmount.setText("P"+poboxObj.getRenewalAmount()+".00");
        tSize.setText(poboxObj.getSize());
        tStatus.setText(poboxObj.getStatus());
        tPaidUntil.setText(poboxObj.getPaidUntil());
        tTransactionHandle.setText("P"+poboxObj.getTransactionHandle()+".00");
        tHolderType.setText(poboxObj.getHolderType());


    }

    public  void GetPostBox(final  String PostOfficeName,final  String GroupId )
    {
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_RENEWPOBOX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "pobox  response " + response.toString());
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
                        Size = jsonObject.getString("Size");
                        Name = jsonObject.getString("Name");
                        poboxObj.setName(Name);
                        poboxObj.setSize(Size);
                        poboxObj.setHolderType(HolderType);
                        poboxObj.setPaidUntil(PaidUntil);
                        poboxObj.setNextPaidUntil(NextPaidUntil);
                        poboxObj.setLastPaidUntil(LastPaidUntil);
                        poboxObj.setRenewalAmount(RenewalAmount);
                        poboxObj.setPenaltyAmount(PenaltyAmount);
                        poboxObj.setStatus(Status);
                        poboxObj.setTransactionHandle(TransactionHandle);
                         Log.d(LOG,"RenewalAmount "+ RenewalAmount);
                    } catch (JSONException e) {
                    }
                }
               if (response != null)
               {
                   pDialog.dismiss();
               }

                else {
                   Intent intent = new Intent(PostBox.this, RenewPoBox.class);
                   startActivity(intent);
               }
                if (poboxObj != null){
                    updateFields();
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
                params.put("postBoxId","PO100");
                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);




    }


}
