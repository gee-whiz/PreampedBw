package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class UpdateCardActivity extends Activity
{
  private String last3digits;
  private String month;
  private MySQLiteFunctions mysqliteFunction;
  private String year,first13,last3;
    EditText txtInitial;
    EditText txtCvv;
    EditText txtNumber;
    EditText txtSurname;
    String[] yearsAr = {"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};
    String[] monthsAr ={"01","02","03","04","05","06","07","08","09","10","11","12"};

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_update_card);
    this.mysqliteFunction = new MySQLiteFunctions(this);
      final Spinner spnMonths = (Spinner) findViewById(R.id.month);
      final Spinner spnYears = (Spinner) findViewById(R.id.year);
    Button btnSave = (Button)findViewById(R.id.btn_back);
      txtInitial = (EditText) findViewById(R.id.initial);
      txtCvv = (EditText) findViewById(R.id.cvv);
      txtNumber = (EditText) findViewById(R.id.card_number);
      txtSurname = (EditText) findViewById(R.id.surname);

      txtNumber.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
             // ;
          }

          @Override
          public void afterTextChanged(Editable s) {
            if(txtNumber.getText().toString().length() == 0){
                txtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
          }
      });

      final RadioButton rdoMasterCard = (RadioButton) findViewById(R.id.master_card);
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
    ((ImageButton)findViewById(R.id.bck_btn)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        UpdateCardActivity.this.onBackPressed();
        UpdateCardActivity.this.finish();
      }
    });
    spnMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
          month = spnMonths.getSelectedItem().toString();
      }

      public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
      {
      }
    });
    spnYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
          year = spnYears.getSelectedItem().toString();
          year = year.substring(2);
      }

      public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
      {
      }
    });

    /*if (this.mysqliteFunction.checkPaymentHistory())
    {
     final Cursor localCursor = this.mysqliteFunction.getPaymentHistory();
      if ((localCursor != null) && (localCursor.moveToFirst()))
      {
        first13 = localCursor.getString(1).substring(0,13);
        last3 = localCursor.getString(1).substring(13);

        txtInitial.setText(localCursor.getString(2));
        txtSurname.setText(localCursor.getString(3));
        txtNumber.setText(localCursor.getString(1));
        txtCvv.setText(localCursor.getString(6));
          Log.d("year",localCursor.getString(5));

          spnMonths.post(new Runnable() {
              @Override
              public void run() {

                  int selected = 0;
                  for(int i= 1 ; i< 12 ; i++){
                      if(localCursor.getString(4).equals(monthsAr[i])){
                          selected = i;
                      }
                  }

                  spnMonths.setSelection(selected);
              }
          });

          spnYears.post(new Runnable() {
              @Override
              public void run() {
                  int selected = 0;
                  for(int i= 1 ; i< 16 ; i++){
                    if(localCursor.getString(5).equals(yearsAr[i].substring(2))){
                        selected = i;
                    }
                      Log.d("year",yearsAr[i].substring(1));
                  }
                  spnYears.setSelection(selected);
              }
          });

    }
      this.mysqliteFunction.close();
    }*/

    btnSave.setOnClickListener(new View.OnClickListener() {
        String regex = "[0-9]+";

        public void onClick(View paramAnonymousView) {
            String initial = txtInitial.getText().toString();
            String surname = txtSurname.getText().toString();
            String cardNumber;
            String cvv;
            cardNumber = txtNumber.getText().toString();
            cvv = txtCvv.getText().toString();
            if (initial.isEmpty() || surname.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty()) {
                if (initial.isEmpty())
                    txtInitial.setError("please enter the card holder initial");
                if (surname.isEmpty())
                    txtSurname.setError("please enter the card holder surname");
                if (cardNumber.isEmpty())
                    txtNumber.setError("card number must be 16 digits");
                if (cvv.isEmpty())
                    txtCvv.setError("cvv must be 3 digits");
                return;
            }


            if (!(cvv.length() == 3) || !(cardNumber.length() == 16)) {

                if (cvv.length() != 3)
                    txtCvv.setError("cvv must be 3 digits");

                if (cardNumber.length() != 16) ;
                txtNumber.setError("card number must be 16 digits");
                return;
            }

            last3digits = cardNumber.substring(13);
            Intent localIntent = new Intent(UpdateCardActivity.this, MainActivity.class);
          //  UpdateCardActivity.this.mysqliteFunction.deletePayment();
            UpdateCardActivity.this.mysqliteFunction.createPaymentTable(cardNumber, initial, surname, cvv, UpdateCardActivity.this.month, UpdateCardActivity.this.year, last3digits);
            UpdateCardActivity.this.startActivity(localIntent);
            Intent encryptIntent = new Intent(UpdateCardActivity.this,EncryptionService.class);
            encryptIntent.putExtra("card_number",cardNumber);
            encryptIntent.putExtra("card_cvv",cvv);
            encryptIntent.putExtra("initial",initial);
            encryptIntent.putExtra("surname",surname);
            encryptIntent.putExtra("exp_month",month);
            encryptIntent.putExtra("exp_year",year);
            encryptIntent.putExtra("last3_digits",last3digits);
            startService(encryptIntent);
            finish();
        }
    });
  }
}