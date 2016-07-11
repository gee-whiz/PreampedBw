package za.co.empirestate.botspost.json;

import android.util.Log;

import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;

public class JSONArrayFunctions
{
    private static final String LOG = "Hey Gee" ;
    private static String ADD_USER_TAG = "add_user";
  private static String LOG_USER_TAG = "log_user";
  private static String UPDATE_DETAILS_TAG = "update_details";
  //private static String url = "http://utc.empirestate.co.za/bostwana_post_office/sendNotifications.php";
  private static String url ="http://utc.empirestate.co.za/bostwana_post_office/log_reg_upd.php";
  private JSONArrayParser JsonParser = new JSONArrayParser();

  public String addNewUser(String paramString1, String paramString2, String paramString3,String phone,String name,String deviceId,String os)
  {
      if (deviceId == null){
          deviceId = null;
      }
    LinkedList localLinkedList = new LinkedList();
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("tag", ADD_USER_TAG));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("meter_number", paramString1));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("email", paramString2));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("password", paramString3));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("phone", phone));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("name", name));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("device_id", deviceId));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("os", os));
      Log.e(LOG,"params sent "+localLinkedList);
     if(deviceId.isEmpty()){
         return "no device id";
     }else {
         //noinspection unchecked
         return this.JsonParser.getJSONFromUrl(url, localLinkedList);
     }
  }

  public String logUser(String meterNumnber, String password,String phone,String device_id)
  {
    LinkedList localLinkedList = new LinkedList();
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("tag", LOG_USER_TAG));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("meter_number", meterNumnber));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("password", password));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("phone", phone));
      //noinspection unchecked
      localLinkedList.add(new BasicNameValuePair("device_id", device_id));
      Log.e(LOG, "values sent " +localLinkedList);
      //noinspection unchecked
      return this.JsonParser.getJSONFromUrl(url, localLinkedList);
  }

    public String updateDetails(String meterNumber, String email, String phone,String curPhone)
    {
        LinkedList localLinkedList = new LinkedList();
        //noinspection unchecked
        localLinkedList.add(new BasicNameValuePair("tag", UPDATE_DETAILS_TAG));
        //noinspection unchecked
        localLinkedList.add(new BasicNameValuePair("meter_number", meterNumber));
        //noinspection unchecked
        localLinkedList.add(new BasicNameValuePair("email", email));
        //noinspection unchecked
        localLinkedList.add(new BasicNameValuePair("phone", phone));
        //noinspection unchecked
        localLinkedList.add(new BasicNameValuePair("cur_phone", curPhone));
        //noinspection unchecked
        return this.JsonParser.getJSONFromUrl(url, localLinkedList);
    }
}
