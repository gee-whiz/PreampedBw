package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class AirtimeActivity extends Activity {
    private static final String LOG = "Hey George" ;
    View backButton;
    ImageButton backImage;
    Spinner spnAmnt, spnProviders;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime);
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

    }




    public  void setFields(){
        backButton = (View)findViewById(R.id.btnBack);
        backImage =  (ImageButton)findViewById(R.id.imgBack);
        spnAmnt =    (Spinner)findViewById(R.id.spAmount);
        spnProviders = (Spinner)findViewById(R.id.smProviders);
    }

    public  void setSpinners(){
        //spinner for denominations
        ArrayAdapter<CharSequence> adapterAmnt = ArrayAdapter.createFromResource(this, R.array.airtime_vouchers,
                android.R.layout.simple_spinner_item);
        adapterAmnt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAmnt.setAdapter(adapterAmnt);

        spnAmnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                       amount = spnAmnt.getSelectedItem().toString().substring(1);
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
    }

}
