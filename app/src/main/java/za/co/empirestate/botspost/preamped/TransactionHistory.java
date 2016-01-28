package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;


public class TransactionHistory extends Activity {
    private MySQLiteFunctions mysqliteFunction;
    TextView tab1,tab2,tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        TableLayout tblLay = (TableLayout) findViewById(R.id.all_trans);

        setFields();
        setTabs();

        this.mysqliteFunction = new MySQLiteFunctions(this);



        ((ImageButton)findViewById(R.id.bck_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                onBackPressed();
                finish();
            }
        });

        TableLayout.LayoutParams tblLayParms = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams tblRowlayParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams tblRowlayParams2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        TableRow.LayoutParams tblRowlayParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Cursor localCursor = this.mysqliteFunction.getTransactionHistory();

        if((localCursor != null) && localCursor.moveToFirst()){

           do {
                TableRow tblRw = new TableRow(TransactionHistory.this);
               // tblRw.setLayoutParams(tblLayParms);
                tblLay.addView(tblRw,tblLayParms);

                TextView txtRef = new TextView(TransactionHistory.this);
                txtRef.setText(localCursor.getString(0));

                TextView txtAmnt = new TextView(TransactionHistory.this);
                   tblRowlayParams3.setMargins(5,5,5,5);
                txtAmnt.setText("   "+localCursor.getString(2));
            TextView txtToken = new TextView(TransactionHistory.this);
                if(localCursor.getString(1).length() == 20){
                    txtToken.setText(localCursor.getString(1).substring(0,4)+" "+localCursor.getString(1).substring(4,8)+" "+localCursor.getString(1).substring(8,12) +"\n" +"     "+localCursor.getString(1).substring(12,16) +" "+localCursor.getString(1).substring(16,20));
                }else{
                    txtToken.setText(localCursor.getString(1));
                }
               TextView txtDateTime = new TextView(TransactionHistory.this);
               txtDateTime.setText("   "+localCursor.getString(3) + "\n" +"   "+localCursor.getString(4));
           // Log.e("TOKEN",localCursor.getString(5));
              //  TextView txtMeterNumber = new TextView(TransactionHistory.this);
               // txtMeterNumber.setText(localCursor.getString(1));

             //   TextView txtUnits = new TextView(TransactionHistory.this);
              //  txtUnits.setText(localCursor.getString(4));

            View view = new View(this);
            view.setLayoutParams(tblRowlayParams2);
            view.setBackgroundColor(Color.parseColor("#000000"));

                tblRw.addView(txtRef,tblRowlayParams);
                tblRw.addView(txtAmnt,tblRowlayParams3);
                tblRw.addView(txtToken,tblRowlayParams);
               tblRw.addView(txtDateTime,tblRowlayParams);
                tblLay.addView(view);
              //  tblRw.addView(txtMeterNumber,tblRowlayParams);
                //tblRw.addView(txtUnits,tblRowlayParams);

            }while (localCursor.moveToNext());

        }else {
            new MsgDialog().show(getFragmentManager(),null);
        }

        this.mysqliteFunction.close();
    }

    public static class MsgDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstance) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("No transactions found")
                    .setCancelable(false)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    // TODO Auto-generated method stub
                                    Intent i = new Intent(getActivity(),
                                            MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                }
                            });
            return builder.create();
        }
    }


    public  void  setTabs(){

        tab1.setBackgroundResource(R.drawable.red_back);
        tab2.setBackgroundResource(R.drawable.yellow_back);
        tab3.setBackgroundResource(R.drawable.yellow_back);
        tab1.setTextColor(getResources().getColor(R.color.white));
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab2.setBackgroundResource(R.drawable.yellow_back);
                tab3.setBackgroundResource(R.drawable.yellow_back);
                tab1.setBackgroundResource(R.drawable.red_back);
                tab1.setTextColor(getResources().getColor(R.color.white));
                tab2.setTextColor(getResources().getColor(R.color.black));
                tab3.setTextColor(getResources().getColor(R.color.black));

            }
        });
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab1.setBackgroundResource(R.drawable.yellow_back);
                tab3.setBackgroundResource(R.drawable.yellow_back);
                tab2.setBackgroundResource(R.drawable.red_back);
                tab2.setTextColor(getResources().getColor(R.color.white));
                tab1.setTextColor(getResources().getColor(R.color.black));
                tab3.setTextColor(getResources().getColor(R.color.black));

            }
        });
        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tab2.setBackgroundResource(R.drawable.yellow_back);
                tab1.setBackgroundResource(R.drawable.yellow_back);
                tab3.setBackgroundResource(R.drawable.red_back);
                tab3.setTextColor(getResources().getColor(R.color.white));
                tab1.setTextColor(getResources().getColor(R.color.black));
                tab2.setTextColor(getResources().getColor(R.color.black));


            }
        });

    }


    public  void setFields(){
        tab1 = (TextView)findViewById(R.id.txt_top_tab1);
        tab2 = (TextView)findViewById(R.id.txt_top_tab2);
        tab3 = (TextView)findViewById(R.id.txt_top_tab3);

    }
}
