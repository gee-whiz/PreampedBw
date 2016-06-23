package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import za.co.empirestate.botspost.json.JSONArrayFunctions;
import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class SettingsActivity extends Activity
{
    private static String dialogMsg;
  boolean updateMeter = false;
  boolean updateEmail = false;
  boolean updatePhone = false;
  private MySQLiteFunctions mysqliteFunction;
  private String curMeterNumber,curPhone;
  private String meterNumber,emailAddress,phoneNumber;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_settings);
    this.mysqliteFunction = new MySQLiteFunctions(this);
      curMeterNumber = mysqliteFunction.getMeterNumber();

      curPhone = mysqliteFunction.getPhone();
      Log.d("PHONE",curPhone);
    ImageButton bckBtn = (ImageButton)findViewById(R.id.bck_btn);
    Button btnUpdateCard = (Button)findViewById(R.id.update_card);
    Button btnUpdateAll = (Button)findViewById(R.id.update);
      final EditText txtMeteNumber = (EditText) findViewById(R.id.meter_number);
      final EditText txtEmail = (EditText) findViewById(R.id.email_address);
      final EditText txtPhone = (EditText) findViewById(R.id.phone);
    ((EditText)findViewById(R.id.meter_number)).setText(this.mysqliteFunction.getMeterNumber());
      //txtEmail.setText(this.mysqliteFunction.getEmail());

      txtMeteNumber.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(),UpdateMeterNumber.class);
              startActivity(intent);
          }
      });

    btnUpdateAll.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        meterNumber = txtMeteNumber.getText().toString();
          emailAddress = txtEmail.getText().toString();
          phoneNumber = txtPhone.getText().toString();

          if(curMeterNumber.equals(meterNumber) && emailAddress.isEmpty() && phoneNumber.isEmpty()){
              Intent i = new Intent(SettingsActivity.this,MainActivity.class);
               startActivity(i);
              return;
          }
          if(!curMeterNumber.equals(meterNumber)){
            if(meterNumber.length() == 11){
                updateMeter = true;
            }else {
                txtMeteNumber.setError("meter number must be 11 digits");
                return;
            }
         }

          if(!emailAddress.isEmpty()){
            if(validateEmail(emailAddress)){
                updateEmail = true;
            }else {
                txtEmail.setError("invalid email address");
                return;
            }
          }

          if(!phoneNumber.isEmpty()){
              if(phoneNumber.startsWith("7") && phoneNumber.length() == 8 ){
                      updatePhone = true;

              }else {
                  txtPhone.setError("incorrect phone numbers");
                  return;
              }
          }


          if(isOnline()){
            new UpdateTask().execute();
          }else {
              dialogMsg = "You are offline Please check your internet settings";
              new ErrorMsgDialog().show(getFragmentManager(), null);
          }

         // overridePendingTransition(R.anim.from, R.anim.to);
      }
    });
    btnUpdateCard.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(SettingsActivity.this, UpdateCardActivity.class);
        SettingsActivity.this.startActivity(localIntent);
      }
    });
    bckBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
          startActivity(intent);
          finish();
      }
    });
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

    public static class ErrorMsgDialog extends DialogFragment
    {
        public Dialog onCreateDialog(Bundle paramBundle)
        {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
            localBuilder.setMessage(dialogMsg).setCancelable(false).setTitle(getResources().getString(R.string.app_name)).setPositiveButton("ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                {
                }
            });
            return localBuilder.create();
        }
    }

    private class UpdateTask extends AsyncTask<String,Void,String>{
        JSONArray jAr = null;
        private ProgressDialog progressDialog;
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            JSONArrayFunctions jsonArrayFunctions = new JSONArrayFunctions();
            Request xmlVendRequest = new Request();

            if(updateMeter){
                String utilityResponse = xmlVendRequest.verifyMeterNumber(meterNumber);
                if(utilityResponse.equals("can't establish connection to server") || utilityResponse.equals("No response From the server")){
                    resp = "Error While Communicating with the server Please Try again later";
                }else {

                    if(utilityResponse.equals("success")){
                        resp = jsonArrayFunctions.updateDetails(meterNumber,emailAddress,phoneNumber,curPhone);
                    }else{
                        //in this we will detect the error message related to the meter number and display it to the user
                        resp = "failed to verify";
                    }
                }
            }else {
                meterNumber="";
                resp = jsonArrayFunctions.updateDetails(meterNumber,emailAddress,phoneNumber,curPhone);
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            resp = this.resp;
            super.onPostExecute(resp);
            progressDialog.dismiss();

            if (resp.equals("Error While Communicating with the server Please Try again later")) {
                dialogMsg = "Please check your internet connection";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }else if(resp.equals("failed to verify")){
                dialogMsg = "your meter number is invalid or cannot be verified";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }else {
                try {
                    jAr = new JSONArray(resp);
                    JSONObject serverObj = jAr.getJSONObject(0);
                    String serverMessge = serverObj.getString("message");
                    if (serverMessge.equalsIgnoreCase("successfull update")) {
                        dialogMsg = "detail(s) successfully updated";
                        new ErrorMsgDialog().show(getFragmentManager(), null);
                        if(updateMeter){
                            mysqliteFunction.updateMeterNumber(meterNumber);
                        }

                        if(updateEmail){
                            mysqliteFunction.updateEmail(emailAddress);
                        }

                        if(updatePhone){
                            mysqliteFunction.updatePhone("+267"+phoneNumber);
                        }
                        new Handler().postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                         ,2000);
                    }else if(serverMessge.equalsIgnoreCase("update error")){
                        dialogMsg = "Some details could not be updated";
                        new ErrorMsgDialog().show(getFragmentManager(), null);
                    }else {
                        dialogMsg = "Server error please try again later";
                        new ErrorMsgDialog().show(getFragmentManager(), null);
                    }
                } catch (JSONException e) {
                    dialogMsg = "Processing error Please Try again later";
                    new ErrorMsgDialog().show(getFragmentManager(), null);
                    e.printStackTrace();
                }

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingsActivity.this);
            progressDialog.setMessage("Updating Your Details...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


    }
}
