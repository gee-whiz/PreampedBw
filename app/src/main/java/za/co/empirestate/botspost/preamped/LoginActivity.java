package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import za.co.empirestate.botspost.json.JSONArrayFunctions;
import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class LoginActivity extends Activity
{
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String LOG = "Hey Gee" ;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static String dialogMsg;
    String SENDER_ID = "1084226562155";
    GoogleCloudMessaging gcm;
  private String meterNumber, phone;
  private MySQLiteFunctions mysqliteFunction;
  private String password;
  private EditText txtMeterNumber,txtPhone;
  private EditText txtPassword;
  private  String regid="";

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected();
    }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_login);

      Button btnLogin = (Button) findViewById(R.id.btn_login);
     //txtMeterNumber = (EditText) findViewById(R.id.meter_number);
      txtPassword = (EditText) findViewById(R.id.password);
      txtPhone = (EditText) findViewById(R.id.phone);
    this.mysqliteFunction = new MySQLiteFunctions(this);
    //this.txtMeterNumber.setText(this.mysqliteFunction.getMeterNumber());
      String tmp = this.mysqliteFunction.getPhone();
      Log.d(LOG,"temp "+tmp);
    meterNumber = "";
      if (!tmp.isEmpty()) {
          txtPhone.setText(this.mysqliteFunction.getPhone().substring(4));
      }
      regid = mysqliteFunction.getDeviceId();
      //txtMeterNumber.setEnabled(true);
      txtPhone.setEnabled(true);
      if(meterNumber.length() <8){
          //txtMeterNumber.setEnabled(true);
          txtPhone.setEnabled(true);
      }else{
          txtPhone.setText(this.mysqliteFunction.getPhone().substring(4));
      }
      findViewById(R.id.txt_forgot_pass).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
              startActivity(i);
              finish();
          }
      });
    findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            LoginActivity.this.onBackPressed();
        }
    });
      btnLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              password = txtPassword.getText().toString();
              //meterNumber = txtMeterNumber.getText().toString();
               phone = txtPhone.getText().toString();

              if ( password.isEmpty() || phone.isEmpty()) {


                  if (password.isEmpty()) {
                      txtPassword.setError("password cannot be empty");
                  }

                  if (phone.isEmpty()) {
                      txtPhone.setError("phone cannot be empty");
                  }
              } else if (password.length() < 6 || !((phone.startsWith("7")) && phone.length() == 8)) {
                  if (password.length() < 6) {
                      txtPassword
                              .setError("password must be at least 6 character long");
                  }



                  if(!((phone.startsWith("7")) && phone.length() == 8))
                      txtPhone.setError("incorrect phone numbers");

              } else {
                  if (isOnline()) {
                      if (meterNumber.length() <8){
                          new LoginTask().execute("", password, phone, regid);
                      }
                      else {
                          new LoginTask().execute(meterNumber, password, phone, regid);

                      }
                  } else {
                      dialogMsg = "You are offline Please check your internet settings";
                      new ErrorMsgDialog().show(getFragmentManager(), null);
                  }
              }
          }
      });
  }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                dialogMsg = "You must download the Google Play Services APK or Activate it in your settings";
                new MsgDialog().show(getFragmentManager(), null);
                // Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
                //  dialog.show();
                finish();
            }
            return false;
        }
        return true;
    }

  public static class ErrorMsgDialog extends DialogFragment
  {
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      AlertDialog.Builder locaBuilder = new AlertDialog.Builder(getActivity());
      locaBuilder.setMessage(LoginActivity.dialogMsg).setCancelable(false).setTitle(getResources().getString(R.string.app_name)).setPositiveButton("ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
        }
      });
      return locaBuilder.create();
    }
  }

    public static class MsgDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You must download the Google Play Services APK or Activate it in your settings")
                    .setCancelable(false)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    // TODO Auto-generated method stub
                                    Intent i = new Intent(getActivity(),
                                            MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                }
                            });
            return builder.create();
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        JSONArray jAr = null;
        private String resp; //response from server
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
            }
            // regid = getRegistrationId(context);

            if(regid.isEmpty()){
                try {
                    regid = gcm.register(SENDER_ID);
                }catch (IOException e) {

                }

                mysqliteFunction.createDeviceIdTable(regid);
                regid = mysqliteFunction.getDeviceId();
            }

                    JSONArrayFunctions jsonArrayFunctions = new JSONArrayFunctions();
                        resp = jsonArrayFunctions.logUser(params[0],params[1],params[2],params[3]);

            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            resp = this.resp;
            super.onPostExecute(resp);
            progressDialog.dismiss();
            if(resp.equalsIgnoreCase("can't establish connection to our server") || resp.equalsIgnoreCase("can't establish connection to the server") ||
                    resp.equalsIgnoreCase("No response From the server")){

                dialogMsg = "Cannot connect to the server please try again later";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }else{

                try {
                    jAr = new JSONArray(resp);
                    JSONObject serverObj = jAr.getJSONObject(0);
                    String serverMessge = serverObj.getString("message");
                    if (serverMessge.equalsIgnoreCase("succeeded")) {
                        String email = serverObj.getString("email");
                        String phone = serverObj.getString("phone");
                        File dbTest =new File("/data/data/za.co.empirestate.botspost.preamped/databases/preamped.db");
                        if(meterNumber != null){
                            mysqliteFunction.createCurrentUserTable(meterNumber ,null,null);
                            Log.d(LOG, "User already exists in local storage");
                        }
                        else
                        {
                            mysqliteFunction.createCurrentUserTable(meterNumber,null,null);
                        }
                        if(dbTest.exists()){
                            //mysqliteFunction.deleteUser();
                            mysqliteFunction.createUserTable(meterNumber,email,phone);
                            Log.d(LOG, "User already exists in local storage");
                        }else {
                           // mysqliteFunction.createCurrentUserTable(meterNumber,email,phone);
                            Log.d(LOG, "the else part");
                        }
                        Intent i = new Intent(LoginActivity.this,
                                MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        //overridePendingTransition(R.anim.from, R.anim.to);
                    } else if (serverMessge.equalsIgnoreCase("Incorrect Password")
                            || serverMessge
                            .equalsIgnoreCase("Incorrect phone or password")) {
                            dialogMsg = "Incorrect login details";
                        new ErrorMsgDialog().show(getFragmentManager(), null);

                    } else {
                        Toast.makeText(LoginActivity.this,"NetWork Error Please Try again later",Toast.LENGTH_LONG).show();
                        new ErrorMsgDialog().show(getFragmentManager(), null);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                    Toast.makeText(LoginActivity.this,"NetWork Error Check Your internet settings Try again",Toast.LENGTH_LONG).show();
                    new ErrorMsgDialog().show(getFragmentManager(), null);
                }

            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Processing please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }
}