package za.co.empirestate.botspost.preamped;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import za.co.empirestate.botspost.ssl.MySSLSocketFactory;

/**
 * Created by jojo on 1/27/2015.
 */
public class Request {

    private static final String LOG = "Hey Gee" ;
    static InputStream is = null;
    public Request(){

    }

    public String verifyMeterNumber(String meterNumber){

        String uniqueId = null;
      // String url = "http://utc.empirestate.co.za:8080/eskom-xmlvend-client-src/UniqueIdServlet";
        String url = "http://utc.empirestate.co.za/bostwana_post_office/verify_meter.php";
        LinkedList localLinkedList = new LinkedList();
        localLinkedList.add(new BasicNameValuePair("meter_number", meterNumber));
        // Making HTTP request
        try {
            int timeoutConnection = 20000;
            int timeoutSocket = 20000;
            KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            localKeyStore.load(null, null);
            MySSLSocketFactory localMySSLSocketFactory = new MySSLSocketFactory(localKeyStore);
            localMySSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            BasicHttpParams localBasicHttpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(localBasicHttpParams, "UTF-8");
            HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, timeoutConnection);
            HttpConnectionParams.setSoTimeout(localBasicHttpParams,timeoutSocket);
            SchemeRegistry localSchemeRegistry = new SchemeRegistry();
            localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            localSchemeRegistry.register(new Scheme("https", localMySSLSocketFactory, 443));
            DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
           // DefaultHttpClient httpClient = new  DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(localLinkedList));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (SocketTimeoutException exc1){
            return  uniqueId = "can't establish connection to server";

        }catch (ConnectTimeoutException exc2){
            return  uniqueId = "No response From the server";
        }catch (KeyStoreException exc3){
            exc3.printStackTrace();

        }catch (NoSuchAlgorithmException exc4){
            exc4.printStackTrace();
        }catch (CertificateException exc5){
            exc5.printStackTrace();
        }catch (IOException exc7){
            exc7.printStackTrace();
        }catch (UnrecoverableKeyException exc7){
            exc7.printStackTrace();
        }catch (KeyManagementException exc8){
            exc8.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            uniqueId = sb.toString();
            Log.e("UTILITY RESPONSE", uniqueId);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "Invalid server response";
        }

        if(!uniqueId.isEmpty()){
            uniqueId = uniqueId.substring(0, uniqueId.length()-1);
        }else {
            uniqueId = "Invalid server response";
        }


        return uniqueId;
    }


    public String getRespFromUrl(String url, List<NameValuePair> params) {
        String resp = null;

        // Making HTTP request
        try {
            // defaultHttpClient
            int timeoutConnection = 45000;
            int timeoutSocket = 45000;
            KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            localKeyStore.load(null, null);
            MySSLSocketFactory localMySSLSocketFactory = new MySSLSocketFactory(localKeyStore);
            localMySSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            BasicHttpParams localBasicHttpParams = new BasicHttpParams();
            HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(localBasicHttpParams, "UTF-8");
            HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, timeoutConnection);
            HttpConnectionParams.setSoTimeout(localBasicHttpParams,timeoutSocket);
            SchemeRegistry localSchemeRegistry = new SchemeRegistry();
            localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            localSchemeRegistry.register(new Scheme("https", localMySSLSocketFactory, 443));
            DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
            //DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (SocketTimeoutException exc1){
            return  resp = "connection error";

        }catch (ConnectTimeoutException exc2){
            return  resp = "invalid server response";
        }catch (KeyStoreException exc3){
            exc3.printStackTrace();

        }catch (NoSuchAlgorithmException exc4){
            exc4.printStackTrace();
        }catch (CertificateException exc5){
            exc5.printStackTrace();
        }catch (IOException exc7){
            exc7.printStackTrace();
        }catch (UnrecoverableKeyException exc7){
            exc7.printStackTrace();
        }catch (KeyManagementException exc8){
            exc8.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            resp = sb.toString();
            Log.e("TRANS RESPONSE", resp);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "server error";
        }

        if(resp != ""){
            resp = resp.substring(0, resp.length()-1);
        }else {
            resp = "invalid server response";
        }

        return resp;

    }

    public String addPaymentParamaters(String name, String surname,
                                 String cardNumber, String cvv, String expMonth,String expYear,String amt,String phone,String meterNumber,String tokenAmnt,String date,String time) {
        String url = "http://utc.empirestate.co.za/bostwana_post_office/index.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("cname", name + " " + surname));
        params.add(new BasicNameValuePair("cc", URLEncoder.encode(cardNumber)));
        params.add(new BasicNameValuePair("exp_month", expMonth));
        params.add(new BasicNameValuePair("exp_year", expYear));
        params.add(new BasicNameValuePair("cvv", URLEncoder.encode(cvv)));
        params.add(new BasicNameValuePair("amnt", amt));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("meter_number", meterNumber));
        params.add(new BasicNameValuePair("token_amount", tokenAmnt));
        params.add(new BasicNameValuePair("date", date));
        params.add(new BasicNameValuePair("time", time));
         Log.d(LOG,"Electricity encoded "+params);
        return getRespFromUrl(url, params);


    }


    public String addParamatersToConfirmTrans(String ref) {
        String url = "http://utc.empirestate.co.za/bostwana_post_office/confirm_transaction.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ref", ref));
        return getRespFromUrl(url, params);

    }

    public String addParamatersToSetTransStatus(String device_id) {
        String url = "http://utc.empirestate.co.za/bostwana_post_office/set_transaction_status.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("device_id", device_id));
        params.add(new BasicNameValuePair("plateform", "ANDROID"));
        return getRespFromUrl(url, params);

    }

    public String addParamatersToReset(String email,String phone) {
        String url = "http://utc.empirestate.co.za/bostwana_post_office/gen_tok_res_pass.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("send_token", "true"));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("phone", phone));
        return getRespFromUrl(url, params);
    }

    public String addParamsToReq(String iv,String key,String phone,String meter_number)
    {
       String url = "http://utc.empirestate.co.za/bostwana_post_office/update_key_and_iv.php";
        //encryptedString= URLEncoder.encode(encryptedString);
        LinkedList localLinkedList = new LinkedList();
     //   localLinkedList.add(new BasicNameValuePair("encrypted_string", URLEncoder.encode(encryptedString)));
        localLinkedList.add(new BasicNameValuePair("iv", iv));
        localLinkedList.add(new BasicNameValuePair("key", key));
        localLinkedList.add(new BasicNameValuePair("meter_number", meter_number));
        localLinkedList.add(new BasicNameValuePair("phone", phone));
        Log.d(LOG, "card update encoded " +localLinkedList  );
        return getRespFromUrl(url, localLinkedList);
    }
}