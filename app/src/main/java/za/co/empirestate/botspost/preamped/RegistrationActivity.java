package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import za.co.empirestate.botspost.json.JSONArrayFunctions;
import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class RegistrationActivity extends Activity
{
  public static final String EXTRA_MESSAGE = "message";
  public static final String PROPERTY_REG_ID = "registration_id";
    private static final String LOG = "hey Gee" ;
  private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
  private static final String PROPERTY_APP_VERSION = "appVersion";
    private static String dialogMsg;
  String SENDER_ID = "1084226562155";
  GoogleCloudMessaging gcm;
  Context context;
  String regid="";
  private String confirmPass;
  private String emailAddress;
  private String meterNumber;
  private String name,android_id;
  private MySQLiteFunctions mysqliteFunction;
  private String password,utilityResponse ;
  private String phone;
  private Button btnReg;

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

    /**
     * this function will validate the email entered by the user
     *
     * @param email
     *
     * @return -1 if the email is invalid and 1 id the email is valid
     */
    private boolean validateEmail(String email) {

        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern p = Pattern.compile(email_pattern);
        Matcher m = p.matcher(email);

        return m.matches();

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
      setContentView(R.layout.activity_registration);
    btnReg = (Button) findViewById(R.id.register_btn);
      final EditText txtPhoneNumber = (EditText) findViewById(R.id.phone);
      final EditText txtMeterNumber = (EditText) findViewById(R.id.meter_number);
      final EditText txtEmailAddress = (EditText) findViewById(R.id.email_address);
    final EditText txtPassword = (EditText) findViewById(R.id.password);
    final EditText txtConfirmPass = (EditText) findViewById(R.id.confirm_pass);
      final EditText txtName = (EditText) findViewById(R.id.name);
      final TextView txtTerms = (TextView) findViewById(R.id.txt_terms);
      final CheckBox termsAndConChk = (CheckBox) findViewById(R.id.news_chk_box);
    this.mysqliteFunction = new MySQLiteFunctions(this);
      context = this;
      android_id = Settings.Secure.getString(context.getContentResolver(),
              Settings.Secure.ANDROID_ID);
       regid = mysqliteFunction.getDeviceId();
       Log.e(LOG,"gcm device id "+regid);


    findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            RegistrationActivity.this.onBackPressed();
        }
    });

      txtTerms.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View arg0) {
              // TODO Auto-generated method stub
              Intent intent = new Intent(RegistrationActivity.this,
                      TermsAndConditions.class);
              startActivity(intent);
          }
      });

    btnReg.setOnClickListener(new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            RegistrationActivity.this.meterNumber = txtMeterNumber.getText().toString();
            RegistrationActivity.this.emailAddress = txtEmailAddress.getText().toString();
            RegistrationActivity.this.password = txtPassword.getText().toString();
            RegistrationActivity.this.confirmPass = txtConfirmPass.getText().toString();
            name = txtName.getText().toString();
            phone = txtPhoneNumber.getText().toString();

            boolean bool = RegistrationActivity.this.validateEmail(RegistrationActivity.this.emailAddress);
            if ( (RegistrationActivity.this.emailAddress.isEmpty()) || (RegistrationActivity.this.password.isEmpty()) || phone.isEmpty() || name.isEmpty()) {
               // if (RegistrationActivity.this.meterNumber.isEmpty())
                    //txtMeterNumber.setError("field cannot be empty");
                if (RegistrationActivity.this.emailAddress.isEmpty())
                    txtEmailAddress.setError("field cannot be empty");
                if (RegistrationActivity.this.password.isEmpty())
                    txtPassword.setError("field cannot be empty");
                if (RegistrationActivity.this.confirmPass.isEmpty())
                    txtConfirmPass.setError("field cannot be empty");
                if(phone.isEmpty())
                    txtPhoneNumber.setError("field cannot be empty");
                if(name.isEmpty())
                    txtName.setError("field cannot be empty");
                return;
            }

            if (!bool || (RegistrationActivity.this.password.length() < 6)  || !((phone.startsWith("7")) && phone.length() == 8)) {
                if (!bool)
                    txtEmailAddress.setError("Invalid email address");
                if (RegistrationActivity.this.password.length() < 6)
                    txtPassword.setError("Password must be at least 6 character long");

                //if (RegistrationActivity.this.meterNumber.length() != 11)
                   // txtMeterNumber.setError("Meter number must be 11 digits");

                if(!((phone.startsWith("7")) && phone.length() == 8))
                    txtPhoneNumber.setError("Incorrect phone numbers");

                return;
            }


            if (!RegistrationActivity.this.password.equals(RegistrationActivity.this.confirmPass)) {
                txtConfirmPass.setError("Password do not match");
                return;
            }
            if (RegistrationActivity.this.isOnline()) {
                if(termsAndConChk.isChecked()){
                  if (meterNumber.isEmpty()){
                      new RegistrationTask().execute("null",
                              emailAddress, password, phone, name, mysqliteFunction.getDeviceId(), "ANDROID");
                  }
                    else {
                      new RegistrationTask().execute(meterNumber,
                              emailAddress, password, phone, name, mysqliteFunction.getDeviceId(), "ANDROID");
                  }
                }else {
                    RegistrationActivity.dialogMsg = "Please accept the terms and conditions";
                    new RegistrationActivity.ErrorMsgDialog().show(RegistrationActivity.this.getFragmentManager(), null);
                }

            } else {
                RegistrationActivity.dialogMsg = "You are offline Please check your internet settings";
                new RegistrationActivity.ErrorMsgDialog().show(RegistrationActivity.this.getFragmentManager(), null);
            }

        }
    });
  }

    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                dialogMsg = "You must download the Google Play Services APK or Activate it in your settings";
                new MsgDialog().show(getFragmentManager(),null);
               // Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
              //  dialog.show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(RegistrationActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
       // Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
          //  Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
           // Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

  public static class ErrorMsgDialog extends DialogFragment
  {
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      Builder localBuilder = new AlertDialog.Builder(getActivity());
      localBuilder.setMessage(RegistrationActivity.dialogMsg).setCancelable(false).setTitle(getResources().getString(R.string.app_name)).setPositiveButton("ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
        }
      });
      return localBuilder.create();
    }
  }

    public static class MsgDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(dialogMsg)
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

    private class RegistrationTask extends AsyncTask<String, Void, String> {
        JSONArray jAr = null;
        String host = "http://google.com";
        private String resp; //response from server
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

           JSONArrayFunctions jsonArrayFunctions = new JSONArrayFunctions();
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(host)
                        .openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();
                Log.e(LOG,"now in background");
                if (urlc.getResponseCode() == 200) {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                        Log.e(LOG,"gcm "+gcm);
                    }



                  if (meterNumber.isEmpty()){
                      utilityResponse = "success";
                  }
                    else {
                      Request xmlVendRequest = new Request();
                      utilityResponse = xmlVendRequest.verifyMeterNumber(meterNumber);
                  }

                    Log.e(LOG,"utitlity response "+utilityResponse);
                       if (utilityResponse.equals("success") ){
                        resp = jsonArrayFunctions.addNewUser(params[0],
                                params[1], params[2],params[3],params[4],params[5],params[6]);
                            }else{
                           if(utilityResponse != "" || utilityResponse != null){
                               resp = utilityResponse;
                           }else {
                               resp="server error";
                           }
                       }
                }else{
                    Log.e(LOG,"can't establish connection to the server");
                   resp = "can't establish connection to server";
                }
            }catch (IOException e) {

            }
             Log.e(LOG,"response now "+resp);
            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            resp = this.resp;
            progressDialog.dismiss();

            if(resp != null){

                if(resp.equalsIgnoreCase("can't establish connection to server")  ){
                    dialogMsg = "Connection Error Please check your internet settings or try again later";
                    new ErrorMsgDialog().show(getFragmentManager(), null);
                }else if(resp.equalsIgnoreCase("failed to verify")){
                    // TODO Auto-generated catch block
                    dialogMsg = "your meter number is invalid or cannot be verified";
                    new ErrorMsgDialog().show(getFragmentManager(), null);
                }else if(resp.equalsIgnoreCase("server error")){
                    dialogMsg = "server error try again later";
                    new ErrorMsgDialog().show(getFragmentManager(), null);
                }else if(resp.equals("no device id")){
                    dialogMsg = "Error getting your device id please try again";
                    new ErrorMsgDialog().show(getFragmentManager(), null);
                }else {

                    try {
                        jAr = new JSONArray(resp);
                        JSONObject serverObj = jAr.getJSONObject(0);
                        String serverMessge = serverObj.getString("message");
                        if (serverMessge.equalsIgnoreCase("succeeded")) {
                            btnReg.setEnabled(false);
                            dialogMsg = "Thank for Registering with Botswana Post!";
                            File dbTest =new File("/data/data/za.co.empirestate.botspost.preamped/databases/preamped.db");
                            mysqliteFunction.deletePayment();
                            if(dbTest.exists()){
                                boolean isCurrentUser = mysqliteFunction.checkCurrentUser(meterNumber,emailAddress,"+267"+phone);
                                mysqliteFunction.deleteUser();
                                mysqliteFunction.createUserTable(meterNumber,emailAddress,"+267"+phone);
                                if(!isCurrentUser){
                                    mysqliteFunction.clearDatabase();
                                    mysqliteFunction.createUserTable(meterNumber,emailAddress,"+267"+phone);
                                    mysqliteFunction.createCurrentUserTable(meterNumber,emailAddress,"+267"+phone);
                                }
                            }else {


                                mysqliteFunction.createUserTable(meterNumber,emailAddress,"+267"+phone);
                                mysqliteFunction.createCurrentUserTable(meterNumber,emailAddress,"+267"+phone);
                            }
                            new Handler().postDelayed(new Runnable()
                            {
                                public void run()
                                {
                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                                    ,3000);
                            new MsgDialog().show(getFragmentManager(), null);
                        }else if(serverMessge.equalsIgnoreCase("email exists")){
                            dialogMsg = "Email Already Exists";
                            new ErrorMsgDialog().show(getFragmentManager(), null);
                        }else if(serverMessge.equalsIgnoreCase("phone exists")){
                            dialogMsg = "Phone Already Exists";
                            new ErrorMsgDialog().show(getFragmentManager(), null);
                        } else {
                            dialogMsg = "Server Error Please Try again later";
                            new ErrorMsgDialog().show(getFragmentManager(), null);
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        dialogMsg = "Processing error Please Try again later";
                        new ErrorMsgDialog().show(getFragmentManager(), null);
                    }
                }

            }else {
                dialogMsg = "Server000000 Error Please Try again later";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setMessage("Saving Your Details...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }
}