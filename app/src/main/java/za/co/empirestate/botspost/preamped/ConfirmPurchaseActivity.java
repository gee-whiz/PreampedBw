package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import za.co.empirestate.botspost.model.Payment;
import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

/**
 * Created by jojo on 1/27/2015.
 */
public class ConfirmPurchaseActivity extends Activity {

    private static final String LOG = "Hey Gee" ;
    private String amount;
    private String name;
    private String cardNumber;
    private String meterNumber;
    private String cvv;
    private String expMonth,expYear;
    private String surname;
    private MySQLiteFunctions mysqliteFunction;
    private Payment payment;
    private String phone;
    private static String dialogMsg;
    private String token,units,date,time;
    private String reference = null;
    private String last3Digits;
    private String vatAndLevy;
    private String costOfUnits;
    int tokenAmnt;
    static String iv,shaKey;
    AES aes;
    Intent localIntent;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_confirm_purchase);
        this.mysqliteFunction = new MySQLiteFunctions(this);
        localIntent = getIntent();
        this.payment = ((Payment)localIntent.getParcelableExtra("payment_details"));

        phone = mysqliteFunction.getPhone();
        //meterNumber = mysqliteFunction.getMeterNumber();
        meterNumber = localIntent.getStringExtra("meterNumber");
        Log.d(LOG,"Meter Number "+ meterNumber);
        Time localTime = new Time(Time.getCurrentTimezone());
        localTime.setToNow();
        TextView txtMeterNumber = (TextView) findViewById(R.id.meter_number);
        TextView txtAmount = (TextView) findViewById(R.id.amount);
        TextView txtInitial = (TextView) findViewById(R.id.initial);
        TextView txtSurname = (TextView) findViewById(R.id.surname);
        TextView txtCardNumber = (TextView) findViewById(R.id.card_number);
       // TextView txtCardType = (TextView) findViewById(R.id.card_type);
      //  TextView txtCvv = (TextView) findViewById(R.id.cvv);
        TextView txtDate = (TextView)findViewById(R.id.date);
        TextView txtTime = (TextView)findViewById(R.id.time);
        Button btnConfirm = (Button)findViewById(R.id.btn_confirm);
        if (localIntent.getBooleanExtra("isNew", false))
        {
            tokenAmnt = Integer.parseInt(this.payment.amount) - 4;
            txtMeterNumber.setText(meterNumber);
            txtAmount.setText("P" + this.payment.amount);
            txtInitial.setText(this.payment.cardHolderName);
            txtSurname.setText(this.payment.cardHolderSurname);
            txtCardNumber.setText("*************"+this.payment.cardNumber.substring(13));
            last3Digits = txtCardNumber.getText().toString();
           // txtCardType.setText(this.payment.cardType);
          //  txtCvv.setText(this.payment.cvv);

            try {
                aes = new AES();
                iv = aes.generateRandomIV(16);
                shaKey = aes.SHA256(iv,32);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("IV" ,iv);
            Log.d("KEY",shaKey);
            name = payment.cardHolderName;
            surname = payment.cardHolderSurname;
            cardNumber = payment.cardNumber;
            expMonth = payment.expMonth;
            expYear = payment.expYear;
            amount = payment.amount;
            cvv = payment.cvv;
        }else {
            Cursor localCursor = this.mysqliteFunction.getPaymentHistory();

            if ((localCursor != null) && (localCursor.moveToFirst()))
            {
                last3Digits = localCursor.getString(7);
                txtInitial.setText(localCursor.getString(2));
                txtSurname.setText(localCursor.getString(3));
                txtCardNumber.setText("*************"+last3Digits);
                //txtCvv.setText(localCursor.getString(6));
               // txtCardType.setText(localCursor.getString(7));
               // txtMeterNumber.setText(this.mysqliteFunction.getMeterNumber());

                if(localCursor.getString(1).length() == 16){
                    try {
                        aes = new AES();
                        iv = aes.generateRandomIV(16);
                        shaKey = aes.SHA256(iv,32);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                name = localCursor.getString(2);
                surname = localCursor.getString(3);
                cardNumber=localCursor.getString(1);
                expMonth =localCursor.getString(4);
                expYear = localCursor.getString(5);
                cvv = localCursor.getString(6);
                amount = localIntent.getStringExtra("amount");
                Log.e("cn",cardNumber);

            }
            tokenAmnt = Integer.parseInt(localIntent.getStringExtra("amount")) - 4;
            txtAmount.setText(localIntent.getStringExtra("amount"));
            txtMeterNumber.setText(localIntent.getStringExtra("meter_number"));
            this.mysqliteFunction.close();
        }

        int curMonth = localTime.month + 1;
        date =  localTime.year+ "-" + curMonth + "-" + localTime.monthDay;
        time = localTime.format("%k:%M:%S");
        txtDate.setText(date);
        txtTime.setText(time);
        ((ImageButton)findViewById(R.id.bck_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ConfirmPurchaseActivity.this.onBackPressed();
            }
        });
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(isOnline()){
                     new  GetTokenTask().execute();
                    }else {
                        dialogMsg ="You are offline Please check your internet settings";
                        new ErrorMsgDialog().show(getFragmentManager(),null);
                    }

                }
            });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    private class GetTokenTask extends AsyncTask<Void, Void, String> {
        private String resp;
        private ProgressDialog progressDialog;
        JSONArray jAr = null;

        @Override
        protected String doInBackground(Void... params) {
            String host = "http://utc.empirestate.co.za:8080";
            Request request = new Request();

            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(host)
                        .openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    publishProgress();

                    if (localIntent.getBooleanExtra("isNew", false) || cardNumber.length() == 16)
                    {
                        resp = request.addParamsToReq(iv,shaKey,mysqliteFunction.getPhone(),mysqliteFunction.getMeterNumber());
                        if(resp != null && resp != ""){
                            if(resp.equals("successful")){
                                try {
                                    cardNumber = aes.encrypt(cardNumber,shaKey,iv);
                                    cvv = aes.encrypt(cvv,shaKey,iv);
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (InvalidAlgorithmParameterException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                }
                                mysqliteFunction.deletePayment();

                                mysqliteFunction.createPaymentTable(cardNumber, name, surname, cvv, expMonth, expYear,last3Digits);
                                Log.d(LOG,"Last 3 digits stored "+ last3Digits);
                                resp = request.addPaymentParamaters(name,surname,cardNumber,cvv,expMonth,expYear,amount,phone,meterNumber,Integer.toString(tokenAmnt),date,time);

                            }else{
                                resp = "connection error";
                            }
                        }
                    }else {
                        resp = request.addPaymentParamaters(name,surname,cardNumber,cvv,expMonth,expYear,amount,phone,meterNumber,Integer.toString(tokenAmnt),date,time);
                    }



                    if(resp == null ){
                        resp = "A network error occurred while processing you payment,A token would be sent to you via notification or sms";
                    }else{
                        try {
                            jAr = new JSONArray(resp);
                            JSONObject serverObj = jAr.getJSONObject(0);
                            resp = serverObj.getString("message");
                            reference = serverObj.getString("ref");
                            token = serverObj.getString("token");
                            units = serverObj.getString("unit");
                            vatAndLevy = serverObj.getString("vat_and_levy");
                            costOfUnits = serverObj.getString("cost_of_units");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            resp = "A network error occurred while processing you payment,A token would be sent to you via notification or sms";
                            return resp;
                        }
                    }

                }else {
                    resp = "connection error";
                }
            }catch (IOException e) {

            }

            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            resp = this.resp;
            progressDialog.dismiss();
           // String regex = "[0-9]+"; //numeric regular expression
            Intent i = new Intent(ConfirmPurchaseActivity.this, SuccessfullActivity.class);
            Intent i2 = new Intent(ConfirmPurchaseActivity.this, UnsuccessfullActivity.class);
            Intent i3 = new Intent(ConfirmPurchaseActivity.this, PendingActivity.class);

            if(resp == null || resp == ""){
                i3.putExtra("error_msg","Transaction is in progress. A token will be sent to you via notification or sms once the payment has been approved.");
                i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i3);
                finish();
            }else {

                if(resp.equalsIgnoreCase("connection error")){
                    //empire state server might be down or user do not have inter credit
                    dialogMsg = "Please check your internet connection and try again";
                    new ErrorMsgDialog().show(getFragmentManager(),null);

                }else if(resp.equals("A network error occurred while processing you payment,A token would be sent to you via notification or sms")){

                    i3.putExtra("error_msg","Transaction is in progress. A token will be sent to you via notification or sms once the payment has been approved.");
                    i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i3);
                    finish();
                    //we have a response from utility server with the token
                }else if (resp.equals("successful")) {

                    mysqliteFunction.createHistoryTable(reference,amount,token,meterNumber,units, date, time);

                    i.putExtra("amount", amount);
                    i.putExtra("token",token);
                    i.putExtra("units",units);
                    i.putExtra("reference",reference);
                    i.putExtra("vat_and_levy",vatAndLevy);
                    i.putExtra("cost_of_units",costOfUnits);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }else {
                    mysqliteFunction.createHistoryTable(reference,amount,token,meterNumber,units,date,time);

                    i2.putExtra("error_msg",resp);
                    i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i2);
                    finish();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfirmPurchaseActivity.this);
            progressDialog.setMessage("Connecting...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {

            new android.os.Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    progressDialog.setMessage("Processing payment");
                }
            }
                    , 10000);

            new android.os.Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    progressDialog.setMessage("Generating token");
                }
            }
                    , 20000);

        }
    }

    public static class ErrorMsgDialog extends DialogFragment
    {
        public Dialog onCreateDialog(Bundle paramBundle)
        {
            AlertDialog.Builder locaBuilder = new AlertDialog.Builder(getActivity());
            locaBuilder.setMessage(dialogMsg).setCancelable(false).setTitle(getResources().getString(R.string.app_name)).setPositiveButton("ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                {
                }
            });
            return locaBuilder.create();
        }
    }
}