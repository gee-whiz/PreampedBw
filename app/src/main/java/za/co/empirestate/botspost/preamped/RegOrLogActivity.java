package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegOrLogActivity extends Activity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
      setContentView(R.layout.activity_reg_or_log);
      Button btn_log = (Button) findViewById(R.id.log_btn);
    ((Button)findViewById(R.id.reg_btn)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(RegOrLogActivity.this, RegistrationActivity.class);
        RegOrLogActivity.this.startActivity(localIntent);
        //  overridePendingTransition(R.anim.from, R.anim.to);
      }
    });
    btn_log.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(RegOrLogActivity.this, LoginActivity.class);
        RegOrLogActivity.this.startActivity(localIntent);
        //  overridePendingTransition(R.anim.from, R.anim.to);
      }
    });
  }
}
