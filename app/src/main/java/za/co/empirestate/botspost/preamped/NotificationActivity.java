package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView txtTokenTop = (TextView)findViewById(R.id.token_top);
        TextView txtTokenBottom = (TextView)findViewById(R.id.token_bottom);
        TextView txtRef = (TextView)findViewById(R.id.reference);
       // TextView txtCost = (TextView)findViewById(R.id.cost);
       // TextView txtvat = (TextView)findViewById(R.id.vat);
        TextView txtDate = (TextView)findViewById(R.id.date);
        TextView txtTime = (TextView)findViewById(R.id.time);
        TextView txtTotalAmt = (TextView) findViewById(R.id.unit);
        TextView txtUnits = (TextView) findViewById(R.id.nbr_of_units);
        //TextView txtLevy = (TextView) findViewById(R.id.levy);
        Button btnFinish = (Button)findViewById(R.id.btn_finish);

        Bundle intent = getIntent().getExtras();
        String token = intent.getString("token");
        txtTotalAmt.setText("P"+intent.getString("amount"));
        txtUnits.setText("Kwh");
        txtTokenTop.setText(token.substring(0,4)+" "+token.substring(4,8)+" "+token.substring(8,12));
        txtTokenBottom.setText(token.substring(12,16) +" "+token.substring(16,20));
        txtRef.setText(intent.getString("reference"));
        txtDate.setText(intent.getString("date"));
        txtTime.setText(intent.getString("time"));

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NotificationActivity.this, RegOrLogActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NotificationActivity.this.startActivity(i);
            }
        });


    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(NotificationActivity.this, RegOrLogActivity.class);
        startActivity(i);
        finish();
    }
}