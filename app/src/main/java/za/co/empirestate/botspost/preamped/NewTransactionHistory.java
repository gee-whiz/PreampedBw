package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;

public class NewTransactionHistory extends Activity {
    private static final String LOG = "hey gee";
    private static final String TAG = "hey Gee";
    TextView tab1,tab2,tab3;
    View backButton;
    String email,phone,newPhone;
    Context ctx;
    ImageButton backImage;
    CardView cardView;
    private ProgressDialog pDialog;
    private MySQLiteFunctions mysqliteFunction;
    private AirtimeFragment airtimeFragment;
    private PoFragment poFragment;
    private ElecFragment elecFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction_history);
        this.mysqliteFunction = new MySQLiteFunctions(this);
        cardView = (CardView)findViewById(R.id.card_view);
        setFields();
        setTabs();
        email = mysqliteFunction.getEmail();
        phone = mysqliteFunction.getPhone();
        newPhone = phone.substring(1);
        elecFragment = new ElecFragment();
        airtimeFragment = new AirtimeFragment();
       poFragment = new PoFragment();
     getFragmentManager().beginTransaction()
                .replace(R.id.electView,elecFragment)
                .commit();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
                getFragmentManager().beginTransaction()
                        .replace(R.id.electView,elecFragment)
                        .commit();
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
                getFragmentManager().beginTransaction()
                        .replace(R.id.electView,airtimeFragment)
                        .commit();


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
                getFragmentManager().beginTransaction()
                        .replace(R.id.electView,poFragment)
                        .commit();

            }
        });

    }

    public  void setFields(){
        tab1 = (TextView)findViewById(R.id.txt_top_tab1);
        tab2 = (TextView)findViewById(R.id.txt_top_tab2);
        tab3 = (TextView)findViewById(R.id.txt_top_tab3);
        backButton = findViewById(R.id.btnBack);
        backImage =  (ImageButton)findViewById(R.id.imgBack);
    }


}
