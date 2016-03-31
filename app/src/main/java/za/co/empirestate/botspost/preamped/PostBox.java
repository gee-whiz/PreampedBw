package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class PostBox extends Activity {
    private static final String LOG = "Hey Gee" ;
    private static final String TAG = "hey Gee";
    Intent localIntent;
    View back;
    String PostOfficeName,GroupId;
    View backView;
    PoboxObj poboxObj;
    Button cancel,next;

    double t,tot,pen;
    TextView tHolderType,tName,tPaidUntil,total,tSize,tStartDate,tStatus,tLastPaidUntil,tRenewalAmount,tPenaltyAmount,tNextPaidUntil,tTransactionHandle;
    String HolderType,totalSt,Name,PaidUntil,Size,StartDate,Status,LastPaidUntil,RenewalAmount,PenaltyAmount,NextPaidUntil,TransactionHandle,poboxID;
    private MySQLiteFunctions mysqliteFunction;
    private ProgressDialog pDialog;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_box);
        setFields();

        this.mysqliteFunction = new MySQLiteFunctions(this);
         poboxObj = new PoboxObj();

          localIntent = getIntent();
            updateFields();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostBox.this, MainActivity.class);
                startActivity(intent);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostBox.this, MainActivity.class);
                startActivity(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PostBox.this.mysqliteFunction.checkPaymentHistory())
                {
                    Intent localIntent1 = new Intent(PostBox.this, ConfirmPurchaseActivity.class);
                    localIntent1.putExtra("amount",String.valueOf(tot));
                    localIntent1.putExtra("isNew", false);
                    localIntent1.putExtra("meter_number", localIntent.getStringExtra("poboxID"));
                    localIntent1.putExtra("groupId",localIntent.getStringExtra("GroupId"));
                     PostBox.this.startActivity(localIntent1);
                    overridePendingTransition(R.anim.from, R.anim.to);
                    return;
                }
                Intent localIntent2 = new Intent(PostBox.this, PaymentDetailsActivity.class);
                Log.d(LOG, "sending first Selected meter number " + localIntent.getStringExtra("poboxID"));
                localIntent2.putExtra("amount",String.valueOf(tot));
                localIntent2.putExtra("meter_number", localIntent.getStringExtra("poboxID"));
                localIntent2.putExtra("groupId",localIntent.getStringExtra("GroupId"));
                Log.d(LOG,"group id "+localIntent.getStringExtra("GroupId"));
                PostBox.this.startActivity(localIntent2);



            }

        });


    }



public  void  setFields()
{
    cancel = (Button)findViewById(R.id.btnCancel);
    tName =  (TextView)findViewById(R.id.txtName);
    tHolderType = (TextView)findViewById(R.id.txtHolderType);
    tPenaltyAmount = (TextView)findViewById(R.id.txtPenalty);
    tRenewalAmount = (TextView)findViewById(R.id.txtRenewalAmount);
    tSize = (TextView)findViewById(R.id.txtSize);
    tStatus = (TextView)findViewById(R.id.txtStatus);
    tPaidUntil =(TextView)findViewById(R.id.txtUntil);
    tTransactionHandle = (TextView)findViewById(R.id.txtFee);
     back = (View)findViewById(R.id.btnBack);
    next = (Button)findViewById(R.id.btnNext);
    total = (TextView)findViewById(R.id.txtTotal);

}

    public  void updateFields(){

        tName.setText( localIntent.getStringExtra("Name"));
        tPenaltyAmount.setText("P"+ localIntent.getStringExtra("PenaltyAmount")+".00");
        tRenewalAmount.setText("P"+ localIntent.getStringExtra("RenewalAmount")+".00");
        tSize.setText(localIntent.getStringExtra("Size"));
        tStatus.setText(localIntent.getStringExtra("Status"));
        tPaidUntil.setText( localIntent.getStringExtra("PaidUntil"));
        tTransactionHandle.setText("P11.20");
        tHolderType.setText( localIntent.getStringExtra("HolderType"));
        RenewalAmount = localIntent.getStringExtra("RenewalAmount");
        PenaltyAmount = localIntent.getStringExtra("PenaltyAmount");
        TransactionHandle = "11.20";
         t =  Double.parseDouble(RenewalAmount);
         pen = Double.parseDouble(TransactionHandle);
         tot = t + pen;
        total.setText("P"+String.format("%.2f",tot));




    }




}
