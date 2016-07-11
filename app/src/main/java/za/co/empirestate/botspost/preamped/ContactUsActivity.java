package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class ContactUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                onBackPressed();
            }
        });
    }

}

