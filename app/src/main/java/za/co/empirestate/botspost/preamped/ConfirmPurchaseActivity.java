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
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    private static final String TAG = "hey Gee";
    static String iv,shaKey;
    private static String dialogMsg;
    double tokenAmnt;
    AES aes;
    Intent localIntent;
    String meterChar;
    PoboxObj poboxObj;
    String poBoxId,groupId,NewPhone;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String amount;
    private String name;
    private String cardNumber;
    private String meterNumber;
    private String cvv;
    private String expMonth,expYear;
    private String surname;
    private MySQLiteFunctions mysqliteFunction;
    private Payment payment;
    private String phone,encriptedCard,encriptedCVV;
    private String token,units,date,time,voucherValue;
    private String reference = null;
    private String last3Digits;
    private String vatAndLevy;
    private  String PaidUntil;
    private String costOfUnits;
    private String email,ls_transactionFee;
    private TextView tmeter;
    private ProgressDialog pDialog;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_confirm_purchase);
        this.mysqliteFunction = new MySQLiteFunctions(this);
        localIntent = getIntent();
        this.payment = localIntent.getParcelableExtra("payment_details");
        email =   mysqliteFunction.getEmail();
        pDialog = new ProgressDialog(ConfirmPurchaseActivity.this);
        pDialog.setMessage("Processing Payment..");
        phone = mysqliteFunction.getPhone();
        //meterNumber = mysqliteFunction.getMeterNumber();
        meterNumber = localIntent.getStringExtra("meter_number");
        groupId = localIntent.getStringExtra("groupId");
        voucherValue = localIntent.getStringExtra("voucherValue");
        Log.e(LOG,"Group id "+groupId);
        Log.d(LOG, "Meter Number " + meterNumber);
        Time localTime = new Time(Time.getCurrentTimezone());
        localTime.setToNow();
        TextView txtMeterNumber = (TextView) findViewById(R.id.meter_number);
        TextView txtAmount = (TextView) findViewById(R.id.amount);
        TextView txtInitial = (TextView) findViewById(R.id.initial);
        TextView txtSurname = (TextView) findViewById(R.id.surname);
        TextView txtCardNumber = (TextView) findViewById(R.id.card_number);
        tmeter = (TextView)findViewById(R.id.txtMeter);
        // TextView txtCardType = (TextView) findViewById(R.id.card_type);
        //  TextView txtCvv = (TextView) findViewById(R.id.cvv);
        TextView txtDate = (TextView)findViewById(R.id.date);
        TextView txtTime = (TextView)findViewById(R.id.time);
        Button btnConfirm = (Button)findViewById(R.id.btn_confirm);
        NewPhone = phone.substring(1);
        meterChar = String.valueOf(meterNumber.charAt(0));
        if (meterNumber.length() > 9) {
            tmeter.setText("Meter Number");
        }
        else {

            if (meterChar.equalsIgnoreCase("B")||meterChar.equalsIgnoreCase("O")||meterChar.equalsIgnoreCase("M")) {
                tmeter.setText("Mobile Operator");
                ls_transactionFee = localIntent.getStringExtra("transactionFee");
                Log.e(LOG,"transaction fee"+ls_transactionFee);
            }
            else
                tmeter.setText("PoBox ID");
            PaidUntil=  localIntent.getStringExtra("PaidUntil");
            ls_transactionFee = localIntent.getStringExtra("transactionFee");
            Log.e(LOG,"george "+PaidUntil + ls_transactionFee);
        }
        if (localIntent.getBooleanExtra("isNew", false))
        {
            tokenAmnt = Double.parseDouble(this.payment.amount) - 4;
            txtMeterNumber.setText(meterNumber);
            double tempAmount = Double.parseDouble(this.payment.amount);
            txtAmount.setText("P"+String.format(Locale.ENGLISH, "%.2f", tempAmount) );
            txtInitial.setText(this.payment.cardHolderName);
            txtSurname.setText(this.payment.cardHolderSurname);
            txtCardNumber.setText("*************"+this.payment.cardNumber.substring(13));
            last3Digits = txtCardNumber.getText().toString();
            // txtCardType.setText(this.payment.cardType);
            //  txtCvv.setText(this.payment.cvv);

            try {
                aes = new AES();
                iv = AES.generateRandomIV(16);
                shaKey = AES.SHA256(iv, 32);
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
                        iv = AES.generateRandomIV(16);
                        shaKey = AES.SHA256(iv, 32);
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
            tokenAmnt = Double.parseDouble(localIntent.getStringExtra("amount")) - 4;
            double tempAmount = Double.parseDouble(localIntent.getStringExtra("amount"));
            txtAmount.setText("P"+String.format(Locale.ENGLISH, "%.2f", tempAmount) );
            txtMeterNumber.setText(localIntent.getStringExtra("meter_number"));
            this.mysqliteFunction.close();
        }

        int curMonth = localTime.month + 1;
        date =  localTime.year+ "-" + curMonth + "-" + localTime.monthDay;
        time = localTime.format("%k:%M:%S");
        txtDate.setText(date);
        txtTime.setText(time);
        findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ConfirmPurchaseActivity.this.onBackPressed();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(isOnline()){

                    if (meterNumber.length() < 9)  {

                        if (meterChar.equalsIgnoreCase("B")||meterChar.equalsIgnoreCase("O")||meterChar.equalsIgnoreCase("M")) {
                            ls_transactionFee = localIntent.getStringExtra("transactionFee");
                            Log.e(LOG,"transaction fee"+ls_transactionFee);
                            pDialog.show();
                            try {
                                aes = new AES();
                                iv = AES.generateRandomIV(16);
                                shaKey = AES.SHA256(iv, 32);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Log.d("IV" ,iv);
                            Log.d("KEY", shaKey);
                            AddAirtimeRequest();
                        }

                        else {
                            pDialog.show();
                            try {
                                aes = new AES();
                                iv = AES.generateRandomIV(16);
                                shaKey = AES.SHA256(iv, 32);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Log.d("IV" ,iv);
                            Log.d("KEY", shaKey);
                            AddPoRequest();
                        }

                    }
                    else {
                        new GetTokenTask().execute();
                    }
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

        return netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }

    public  void PurchaseAirtime(final String productName,final String value,final
    String email,final String phone, final  String name, final String card ,final String cvv,
                                 final String amount, final  String expYear, final String expMonth,final  String surname){

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_AIRTIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "vend Airtime  response " + response.toString());
                int size = response.length();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String SerialNumber = jsonObject.getString("SerialNumber");
                    String productName  = jsonObject.getString("ProductName");
                    String activationNumber = jsonObject.getString("ActivationNumber");
                    String value = jsonObject.getString("Value");
                    String expDate = jsonObject.getString("ExpirationDate");
                    String res =  jsonObject.getString("Response");
                    Log.d(LOG,"SerialNumber "+ SerialNumber);

                    if (res.equalsIgnoreCase("00")){
                        pDialog.dismiss();
                        Intent i = new Intent(ConfirmPurchaseActivity.this,AirtimeSuccessActivity.class);
                        i.putExtra("Amount", amount);
                        i.putExtra("ProductName",productName);
                        i.putExtra("ActivationNumber",activationNumber);
                        i.putExtra("Value",value);
                        i.putExtra("expDate",expDate);
                        i.putExtra("SerialNumber",SerialNumber);
                        i.putExtra("transactionFee",ls_transactionFee);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                    else if (res.equalsIgnoreCase("99")){
                        pDialog.dismiss();
                        ShowError();
                    }
                    else  if(res.equalsIgnoreCase("98")) {
                        CardError(SerialNumber);
                    }

                    else
                    {
                        pDialog.dismiss();
                        NetworkError();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("productName", productName);
                params.put("value",amount);
                params.put("voucherValue",voucherValue);
                params.put("quantity","1");
                params.put("email",email);
                params.put("phone",phone);
                params.put("platform","Android");
                params.put("name",name + " "+surname);
                params.put("card", URLEncoder.encode(card));
                params.put("cvv",URLEncoder.encode(cvv));
                params.put("amount",amount);
                params.put("expYear",expYear);
                params.put("expMonth",expMonth);
                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);


    }

    private  void ShowError()
    {

        LayoutInflater inflater = LayoutInflater.from(ConfirmPurchaseActivity.this);
        View promptView = inflater.inflate(R.layout.purchase_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPurchaseActivity.this);
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





    public  void  AddAirtimeRequest(){
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_IV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "requset for payment  response " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Postoffices postoffices = new Postoffices();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                    } catch (JSONException e) {
                    }
                }


                if (response.equals("successful")){
                    DecimalFormat decimalFormat= new DecimalFormat("###.#");
                    Double la_amount = Double.parseDouble(amount) * 100;
                    String li_amount = decimalFormat.format(la_amount);

                    Log.d(LOG, "Amount now " + li_amount);
                    if (localIntent.getBooleanExtra("isNew", false) || cardNumber.length() == 16) {

                        try {
                            encriptedCard = aes.encrypt(cardNumber, shaKey, iv);
                            encriptedCVV = aes.encrypt(cvv, shaKey, iv);
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
                        //mysqliteFunction.deletePayment();

                        mysqliteFunction.createPaymentTable(cardNumber, name, surname, cvv, expMonth, expYear, last3Digits);
                        Log.d(LOG, "Last 3 digits stored " + last3Digits);
                        PurchaseAirtime(meterNumber, li_amount, email, NewPhone, name, encriptedCard, encriptedCVV, li_amount, expYear, expMonth, surname);
                    } else {

                        try {
                            encriptedCard = aes.encrypt(cardNumber, shaKey, iv);
                            encriptedCVV = aes.encrypt(cvv, shaKey, iv);
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
                        //mysqliteFunction.deletePayment();

                        mysqliteFunction.updateCardDetails(cardNumber, name, surname, cvv, expMonth, expYear, last3Digits);
                        Log.d(LOG, "Last 3 digits stored " + last3Digits);
                        PurchaseAirtime(meterNumber, li_amount, email, NewPhone, name, encriptedCard, encriptedCVV, li_amount, expYear, expMonth, surname);
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
                params.put("iv", iv);
                params.put("key",shaKey);
                params.put("voucher",meterNumber);
                params.put("phone",phone);

                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }


    public  void  AddPoRequest(){
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_IV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "requset for payment  response " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Postoffices postoffices = new Postoffices();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                    } catch (JSONException e) {
                    }
                }


                if (response.equals("successful")){

                    if (localIntent.getBooleanExtra("isNew", false) || cardNumber.length() == 16) {

                        try {
                            encriptedCard = aes.encrypt(cardNumber, shaKey, iv);
                            encriptedCVV = aes.encrypt(cvv, shaKey, iv);
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
                        //mysqliteFunction.deletePayment();

                        mysqliteFunction.createPaymentTable(cardNumber, name, surname, cvv, expMonth, expYear, last3Digits);
                        Log.d(LOG, "Last 3 digits stored " + last3Digits);
                        RenewPoBox(amount,meterNumber,groupId,email,NewPhone,name,encriptedCard,encriptedCVV,expYear,expMonth,surname);

                    } else {
                        try {
                            encriptedCard = aes.encrypt(cardNumber, shaKey, iv);
                            encriptedCVV = aes.encrypt(cvv, shaKey, iv);
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
                        //mysqliteFunction.deletePayment();

                        mysqliteFunction.updateCardDetails(cardNumber, name, surname, cvv, expMonth, expYear, last3Digits);
                        Log.d(LOG, "Last 3 digits stored " + last3Digits);
                        RenewPoBox(amount,meterNumber,groupId,email,NewPhone,name,encriptedCard,encriptedCVV,expYear,expMonth,surname);
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
                params.put("iv", iv);
                params.put("key",shaKey);
                params.put("postBoxId",meterNumber);
                params.put("phone",phone);

                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
    //vend airtime

    private  void NetworkError()
    {

        LayoutInflater inflater = LayoutInflater.from(ConfirmPurchaseActivity.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPurchaseActivity.this);
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

    private  void CardError(String message)
    {

        LayoutInflater inflater = LayoutInflater.from(ConfirmPurchaseActivity.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPurchaseActivity.this);
        builder.setView(promptView);
        TextView error = (TextView)promptView.findViewById(R.id.txtOr);
        error.setText(message);
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
                pDialog.dismiss();

            }
        });


    }




    public  void RenewPoBox(final  String amount,final String poboBoxId,final  String groupId, final String email,final String phone, final  String name, final String card ,final String cvv,
                            final  String expYear, final String expMonth,final  String surname){
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_RENEWPOBOX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "vend Pobox  response " + response.toString());
                int size = response.length();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("Response");
                    String PaymentReference = jsonObject.getString("PaymentReference");
                    if(res.equalsIgnoreCase("00")){

                        pDialog.dismiss();
                        Intent intent = new Intent(ConfirmPurchaseActivity.this,PoBoxSuccessfull.class);
                        intent.putExtra("PaymentReference",PaymentReference);
                        intent.putExtra("Amount",amount);
                        intent.putExtra("transactionFee","P"+ls_transactionFee);
                        Log.e(LOG, "transaction fee" + ls_transactionFee);
                        intent.putExtra("PaidUntil",PaidUntil);

                        startActivity(intent);
                    }

                    else  if(res.equalsIgnoreCase("98")) {
                        CardError(PaymentReference);
                    }

                    else
                    {
                        pDialog.dismiss();
                        NetworkError();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("function","RenewPostBox");
                params.put("groupId",groupId);
                params.put("postBoxId",poboBoxId);
                params.put("email",email);
                params.put("phone",phone);
                params.put("platform","Android");
                params.put("card", URLEncoder.encode(card));
                params.put("cvv",URLEncoder.encode(cvv));
                params.put("amount",amount);
                params.put("name",name + " "+surname);
                params.put("expYear",expYear);
                params.put("expMonth",expMonth);

                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);





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

    private class GetTokenTask extends AsyncTask<Void, Void, String> {
        JSONArray jAr = null;
        private String resp;
        private ProgressDialog progressDialog;

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

                    if (localIntent.getBooleanExtra("isNew", false) || cardNumber.length() == 16) {
                        resp = request.addParamsToReq(iv, shaKey, mysqliteFunction.getPhone(), mysqliteFunction.getMeterNumber());

                        if (resp != null && resp != "") {
                            if (resp.equals("successful")) {
                                try {
                                    encriptedCard = aes.encrypt(cardNumber, shaKey, iv);
                                    encriptedCVV = aes.encrypt(cvv, shaKey, iv);
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

                                mysqliteFunction.createPaymentTable(cardNumber, name, surname, cvv, expMonth, expYear, last3Digits);
                                Log.d(LOG, "Last 3 digits stored " + last3Digits);
                                resp = request.addPaymentParamaters(name, surname, encriptedCard, encriptedCVV, expMonth, expYear, amount, phone, meterNumber, Double.toString(tokenAmnt), date, time);
                                Log.d(LOG, "first Payment parameters sent to the server " + meterNumber);


                            } else {
                                resp = "connection error";
                            }
                        }
                    } else {
                        resp = request.addParamsToReq(iv, shaKey, mysqliteFunction.getPhone(), mysqliteFunction.getMeterNumber());

                        if (resp != null && resp != "") {
                            if (resp.equals("successful")) {
                                try {
                                    encriptedCard = aes.encrypt(cardNumber, shaKey, iv);
                                    encriptedCVV = aes.encrypt(cvv, shaKey, iv);
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

                                mysqliteFunction.updateCardDetails(cardNumber, name, surname, cvv, expMonth, expYear, last3Digits);
                                Log.d(LOG, "Last 3 digits stored " + last3Digits);
                                resp = request.addPaymentParamaters(name, surname, encriptedCard, encriptedCVV, expMonth, expYear, amount, phone, meterNumber, Double.toString(tokenAmnt), date, time);
                                Log.d(LOG, "second Payment parameters sent to the server " + meterNumber);


                            } else {
                                resp = "connection error";
                            }
                        }
                    }


                    if (resp == null) {
                        resp = "A network error occurred while processing you payment,A token would be sent to you via notification or sms";
                    } else {
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

                } else {
                    resp = "connection error";
                }
            } catch (IOException e) {

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

            if (resp == null || resp == "") {
                i3.putExtra("error_msg", "Transaction is in progress. A token will be sent to you via notification or sms once the payment has been approved.");
                i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i3);
                finish();
            } else {

                if (resp.equalsIgnoreCase("connection error")) {
                    //empire state server might be down or user do not have inter credit
                    dialogMsg = "Please check your internet connection and try again";
                    new ErrorMsgDialog().show(getFragmentManager(), null);

                } else if (resp.equals("A network error occurred while processing you payment,A token would be sent to you via notification or sms")) {

                    i3.putExtra("error_msg", "Transaction is in progress. A token will be sent to you via notification or sms once the payment has been approved.");
                    i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i3);
                    finish();
                    //we have a response from utility server with the token
                } else if (resp.equals("successful")) {

                    mysqliteFunction.createHistoryTable(reference, amount, token, meterNumber, units, date, time);

                    i.putExtra("amount", amount);
                    i.putExtra("token", token);
                    i.putExtra("units", units);
                    i.putExtra("reference", reference);
                    i.putExtra("vat_and_levy", vatAndLevy);
                    i.putExtra("cost_of_units", costOfUnits);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {
                    mysqliteFunction.createHistoryTable(reference, amount, token, meterNumber, units, date, time);

                    i2.putExtra("error_msg", resp);
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

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    progressDialog.setMessage("Processing payment");
                }
            }
                    , 10000);

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    progressDialog.setMessage("Generating token");
                }
            }
                    , 20000);

        }


    }


}