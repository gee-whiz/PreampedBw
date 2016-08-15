package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class WaterSuccess extends Activity {

    private static final String LOG = "hey Gee" ;
    TextView tAccountHolder,tAmount,tCustomerNumber,tContractNumber,tTransactionfee,tTotalDue,txtRef,tExpDate,tExpTime;
    String ls_customerNumber,ls_theDate,ls_contractNumber,ls_amount,ls_accountHolder,cardNumber,ls_ref;
    Intent localIntent;
    Context ctx;
    Button bNext,bCancel;
    TextView tDate,ttime;
    View        bBack;
    ImageButton iBack;
    Time localTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_success);
        localTime = new Time(Time.getCurrentTimezone());
        localTime.setToNow();
        localIntent = getIntent();
        ctx = getApplicationContext();
        setFields();
        updateFields();


        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, WaterPurchaseConfirm.class);
                startActivity(intent);
            }
        });


        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }



    void  updateFields(){
        ls_accountHolder = localIntent.getStringExtra("accountHolder");
        ls_contractNumber = localIntent.getStringExtra("contractNumber");
        ls_customerNumber = localIntent.getStringExtra("customerNumber");
        ls_amount   = localIntent.getStringExtra("amount");
        ls_ref   = localIntent.getStringExtra("paymentReference");
        tAccountHolder.setText(ls_accountHolder);
        tContractNumber.setText(ls_contractNumber);
        tCustomerNumber.setText(ls_customerNumber);

        Log.d(LOG, "amount " + ls_accountHolder);
        Double  amount = Double.parseDouble(ls_amount);
        tAmount.setText("P" + String.format(Locale.ENGLISH, "%.2f", amount));
        int curMonth = localTime.month + 1;
        tDate.setText(localTime.monthDay + "/" + curMonth + "/" + localTime.year);
        ttime.setText(localTime.format("%k:%M:%S"));
        txtRef.setText(ls_ref);



    }
    void setFields(){

        tAccountHolder = (TextView)findViewById(R.id.txtAccountHolder);
        tCustomerNumber = (TextView)findViewById(R.id.txtCustomerNumber);
        tContractNumber = (TextView)findViewById(R.id.txtContractNumber);
        tAmount = (TextView)findViewById(R.id.txtAmount);
        tDate = (TextView)findViewById(R.id.date);
        ttime     = (TextView)findViewById(R.id.time);
        bNext   =           (Button)findViewById(R.id.btnNext);

        iBack       =       (ImageButton)findViewById(R.id.bck_btn);
        txtRef =   (TextView)findViewById(R.id.txtRefNumber);

    }
}
