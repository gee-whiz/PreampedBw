package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class Eleconfirm extends Activity {
    private static final String LOG = "Hey George" ;
    TextView tAmount,tTransactionFee,tTotal,tMeter,tToken;
    Button next,cancel;
    View back;
    private MySQLiteFunctions mysqliteFunction;
    String amount,str;
    int TokenAmount;
    Intent localIntent;
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
                    Intent localIntent1 = new Intent(Eleconfirm.this, ConfirmPurchaseActivity.class);
                    localIntent1.putExtra("amount", Eleconfirm.this.amount);
                    localIntent1.putExtra("isNew", false);
                    localIntent1.putExtra("meter_number", str);
                    Eleconfirm.this.startActivity(localIntent1);
                    overridePendingTransition(R.anim.from, R.anim.to);
                    return;
                }
                Intent localIntent2 = new Intent(Eleconfirm.this, PaymentDetailsActivity.class);
                localIntent2.putExtra("meter_number", str);
                localIntent2.putExtra("amount", Eleconfirm.this.amount);
                Eleconfirm.this.startActivity(localIntent2);
                // overridePendingTransition(R.anim.from, R.anim.to);
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
        back = (View)findViewById(R.id.btnBack);
        tMeter = (TextView)findViewById(R.id.txtVoucher);
        tToken = (TextView)findViewById(R.id.txtaToken);



    }


    public  void  UpdateFields(){

        TokenAmount  = Integer.parseInt(amount) - 4;
        tAmount.setText("P"+amount+".00");
        tMeter.setText(str);
        tTransactionFee.setText("P4.00");
        tTotal.setText("P" + amount+".00");
        tToken.setText("P"+String.valueOf(TokenAmount)+".00");
    }
}
