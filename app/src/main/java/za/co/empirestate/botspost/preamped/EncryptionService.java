package za.co.empirestate.botspost.preamped;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

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

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

/**
 * Created by joel on 15/05/14.
 */
public class EncryptionService extends IntentService {

    static String iv,shaKey;
    String cardNumber,cvv,initial,surname,expMonth,expYear,last3Digits;
    AES aes;
    private MySQLiteFunctions mysqliteFunction;
    private String LOG = "HEy Gee";

    public EncryptionService() {
        super("EncriptionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

      cardNumber = intent.getStringExtra("card_number");
      cvv = intent.getStringExtra("card_cvv");
      initial = intent.getStringExtra("initial");
      surname = intent.getStringExtra("surname");
      expMonth = intent.getStringExtra("exp_month");
      expYear = intent.getStringExtra("exp_year");
      last3Digits = intent.getStringExtra("last3_digits");
      mysqliteFunction = new MySQLiteFunctions(getApplicationContext());
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
        new BackgroundTask().execute(iv,shaKey,mysqliteFunction.getPhone(),mysqliteFunction.getMeterNumber());
    }

    private class BackgroundTask extends AsyncTask<String,Void,String> {

        Request request = new Request();
        String response = "";
        String host = "http://utc.empirestate.co.za";


        @Override
        protected String doInBackground(String... params) {

            try{
                HttpURLConnection urlc = (HttpURLConnection) (new URL(host)
                        .openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();

                if (urlc.getResponseCode() == 200) {
                    response = request.addParamsToReq(params[0],params[1],params[2],params[3]);
                }else {
                    response = "no internet connection";
                }
            }catch (IOException ex){

            }
            Log.e("RESPONSE",response);
            return response;
        }

        @Override
        protected void onPostExecute(String resp){
            super.onPostExecute(resp);
            resp = this.response;

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
                    mysqliteFunction.updateCardDetails(cardNumber,cvv,initial,surname,expMonth,expYear,last3Digits);
                    Log.d(LOG,"card number updated "+cardNumber);
                    Toast.makeText(getApplicationContext(),"Card successfully updated",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Card details could not be updated please check your internet settings",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Card details could not be updated please check your internet settings",Toast.LENGTH_LONG).show();
            }
        }
    }
}