package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;


public class PendingActivity extends Activity {

    MySQLiteFunctions mySQLiteFunctions;
    String regid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);


        TextView txtReason = (TextView) findViewById(R.id.reason);
        Button btnFinish = (Button)findViewById(R.id.btn_finish);
        Intent intent = getIntent();
        String errorMsg = intent.getStringExtra("error_msg");
        txtReason.setText(errorMsg);
        mySQLiteFunctions = new MySQLiteFunctions(this);
        regid = mySQLiteFunctions.getDeviceId();
        new UpdateTransactionTask().execute(regid);


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PendingActivity.this, RegOrLogActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(PendingActivity.this, RegOrLogActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    private class UpdateTransactionTask extends AsyncTask<String,Void,String> {
        String resp;

        @Override
        protected String doInBackground(String... params) {

            Request request = new Request();
            resp = request.addParamatersToSetTransStatus(params[0]);
            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            resp = this.resp;
            if(!resp.equals("successful")){
                Toast.makeText(getApplicationContext(), "error while updating transaction",
                        Toast.LENGTH_SHORT).show();
            }else {
                Log.e("pending",resp);
            }
        }
    }
}