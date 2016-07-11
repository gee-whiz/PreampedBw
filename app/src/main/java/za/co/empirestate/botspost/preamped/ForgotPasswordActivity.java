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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;


public class ForgotPasswordActivity extends Activity {
    private static String dialogMsg;
    EditText txtEmail;
    private MySQLiteFunctions mysqliteFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtEmail = (EditText) findViewById(R.id.email_address);
        Button btnResetPass = (Button) findViewById(R.id.btn_reset);
        this.mysqliteFunction = new MySQLiteFunctions(this);
        txtEmail.setText(mysqliteFunction.getEmail());
        findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                onBackPressed();
                finish();
            }
        });

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    new ResetTask().execute();
                }else {
                    dialogMsg="You are offline,please check your internet settings";
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

    @Override
    public void onBackPressed(){
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
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

    private class ResetTask extends AsyncTask<String,Void,String> {
        private ProgressDialog progressDialog;
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            Request request = new Request();
            String host = "http://utc.empirestate.co.za";

            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(host)
                        .openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                  String email =  txtEmail.getText().toString();
                    resp = request.addParamatersToReset(email,mysqliteFunction.getPhone());

                    if(resp == null || resp == ""){
                        resp = "Error While Communicating with the server Please Try again later";
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
            resp = this.resp;
            super.onPostExecute(resp);
            progressDialog.dismiss();

            if (resp.equals("Error While Communicating with the server Please Try again later")) {
                dialogMsg = "Error While Communicating with the server Please Try again later";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }else if (resp.equals("token not sent")){
                dialogMsg = "Reset Token could not be sent please try again";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }else if (resp.equals("your e-mail is not registered")){
                dialogMsg ="Email not registered";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }else if (resp.equals("token sent")){
                dialogMsg = "Please follow the link sent to your mail to reset your password";
                new ErrorMsgDialog().show(getFragmentManager(), null);
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                }
                        ,2000);
            }else {

                dialogMsg = "Server error please try again later";
                new ErrorMsgDialog().show(getFragmentManager(), null);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
            progressDialog.setMessage("Processing please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
}
