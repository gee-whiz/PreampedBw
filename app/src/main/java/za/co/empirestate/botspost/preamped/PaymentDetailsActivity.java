package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import za.co.empirestate.botspost.model.Payment;
import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class PaymentDetailsActivity extends Activity
{
    private static final String LOG = "Hey Gee" ;
    int spnMonthPos;
    int spnYearPos;
    EditText txtInitial;
    EditText txtCvv;
    EditText txtNumber;
    EditText txtSurname;
    ImageButton back;
    Time localTime;

    private String amount;
    private String cardType;
    private String meterNumber;
    private String month;
    private MySQLiteFunctions mysqliteFunction;
    private Payment payment;
    private String year,groupId,voucherValue,ls_transactionFee,PaidUntil;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_payment_details);
        Intent localIntent = getIntent();
        this.meterNumber = localIntent.getStringExtra("meter_number");
        this.amount = localIntent.getStringExtra("amount");
        this.groupId = localIntent.getStringExtra("groupId");
        voucherValue = localIntent.getStringExtra("voucherValue");
        ls_transactionFee = localIntent.getStringExtra("transactionFee");
        PaidUntil =  localIntent.getStringExtra("PaidUntil");

        this.mysqliteFunction = new MySQLiteFunctions(this);
        final Spinner spnMonths = (Spinner) findViewById(R.id.month);
        final Spinner spnYears = (Spinner) findViewById(R.id.year);
        Button btnNext = (Button) findViewById(R.id.btn_next);
        txtInitial = (EditText) findViewById(R.id.initial);
        txtCvv = (EditText) findViewById(R.id.cvv);
        txtNumber = (EditText) findViewById(R.id.card_number);
        txtSurname = (EditText) findViewById(R.id.surname);
        final RadioButton rdoMasterCard = (RadioButton) findViewById(R.id.master_card);
        localTime = new Time(Time.getCurrentTimezone());
        localTime.setToNow();
        back = (ImageButton)findViewById(R.id.bck_btn);



        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapterMonths = ArrayAdapter
                .createFromResource(this, R.array.months_array,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMonths
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnMonths.setAdapter(adapterMonths);

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapterYears = ArrayAdapter
                .createFromResource(this, R.array.years_array,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterYears
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnYears.setAdapter(adapterYears);
        spnMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                PaymentDetailsActivity.this.month = spnMonths.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
        spnYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                year = spnYears.getSelectedItem().toString();
                year = year.substring(2);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(PaymentDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                String cardHolderInitial = txtInitial.getText().toString();
                String cardHolderSurname = txtSurname.getText().toString();
                String cardNumber;
                String strExpMonth;
                String strExpYear;
                String strCvv;
                int currentMonth = localTime.month;
                int usrMonth = Integer.parseInt(month);
                if (rdoMasterCard.isChecked()) {
                    PaymentDetailsActivity.this.cardType = "master card";
                } else {
                    PaymentDetailsActivity.this.cardType = "visa";
                }

                cardNumber = txtNumber.getText().toString();
                strExpMonth = PaymentDetailsActivity.this.month;
                strExpYear = PaymentDetailsActivity.this.year;
                strCvv = txtCvv.getText().toString();
                if ((cardHolderInitial.isEmpty()) || (cardHolderInitial.isEmpty()) || cardNumber.isEmpty()) {
                    if (cardHolderInitial.isEmpty())
                        txtInitial.setError("please enter the card holder initial");
                    if (cardHolderSurname.isEmpty())
                        txtSurname.setError("please enter the card holder surname");

                    if (cardNumber.isEmpty())
                        txtNumber.setError("please enter the card holder surname");

                    return;

                } else {


                    Log.d("currentMonth",""+currentMonth);
                    Log.d("usrMonth",""+usrMonth);

                    if (!(cardNumber.length() == 16) || !(strCvv.length() == 3)) {
                        if (strCvv.length() != 3)
                            txtCvv.setError("cvv must be 3 digits");

                        if (cardNumber.length() != 16)
                            txtNumber.setError("card number must be 16 digits");

                        return;
                    }
                }

                Intent localIntent = new Intent(PaymentDetailsActivity.this, ConfirmPurchaseActivity.class);
                PaymentDetailsActivity.this.payment = new Payment(cardHolderInitial, cardHolderSurname, PaymentDetailsActivity.this.cardType, cardNumber, strExpMonth, strExpYear, PaymentDetailsActivity.this.meterNumber, PaymentDetailsActivity.this.amount, strCvv);
                // PaymentDetailsActivity.this.mysqliteFunction.deletePayment();
                PaymentDetailsActivity.this.mysqliteFunction.createPaymentTable(cardNumber, cardHolderInitial, cardHolderSurname, strCvv, PaymentDetailsActivity.this.month, PaymentDetailsActivity.this.year, cardNumber.substring(13));
                localIntent.putExtra("payment_details", PaymentDetailsActivity.this.payment);
                localIntent.putExtra("isNew", true);
                localIntent.putExtra("voucherValue",voucherValue);

                localIntent.putExtra("meter_number", PaymentDetailsActivity.this.meterNumber);
                localIntent.putExtra("transactionFee", ls_transactionFee);
                Log.d(LOG, "I'm sending this " + ls_transactionFee);
                localIntent.putExtra("groupId", groupId);
                localIntent.putExtra("PaidUntil",PaidUntil);
                Log.d(LOG,"transaction fee"+ls_transactionFee);
                //Intent encryptIntent = new Intent();
                //encryptIntent.putExtra("card_number",cardNumber);
                //encryptIntent.putExtra("card_cvv",strCvv);
                //startService(encryptIntent);
                PaymentDetailsActivity.this.startActivity(localIntent);
            }
        });
    }
}