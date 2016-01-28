package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import za.co.empirestate.botspost.model.Payment;

public class SuccessfullActivity extends Activity
{
  private Payment payment;
    DecimalFormat df = new DecimalFormat("#.00");

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
      setContentView(R.layout.activity_successfull);
    Intent intent = getIntent();
    this.payment = ((Payment)intent.getParcelableExtra("payment_details"));
    Time localTime = new Time(Time.getCurrentTimezone());
    localTime.setToNow();
    TextView txtTokenTop = (TextView)findViewById(R.id.token_top);
    TextView txtTokenBottom = (TextView)findViewById(R.id.token_bottom);
    TextView txtRef = (TextView)findViewById(R.id.reference);
    TextView txtCost = (TextView)findViewById(R.id.cost);
    TextView txtvat = (TextView)findViewById(R.id.vat);
    TextView txtDate = (TextView)findViewById(R.id.date);
    TextView txtTime = (TextView)findViewById(R.id.time);
    TextView txtTotalAmt = (TextView) findViewById(R.id.unit);
      TextView txtUnits = (TextView) findViewById(R.id.nbr_of_units);
   //   TextView txtLevy = (TextView) findViewById(R.id.levy);
    Button btnFinish = (Button)findViewById(R.id.btn_finish);

      btnFinish.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent i = new Intent(SuccessfullActivity.this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                      | Intent.FLAG_ACTIVITY_CLEAR_TASK);
              SuccessfullActivity.this.startActivity(i);
             // overridePendingTransition(R.anim.from, R.anim.to);
          }
      });

    double d1 = 0.0;
   // double d2 = 0.0;
    double d3 =0.0;
    double d4 =0.0;
    if (this.payment != null){
        d1 = 0.14d * Integer.parseInt(this.payment.amount);
        d3 = 0.06d * Integer.parseInt(this.payment.amount);
       // d2 = Integer.parseInt(this.payment.amount) - (d1+d3);

    }else {
        d1 = 0.14d * Integer.parseInt(intent.getStringExtra("amount"));
        d3 = 0.06d * Integer.parseInt(intent.getStringExtra("amount"));
       // d2 = Integer.parseInt(intent.getStringExtra("amount")) - (d1+d3);
    }
      d4 = d1 + d3;
      String token = intent.getStringExtra("token");
      String units = intent.getStringExtra("units");
      txtUnits.setText(units + "Kwh");
      txtTokenTop.setText(token.substring(0,4)+" "+token.substring(4,8)+" "+token.substring(8,12));
      txtTokenBottom.setText(token.substring(12,16) +" "+token.substring(16,20));
      txtRef.setText(intent.getStringExtra("reference"));
      txtCost.setText("P"+ intent.getStringExtra("cost_of_units"));
      txtvat.setText("P"+intent.getStringExtra("vat_and_levy"));
    //  txtLevy.setText("P"+String.valueOf(df.format(d3)));
     // int displayAmnt = Integer.parseInt(intent.getStringExtra("amount")) + 4;
      txtTotalAmt.setText(intent.getStringExtra("amount"));
      int curMonth = localTime.month + 1;
      txtDate.setText(localTime.monthDay + "/" + curMonth + "/" + localTime.year);
      txtTime.setText(localTime.format("%k:%M:%S"));

      Intent i = new Intent(this,ConfirmTransactionService.class);
      i.putExtra("reference",intent.getStringExtra("reference"));
      startService(i);
  }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(SuccessfullActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SuccessfullActivity.this.startActivity(i);
    }
}