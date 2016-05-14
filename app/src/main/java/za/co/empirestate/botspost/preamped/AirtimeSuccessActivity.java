package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AirtimeSuccessActivity extends Activity {

    private static final String LOG = "Hey Gee" ;
    TextView tproductName;
    TextView tamount;
    TextView tSerialNumber;
    TextView tTransactionFee;
    TextView tActivation;
    TextView tDate,ttime;
    TextView tExpDate,value;
    Button finish;
    ImageButton back;
    String amount;

    String  productName;
    String  ls_amount,ls_serialNumber,ls_activationNumber,ls_theDate,ls_transactionFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_success);
        setFields();
        final Intent i = getIntent();
        Time localTime = new Time(Time.getCurrentTimezone());
        localTime.setToNow();
        ls_activationNumber = i.getStringExtra("ActivationNumber");
        ls_serialNumber  = i.getStringExtra("SerialNumber");
        ls_amount    = i.getStringExtra("Value");
        productName = i.getStringExtra("ProductName");
        ls_theDate = i.getStringExtra("expDate");
        ls_transactionFee = i.getStringExtra("transactionFee");
        Log.e(LOG,"trans fee "+ls_transactionFee);
       String date = ls_theDate.substring(0, ls_theDate.length()-9);
        amount = String.valueOf(Integer.parseInt(ls_amount) / 100);
        if (!ls_serialNumber.isEmpty())
        {
            tproductName.setText(productName);
            tamount.setText("P"+amount+".00");
            tActivation.setText(ls_activationNumber);
            tSerialNumber.setText(ls_serialNumber);
            tExpDate.setText(date);
            int curMonth = localTime.month + 1;
            tDate.setText(localTime.monthDay + "/" + curMonth + "/" + localTime.year);
            ttime.setText(localTime.format("%k:%M:%S"));
            value.setText("P"+ amount+".00");
            //tTransactionFee.setText(ls_transactionFee);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirtimeSuccessActivity.this,AirtimeActivity.class);
                startActivity(intent);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AirtimeSuccessActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }






    public  void setFields(){

        tproductName = (TextView)findViewById(R.id.txtProductName);
        tActivation =  (TextView)findViewById(R.id.txtActivationNumber);
        tamount =(TextView)findViewById(R.id.txtAmount);
        tSerialNumber = (TextView)findViewById(R.id.txtSerial);
        tDate = (TextView)findViewById(R.id.date);
        ttime = (TextView)findViewById(R.id.time);
        tExpDate =  (TextView)findViewById(R.id.txtExpDate);
        back = (ImageButton)findViewById(R.id.bck_btn);
        finish = (Button)findViewById(R.id.btn_finish);
        value = (TextView)findViewById(R.id.txtValue);
       // tTransactionFee = (TextView)findViewById(R.id.txtFee);




    }

}
