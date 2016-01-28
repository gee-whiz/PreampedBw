package za.co.empirestate.botspost.preamped;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;

import java.util.concurrent.atomic.AtomicInteger;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

/*
 * Created by joel on 15/03/24.
 */
public class GcmIntentService extends IntentService {

    public int notification_id = 10;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    String amount,token,date,time,reference,units,errorMsg;
    private MySQLiteFunctions mysqliteFunction;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        this.mysqliteFunction = new MySQLiteFunctions(this);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {

                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                if(extras.getString("is_token").equals("false")){

                    errorMsg = extras.getString("message");
                    reference = extras.getString("ref");

                    Intent i = new Intent(getBaseContext(),ConfirmTransactionService.class);
                    i.putExtra("reference",reference);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mysqliteFunction.createHistoryTable(reference,amount,token,"",units,date,time);

                    Toast.makeText(getApplicationContext(), errorMsg,
                            Toast.LENGTH_SHORT).show();

                   // getApplication().startService(i);

                    sendNotification("Transaction Failed");

                }else{
                    amount = extras.getString("amount");
                    reference = extras.getString("ref");
                    time = extras.getString("time");
                    token = extras.getString("token");
                    date = extras.getString("date");
                    notification_id = Integer.parseInt(reference);
                    units = extras.getString("units");

                    Intent i = new Intent(getBaseContext(),ConfirmTransactionService.class);
                    i.putExtra("reference",reference);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mysqliteFunction.createHistoryTable(reference,amount,token,"",units,date,time);

                    getApplication().startService(i);

                    sendNotification("New electricity token received");
                }

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    // Put the message into a notification and post it.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews remoteView = new RemoteViews(getPackageName(),R.layout.expandable_notifications_layout);
        remoteView.setTextViewText(R.id.not_txt,"Token: "+token);
        Intent i;
        if(msg.equals("New electricity token received")){
            i = new  Intent(this, NotificationActivity.class);
            i.putExtra("token",token);
            i.putExtra("amount",amount);
            i.putExtra("time",time);
            i.putExtra("date",date);
            i.putExtra("reference",reference);
            i.putExtra("units",units);
        }else {
            remoteView.setTextViewText(R.id.message,"Transaction Failed");
            i = new  Intent(this, UnsuccessfullActivity.class);
            i.putExtra("error_msg",errorMsg);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);


         mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Botswana Post")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.YELLOW, 3000, 3000)
                        .setContent(remoteView);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notification_id, mBuilder.build());
    }
}
