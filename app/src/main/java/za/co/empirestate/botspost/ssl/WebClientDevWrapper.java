package za.co.empirestate.botspost.ssl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

public class WebClientDevWrapper
{
  public static HttpClient getNewHttpClient()
  {
    try
    {
      int timeoutConnection = 5000;
      int timeoutSocket = 10000;
      KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      localKeyStore.load(null, null);
      MySSLSocketFactory localMySSLSocketFactory = new MySSLSocketFactory(localKeyStore);
      localMySSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(localBasicHttpParams, "UTF-8");
      HttpConnectionParams.setConnectionTimeout(localBasicHttpParams,timeoutConnection);
      HttpConnectionParams.setSoTimeout(localBasicHttpParams,timeoutSocket);
      SchemeRegistry localSchemeRegistry = new SchemeRegistry();
      localSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      localSchemeRegistry.register(new Scheme("https", localMySSLSocketFactory, 443));
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
      return localDefaultHttpClient;
    }
    catch (SocketTimeoutException exc1){
      //  return "";

    }catch (ConnectTimeoutException exc2){

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
    return new DefaultHttpClient();
  }
}
