package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

public class WaterPurchaseConfirm extends Activity {
    private static final String LOG = "Hey Gee" ;
    private static final String TAG = "hey Gee";
    static String iv,shaKey;
    private static String dialogMsg;
    String ls_customerNumber,ls_contractNumber,ls_amount,ls_accountHolder;
    Context ctx;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_purchase_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        localIntent = getIntent();
        ctx   = getApplicationContext();

        ls_accountHolder = localIntent.getStringExtra("accountHolder");
        ls_contractNumber = localIntent.getStringExtra("contractNumber");
        ls_customerNumber = localIntent.getStringExtra("customerNumber");
        ls_amount   = localIntent.getStringExtra("amount");
        this.mysqliteFunction = new MySQLiteFunctions(this);
        this.payment = localIntent.getParcelableExtra("payment_details");
        email =   mysqliteFunction.getEmail();
        pDialog = new ProgressDialog(WaterPurchaseConfirm.this);
        pDialog.setMessage("Processing Payment..");
        phone = mysqliteFunction.getPhone();

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



        if (localIntent.getBooleanExtra("isNew", false))
        {

            txtMeterNumber.setText(ls_accountHolder);
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
            Log.d("IV", iv);
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
            txtMeterNumber.setText(localIntent.getStringExtra("accountHolder"));
            this.mysqliteFunction.close();
        }


        int curMonth = localTime.month + 1;
        date =  localTime.year+ "-" + curMonth + "-" + localTime.monthDay;
        time = localTime.format("%k:%M:%S");
        txtDate.setText(date);
        txtTime.setText(time);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOnline()) {



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
                        Log.d("IV", iv);
                        Log.d("KEY", shaKey);
                        //AddWaterRequest();

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
                    AddWaterRequest();

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


    public  void  AddWaterRequest(){
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
                        PurchaseWater(ls_accountHolder, ls_customerNumber, ls_contractNumber, ls_amount, email, NewPhone, name, encriptedCard, encriptedCVV, expYear, expMonth, surname);
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
                       PurchaseWater(ls_accountHolder, ls_customerNumber, ls_contractNumber, ls_amount, email, NewPhone, name, encriptedCard, encriptedCVV, expYear, expMonth, surname);
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
                params.put("voucher",ls_customerNumber);
                params.put("phone",phone);

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

        LayoutInflater inflater = LayoutInflater.from(WaterPurchaseConfirm.this);
        View promptView = inflater.inflate(R.layout.purchase_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(WaterPurchaseConfirm.this);
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





    public  void  PurchaseWater(final String accountHolder,final String customerNumber,final  String contractNumber,final String amount,final
    String email, final String phone, final  String name, final String card ,final String cvv,
                                  final  String expYear, final String expMonth,final  String surname){
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_Water, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "water response " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String  res = jsonObject.getString("SerialNumber");
                       String  rss = jsonObject.getString("Response");
                 if (rss.equalsIgnoreCase("00")){
                     String accountHolder = jsonObject.getString("accountHolder");
                     String customerNumber = jsonObject.getString("customerNumber");
                     String contractNumber = jsonObject.getString("contractNumber");
                     String  amount  = jsonObject.getString("amount");
                     String  paymentReference = jsonObject.getString("paymentreference");
                     Intent intent = new Intent(ctx,WaterSuccess.class);
                     intent.putExtra("customerNumber", customerNumber);
                     intent.putExtra("contractNumber",contractNumber);
                     intent.putExtra("accountHolder",accountHolder);
                     intent.putExtra("amount", amount);
                     intent.putExtra("paymentReference",paymentReference);
                     startActivity(intent);
                     finish();
                 }

               else  if (rss.equalsIgnoreCase("98")) {

                        CardError(res);

                  }
                       else if (rss.equalsIgnoreCase("99")) {

                          CardError(res);

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
                params.put("accountHolder", accountHolder);
                params.put("value",amount);
                params.put("customerNumber",customerNumber);
                params.put("contractNumber",contractNumber);
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
                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }




    private  void NetworkError()
    {

        LayoutInflater inflater = LayoutInflater.from(WaterPurchaseConfirm.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(WaterPurchaseConfirm.this);
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

        LayoutInflater inflater = LayoutInflater.from(WaterPurchaseConfirm.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(WaterPurchaseConfirm.this);
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




}
