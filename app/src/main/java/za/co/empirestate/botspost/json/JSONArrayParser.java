package za.co.empirestate.botspost.json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class JSONArrayParser {
    static InputStream is = null;
    static String response = "";

    // constructor
    public JSONArrayParser() {

    }

   /* public String getJSONFromUrl(String url, List<NameValuePair> params) {

        // Making HTTP request
        try {
            if (!url.endsWith("?"))
                url += "?";
            String paramString = URLEncodedUtils.format(params, "utf-8");

            url += paramString;

            // defaultHttpClient
           // DefaultHttpClient httpClient = (DefaultHttpClient) WebClientDevWrapper
           ///         .getNewHttpClient();

            int timeoutConnection = 5000;
            int timeoutSocket = 10000;
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
            HttpGet httpGet = new HttpGet(url);
           // httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (SocketTimeoutException exc1){
        return  response = "can't establish connection to our server";

        }catch (ConnectTimeoutException exc2){
            return  response = "No response From the server";
        }catch (KeyStoreException exc3){
            exc3.printStackTrace();
            return  response = "server error";
        }catch (NoSuchAlgorithmException exc4){
            exc4.printStackTrace();
            return  response = "server error";
        }catch (CertificateException exc5){
            exc5.printStackTrace();
            return  response = "server error";
        }catch (IOException exc7){
            exc7.printStackTrace();
            return  response = "server error";
        }catch (UnrecoverableKeyException exc7){
            exc7.printStackTrace();
            return  response = "server error";
        }catch (KeyManagementException exc8){
            exc8.printStackTrace();
            return  response = "server error";
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
            response = sb.toString();
            Log.e("JSON", response);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return  response = "server error";
        }

        return response;

    }*/

    public String getJSONFromUrl(String url, List<NameValuePair> params) {

        // Making HTTP request
        try {
            if (!url.endsWith("?"))
                url += "?";
            String paramString = URLEncodedUtils.format(params, "utf-8");

            url += paramString;


            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return  response = "network error";
        } catch (IOException e) {
            e.printStackTrace();
            return  response = "network error";
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
            response = sb.toString();
            Log.e("JSON", response);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return  response = "server error";
        }

        return response;

    }
}