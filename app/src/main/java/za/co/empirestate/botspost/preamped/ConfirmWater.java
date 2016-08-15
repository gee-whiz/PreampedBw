package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class ConfirmWater extends Activity {

    private static final String LOG = "hey Gee" ;
    TextView  tAccountHolder,tAmount,tCustomerNumber,tContractNumber,tTransactionfee,tTotalDue;
    String ls_customerNumber,ls_contractNumber,ls_amount,ls_accountHolder,cardNumber;
    Intent localIntent;
    Context  ctx;
    Button bNext,bCancel;
    View        bBack;
    ImageButton iBack;
    private MySQLiteFunctions mysqliteFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_water);
        ctx = getApplicationContext();
        localIntent = getIntent();
        this.mysqliteFunction = new MySQLiteFunctions(this);
        setFields();

            updateFields();


        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,WaterActivity.class);
                startActivity(intent);
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,WaterActivity.class);
                startActivity(intent);
            }
        });


        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,WaterActivity.class);
                startActivity(intent);
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ConfirmWater.this.mysqliteFunction.checkPaymentHistory())
                {
                    checkCard();


                }
                else {
                    Intent localIntent2 = new Intent(ConfirmWater.this,WaterPaymentDetails.class);
                    localIntent2.putExtra("customerNumber",ls_customerNumber);
                    localIntent2.putExtra("contractNumber",ls_contractNumber);
                    localIntent2.putExtra("accountHolder",ls_accountHolder);
                    localIntent2.putExtra("amount",ls_amount);
                    ConfirmWater.this.startActivity(localIntent2);

                }


            }
        });


    }







    void setFields(){

        tAccountHolder = (TextView)findViewById(R.id.txtAccountHolder);
        tCustomerNumber = (TextView)findViewById(R.id.txtCustomerNumber);
        tContractNumber = (TextView)findViewById(R.id.txtContractNumber);
        tAmount = (TextView)findViewById(R.id.txtAmount);
        tTransactionfee = (TextView)findViewById(R.id.txtFee);
        tTotalDue     = (TextView)findViewById(R.id.txtTotal);
        bNext   =           (Button)findViewById(R.id.btnNext);
        bBack   =           (View)findViewById(R.id.btnBack);
        iBack       =       (ImageButton)findViewById(R.id.imgBack);
        bCancel     =    (Button)findViewById(R.id.btnCancel);

    }

    void  updateFields(){

        ls_accountHolder = localIntent.getStringExtra("accountHolder");
        ls_contractNumber = localIntent.getStringExtra("contractNumber");
        ls_customerNumber = localIntent.getStringExtra("customerNumber");
        ls_amount   = localIntent.getStringExtra("amount");
       tAccountHolder.setText(ls_accountHolder);
        tContractNumber.setText(ls_contractNumber);
        tCustomerNumber.setText(ls_customerNumber);
        tTransactionfee.setText("0.00");
        Log.d(LOG,"amount "+ls_accountHolder);
       Double  amount = Double.parseDouble(ls_amount);
        tAmount.setText("P" + String.format(Locale.ENGLISH, "%.2f", amount));
       tTotalDue.setText("P" + String.format(Locale.ENGLISH, "%.2f", amount));



    }



    //check card if not encripted
    public  void checkCard(){
        Cursor localCursor = this.mysqliteFunction.getPaymentHistory();

        if ((localCursor != null) && (localCursor.moveToFirst()))
        {

            cardNumber=localCursor.getString(1);
            Log.e("card number",cardNumber);
            if (cardNumber.length() > 18 ){
                CardError("Please update your card details");
            }
            else
            {
                Intent localIntent1 = new Intent(ConfirmWater.this, WaterPurchaseConfirm.class);
                localIntent1.putExtra("isNew", false);
                localIntent1.putExtra("customerNumber",ls_customerNumber);
                localIntent1.putExtra("contractNumber",ls_contractNumber);
                localIntent1.putExtra("accountHolder",ls_accountHolder);
                localIntent1.putExtra("amount",ls_amount);
                ConfirmWater.this.startActivity(localIntent1);
                overridePendingTransition(R.anim.from, R.anim.to);
                return;
            }

        }
    }




    private  void CardError(String message)
    {

        LayoutInflater inflater = LayoutInflater.from(ConfirmWater.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmWater.this);
        builder.setView(promptView);
        TextView error = (TextView)promptView.findViewById(R.id.txtOr);
        error.setText(message);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysqliteFunction.deletePayment();
                Intent intent = new Intent(ConfirmWater.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });


    }


}
