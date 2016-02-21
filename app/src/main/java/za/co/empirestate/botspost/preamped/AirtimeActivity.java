package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class AirtimeActivity extends Activity {
    private static final String LOG = "Hey George" ;
    View backButton;
    ImageButton backImage;
    Button next;
    Spinner spnAmnt, spnProviders;
    String li_amount,voucher,ls_voucher;
    String amount;
    private MySQLiteFunctions mysqliteFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime);
        this.mysqliteFunction = new MySQLiteFunctions(this);
        setFields();
        setSpinners();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AirtimeActivity.this.mysqliteFunction.checkPaymentHistory())
                {
                    Intent localIntent1 = new Intent(AirtimeActivity.this, ConfirmPurchaseActivity.class);
                    localIntent1.putExtra("amount", AirtimeActivity.this.amount);
                    localIntent1.putExtra("isNew", false);
                    localIntent1.putExtra("meter_number", voucher);
                    AirtimeActivity.this.startActivity(localIntent1);
                    overridePendingTransition(R.anim.from, R.anim.to);
                    return;
                }
                Intent localIntent2 = new Intent(AirtimeActivity.this, PaymentDetailsActivity.class);
                Log.d(LOG,"sending first Selected meter number "+ voucher);
                localIntent2.putExtra("meter_number", voucher);
                localIntent2.putExtra("amount", AirtimeActivity.this.amount);
               AirtimeActivity.this.startActivity(localIntent2);
                // overridePendingTransition(R.anim.from, R.anim.to);


            }
        });

    }




    public  void setFields(){
        backButton = (View)findViewById(R.id.btnBack);
        backImage =  (ImageButton)findViewById(R.id.imgBack);
        spnAmnt =    (Spinner)findViewById(R.id.spAmount);
        spnProviders = (Spinner)findViewById(R.id.smProviders);
        next = (Button)findViewById(R.id.btnNext);
    }

    public  void setSpinners(){
     //initialise with default values
      //voucher = "1";
      //  amount = "100";
        Log.d(LOG,"current voucher  is "+ ls_voucher);
        //spinner for denominations
        ArrayAdapter<CharSequence> adapterAmnt = ArrayAdapter.createFromResource(this, R.array.airtime_vouchers,
                android.R.layout.simple_spinner_item);
        adapterAmnt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAmnt.setAdapter(adapterAmnt);

        spnAmnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                li_amount = spnAmnt.getSelectedItem().toString().substring(1);
                       amount = li_amount;
                Log.d(LOG,"Selected amount is "+ amount);
            }



            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });


         //spinner for providers
        ArrayAdapter<CharSequence> adapterProviders = ArrayAdapter.createFromResource(this, R.array.airtime_providers,
                android.R.layout.simple_spinner_item);
        adapterProviders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProviders.setAdapter(adapterProviders);



        spnProviders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               ls_voucher = spnProviders.getSelectedItem().toString().substring(0);
                voucher = ls_voucher;
                Log.d(LOG,"Selected voucher is "+ voucher);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
