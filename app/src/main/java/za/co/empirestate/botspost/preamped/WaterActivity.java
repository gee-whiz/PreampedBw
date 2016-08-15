package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class WaterActivity extends Activity{

    private static final String LOG = "Hey Gee";
    EditText eAccountHolder;
    EditText eCustomerNumber;
    EditText eContractNumber;
    EditText eAmount;
    Button       bNext;
    View        bBack;
    ImageButton iBack;
    Context ctx;
    String ls_customerNumber,ls_contractNumber,ls_amount,ls_accountHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        ctx = getApplicationContext();
        setFields();


        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MainActivity.class);
                startActivity(intent);
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,MainActivity.class);
                startActivity(intent);
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eAccountHolder.getText().toString().isEmpty()){
                    eAccountHolder.setError("Please enter account holder");
                    return;
                }
                else if (eContractNumber.getText().toString().isEmpty()){
                    eContractNumber.setError("Please enter contract number");
                    return;
                }
                else if (eCustomerNumber.getText().toString().isEmpty()){
                    eCustomerNumber.setError("Please enter customer number");
                    return;
                }

                else if (eAmount.getText().toString().isEmpty()){
                    eAmount.setError("Please enter amount");
                    return;
                }
                else {
                    ls_accountHolder = eAccountHolder.getText().toString();
                    ls_contractNumber = eContractNumber.getText().toString();
                    ls_amount   = eAmount.getText().toString();
                    ls_customerNumber = eCustomerNumber.getText().toString();

                    Intent intent = new Intent(WaterActivity.this, ConfirmWater.class);
                    intent.putExtra("customerNumber",ls_customerNumber);
                    intent.putExtra("contractNumber",ls_contractNumber);
                    intent.putExtra("accountHolder",ls_accountHolder);
                    intent.putExtra("amount",ls_amount);
                    Log.d(LOG,"account holder"+ ls_accountHolder);

                    startActivity(intent);

                }
            }
        });

    }

    void  setFields(){
        eAccountHolder = (EditText)findViewById(R.id.edtAcoountHolder);
        eCustomerNumber =  (EditText)findViewById(R.id.edtCustomerNumber);
        eContractNumber =  (EditText)findViewById(R.id.edtContractNumber);
        eAmount  =          (EditText)findViewById(R.id.edtAmount);
        bNext   =           (Button)findViewById(R.id.btnNext);
        bBack   =           (View)findViewById(R.id.btnBack);
        iBack       =       (ImageButton)findViewById(R.id.imgBack);



    }

}
