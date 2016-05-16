package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class AirtimeConfirm extends Activity {

    private static final String LOG = "Hey George" ;
    TextView tAmount,tTransactionFee,tTotal,tVoucher;
    Button next,cancel;
    View back;
    double total;
    String amount,voucherValue,voucher,ls_transactionFee,cardNumber;
    Intent localIntent;
    private MySQLiteFunctions mysqliteFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_confirm);
        SetFields();
        localIntent = getIntent();
        voucher = localIntent.getStringExtra("meter_number");
        amount =  localIntent.getStringExtra("amount");
        UpdateFields();

        this.mysqliteFunction = new MySQLiteFunctions(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AirtimeConfirm.this.mysqliteFunction.checkPaymentHistory())
                {
                 checkCard();


                }
             else {
                    Intent localIntent2 = new Intent(AirtimeConfirm.this, PaymentDetailsActivity.class);
                    Log.d(LOG, "sending first Selected meter number " + voucher);
                    localIntent2.putExtra("meter_number", voucher);
                    localIntent2.putExtra("voucherValue", voucherValue);
                    localIntent2.putExtra("amount", String.valueOf(total));
                    localIntent2.putExtra("transactionFee", ls_transactionFee);
                    AirtimeConfirm.this.startActivity(localIntent2);

                }
                // overridePendingTransition(R.anim.from, R.anim.to);


            }

        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AirtimeConfirm.this,AirtimeActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AirtimeConfirm.this,AirtimeActivity.class);
                startActivity(intent);
            }
        });
    }






    public  void  SetFields(){

        tAmount = (TextView)findViewById(R.id.txtAmount);
       // tTransactionFee =  (TextView)findViewById(R.id.txtaTransactionFee);
        tTotal = (TextView)findViewById(R.id.txtTotal);
        next = (Button)findViewById(R.id.btnNext);
        cancel = (Button)findViewById(R.id.btnCancel);
        back = findViewById(R.id.btnBack);
        tVoucher = (TextView)findViewById(R.id.txtVoucher);



    }


    public  void  UpdateFields(){
        DecimalFormat decimalFormat= new DecimalFormat("###.#");
        double transactionFee;
        double fAmount = Double.parseDouble(amount);
        double fvoucherValue = fAmount * 100;
        voucherValue = decimalFormat.format(fvoucherValue);
        transactionFee = ((fAmount * 7) / 100);
        total = fAmount ;
        tAmount.setText("P"+amount+".00");
        tVoucher.setText(voucher);
       // tTransactionFee.setText("P"+String.format(Locale.ENGLISH, "%.2f", transactionFee) );
        ls_transactionFee = "P"+String.format(Locale.ENGLISH, "%.2f", transactionFee);
        tTotal.setText("P" + String.format(Locale.ENGLISH, "%.2f", total));



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
                Intent localIntent1 = new Intent(AirtimeConfirm.this, ConfirmPurchaseActivity.class);
                localIntent1.putExtra("amount",String.valueOf(total));
                localIntent1.putExtra("isNew", false);
                localIntent1.putExtra("meter_number", voucher);
                localIntent1.putExtra("voucherValue",voucherValue);
                localIntent1.putExtra("transactionFee",ls_transactionFee);
                AirtimeConfirm.this.startActivity(localIntent1);
                overridePendingTransition(R.anim.from, R.anim.to);
                return;
            }

        }
    }

    private  void CardError(String message)
    {

        LayoutInflater inflater = LayoutInflater.from(AirtimeConfirm.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AirtimeConfirm.this);
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
                Intent intent = new Intent(AirtimeConfirm.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });


    }

}
