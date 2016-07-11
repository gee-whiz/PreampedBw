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

import java.util.Locale;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class Eleconfirm extends Activity {
    private static final String LOG = "Hey George" ;
    TextView tAmount,tTransactionFee,tTotal,tMeter,tToken;
    Button next,cancel;
    View back;
    String amount,str,cardNumber;
    double TokenAmount;
    Intent localIntent;
    private MySQLiteFunctions mysqliteFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleconfirm);
        SetFields();
        this.mysqliteFunction = new MySQLiteFunctions(this);
        localIntent = getIntent();
        str = localIntent.getStringExtra("meter_number");
        amount = localIntent.getStringExtra("amount");

        UpdateFields();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Eleconfirm.this.mysqliteFunction.checkPaymentHistory()) {
                    checkCard();

                }
                else {

                    Intent localIntent2 = new Intent(Eleconfirm.this, PaymentDetailsActivity.class);
                    localIntent2.putExtra("meter_number", str);
                    localIntent2.putExtra("amount", Eleconfirm.this.amount);
                    Eleconfirm.this.startActivity(localIntent2);
                    // overridePendingTransition(R.anim.from, R.anim.to);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Eleconfirm.this, AccountDetailsActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Eleconfirm.this,AccountDetailsActivity.class);
                startActivity(intent);
            }
        });
    }




    public  void  SetFields(){

        tAmount = (TextView)findViewById(R.id.txtAmount);
        tTransactionFee =  (TextView)findViewById(R.id.txtaTransactionFee);
        tTotal = (TextView)findViewById(R.id.txtTotal);
        next = (Button)findViewById(R.id.btnNext);
        cancel = (Button)findViewById(R.id.btnCancel);
        back = findViewById(R.id.btnBack);
        tMeter = (TextView)findViewById(R.id.txtVoucher);
        tToken = (TextView)findViewById(R.id.txtaToken);



    }


    public  void  UpdateFields(){

        TokenAmount = Double.parseDouble(amount) -4.50;
        tAmount.setText("P"+amount+".00");
        tMeter.setText(str);
        tTransactionFee.setText("P4.50");
        tTotal.setText("P" + amount+".00");
        tToken.setText("P"+String.format(Locale.ENGLISH, "%.2f", TokenAmount));
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
                Intent localIntent1 = new Intent(Eleconfirm.this, ConfirmPurchaseActivity.class);
                localIntent1.putExtra("amount", Eleconfirm.this.amount);
                localIntent1.putExtra("isNew", false);
                localIntent1.putExtra("meter_number", str);
                Eleconfirm.this.startActivity(localIntent1);
                overridePendingTransition(R.anim.from, R.anim.to);
                return;
            }

        }
    }

    private  void CardError(String message)
    {

        LayoutInflater inflater = LayoutInflater.from(Eleconfirm.this);
        View promptView = inflater.inflate(R.layout.network_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Eleconfirm.this);
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
                Intent intent = new Intent(Eleconfirm.this,SettingsActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });


    }
}