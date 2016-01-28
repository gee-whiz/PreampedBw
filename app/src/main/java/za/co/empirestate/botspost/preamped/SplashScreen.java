package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

//import main.java.com.mindscapehq.android.raygun4android.RaygunClient;
//import main.java.com.mindscapehq.android.raygun4android.messages.RaygunUserInfo;

public class SplashScreen extends Activity
{

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Fabric.with(this, new Crashlytics());
      setContentView(R.layout.activity_splash_screen);

     startService(new Intent(this,GcmRegistrationService.class));
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        SplashScreen.this.startActivity(new Intent(SplashScreen.this, RegOrLogActivity.class));
         // overridePendingTransition(R.anim.from, R.anim.to);
        SplashScreen.this.finish();
      }
    }
    , 3000);
  }
}