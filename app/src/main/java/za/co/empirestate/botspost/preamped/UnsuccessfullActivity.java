package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class UnsuccessfullActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsuccessfull);

        TextView txtReason = (TextView) findViewById(R.id.reason);
        Button btnFinish = (Button)findViewById(R.id.btn_finish);
        Intent intent = getIntent();
        String errorMsg = intent.getStringExtra("error_msg");
        txtReason.setText(errorMsg);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UnsuccessfullActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
       // super.onBackPressed();
        Intent i = new Intent(UnsuccessfullActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}
