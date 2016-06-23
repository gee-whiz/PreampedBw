package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class AccountDetailsActivity extends Activity
{
  private static final String LOG = "Hey George";
 int num;
  private String amount;
    private String savedMeters;
    private String[] mmeters,newmeters;
  private  Spinner meterNumberspn;
  private MySQLiteFunctions mysqliteFunction;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_account_details);
    this.mysqliteFunction = new MySQLiteFunctions(this);
      final Spinner spnAmnt = (Spinner) findViewById(R.id.spn_amount);
      final EditText txtAmount = (EditText) findViewById(R.id.txt_amount);
      final EditText txtMeterNumber = (EditText) findViewById(R.id.meter_number);
      Button btnNxt = (Button) findViewById(R.id.btn_next);
    meterNumberspn = (Spinner)findViewById(R.id.meterNumber);
setSpinners();
    findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
        public void onClick(View paramAnonymousView) {
            Intent intent = new Intent(AccountDetailsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    });

    //txtMeterNumber.setText(this.mysqliteFunction.getMeterNumber());
      ArrayAdapter<CharSequence> adapterAmnt = ArrayAdapter
              .createFromResource(this, R.array.amount_array,
                      android.R.layout.simple_spinner_item);
      adapterAmnt
              .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spnAmnt.setAdapter(adapterAmnt);
   spnAmnt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
           switch (paramAnonymousInt) {
               case 5:
                   spnAmnt.setVisibility(View.GONE);
                   txtAmount.setVisibility(View.VISIBLE);
                   break;
               default:
                   AccountDetailsActivity.this.amount = spnAmnt.getSelectedItem().toString().substring(1);
                   break;

           }

       }

       public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
       }
   });
      String curMeterNumber = mysqliteFunction.getMeterNumber();
      if (curMeterNumber.length() < 9){
          ShowMeterError();
      }
    btnNxt.setOnClickListener(new View.OnClickListener()
    {

      public void onClick(View paramAnonymousView) {

          if (savedMeters  == null) {
            ShowMeterError();

          } else {
              if (txtAmount.getVisibility() == View.VISIBLE) {
                  AccountDetailsActivity.this.amount = txtAmount.getText().toString();
                  if (AccountDetailsActivity.this.amount.isEmpty()) {
                      txtAmount.setError("Please enter an amount less or equal to R1000");
                      return;
                  }
                  if (Integer.parseInt(AccountDetailsActivity.this.amount) > 1000) {
                      txtAmount.setError("amount must be less or equal to R1000");
                      return;
                  }
              }
              if ((AccountDetailsActivity.this.amount.equals("Select")) || (AccountDetailsActivity.this.amount.equals("")) || (AccountDetailsActivity.this.amount.equals(null))) {
                  new AccountDetailsActivity.ErrorMsgDialog().show(AccountDetailsActivity.this.getFragmentManager(), null);
                  return;
              }
              String str = savedMeters;
              Log.d(LOG, "Selected meter number " + str);
              //  Intent i = new Intent(AccountDetailsActivity.this, PaymentWebView.class);
              // AccountDetailsActivity.this.startActivity(i);
              // overridePendingTransition(R.anim.from, R.anim.to);

              Intent localIntent1 = new Intent(AccountDetailsActivity.this, Eleconfirm.class);
              localIntent1.putExtra("amount", AccountDetailsActivity.this.amount);
              localIntent1.putExtra("isNew", false);
              localIntent1.putExtra("meter_number", str);
              AccountDetailsActivity.this.startActivity(localIntent1);
              overridePendingTransition(R.anim.from, R.anim.to);

          }
      }
    });
  }


    private  void ShowMeterError()
    {

        LayoutInflater inflater = LayoutInflater.from(AccountDetailsActivity.this);
        View promptView = inflater.inflate(R.layout.meter_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailsActivity.this);
        builder.setView(promptView);
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
                Intent intent = new Intent(getApplicationContext(), UpdateMeterNumber.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });


    }


    private void setSpinners() {

        mysqliteFunction.getAllMeterNumbers();
        mmeters = mysqliteFunction.getAllM();

        Log.d(LOG, "All meter numbers " + mmeters);
        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0;i < mmeters.length;i++){
            Log.e(LOG,"nuuuuu"+mmeters[i].length());
            if (mmeters[i].length() > 9) {
                tmp.add(mmeters[i]);
            }
        }

        tmp.removeAll(Collections.singleton(null));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_custom_spinner, tmp);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            meterNumberspn.setAdapter(adapter);


        num = 0;
        meterNumberspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                savedMeters = meterNumberspn.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

  public void getAllMeters(){
    Cursor localCursor = this.mysqliteFunction.getAllMeterNumbers();

    if((localCursor != null) && localCursor.moveToFirst()){

      do {

        localCursor.getString(1);
        //Log.d(LOG, "All meter numbers " +  );
      }while (localCursor.moveToNext());

    }else {
      new RegistrationActivity.MsgDialog().show(getFragmentManager(),null);
    }

    this.mysqliteFunction.close();
  }

  public static class ErrorMsgDialog extends DialogFragment
  {
    public Dialog onCreateDialog(Bundle paramBundle)
    {
      AlertDialog.Builder locaBuilder = new AlertDialog.Builder(getActivity());
      locaBuilder.setMessage("please select an amount").setCancelable(false).setTitle(getResources().getString(R.string.app_name)).setPositiveButton("ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
        }
      });
      return locaBuilder.create();
    }
  }
}