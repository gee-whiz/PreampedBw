package za.co.empirestate.botspost.preamped;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class ContactUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ((ImageButton)findViewById(R.id.bck_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                onBackPressed();
            }
        });
    }

}

