package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class PoBoxSuccessfull extends Activity {

    TextView  tPaymentReference;
    TextView  tAmount;
    TextView  tPaidUntul;
    TextView  tTransactionFee;
    String  PaymentReference;
    String  PaidUntil;
   Double Amount;
    String  transactionFee;
     Button bFinish;
    ImageButton  back;
    View  bview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_box_successfull);


        setFields();
        updateFields();



        bFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PoBoxSuccessfull.this, MainActivity.class);
                startActivity(intent);
            }
        });


         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent  intent = new Intent(PoBoxSuccessfull.this,ConfirmPurchaseActivity.class);
                 startActivity(intent);
             }
         });
        bview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(PoBoxSuccessfull.this,ConfirmPurchaseActivity.class);
                startActivity(intent);
            }
        });

    }


    public  void  setFields(){

        tPaymentReference = (TextView)findViewById(R.id.txtPaymentReference);
        tAmount    =  (TextView)findViewById(R.id.txtAmount);
        tPaidUntul = (TextView)findViewById(R.id.txtPaid);
        tTransactionFee   = (TextView)findViewById(R.id.txtaTransactionFee);
        bFinish = (Button)findViewById(R.id.btn_finish);
        back = (ImageButton)findViewById(R.id.bck_btn);
        bview = findViewById(R.id.bview);
    }



    public  void   updateFields(){
        Intent intent = getIntent();
        PaymentReference =   intent.getStringExtra("PaymentReference");
        PaidUntil=   intent.getStringExtra("PaidUntil");
        String tmpAmount = intent.getStringExtra("Amount");
        transactionFee = intent.getStringExtra("transactionFee");

        tPaymentReference.setText(PaymentReference);
      Amount  =  Double.parseDouble(tmpAmount);

        tAmount.setText("P" + String.format(Locale.ENGLISH, "%.2f", Amount));
        tPaidUntul.setText(PaidUntil);
        tTransactionFee.setText(transactionFee);



    }


}
