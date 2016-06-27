package za.co.empirestate.botspost.preamped;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
     Context ctx;
    Button elec,air;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ctx = getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.redBorderColor));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         elec = (Button)findViewById(R.id.btn_buy);
        air  = (Button)findViewById(R.id.btn_airtime);

        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,
                        AccountDetailsActivity.class);
                startActivity(intent);
            }
        });


        air.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,AirtimeActivity.class);
                startActivity(intent);
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new LogoutMsgDialog().show(getFragmentManager(),null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.buy_elec) {
            Intent intent = new Intent(ctx,
                    AccountDetailsActivity.class);
            startActivity(intent);
        } else if (id == R.id.buy_airtime) {
            Intent intent = new Intent(ctx,AirtimeActivity.class);
            startActivity(intent);

        } else if (id == R.id.renew) {
            Intent intent = new Intent(ctx,RenewPoBox.class);
            startActivity(intent);

        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ctx,
                    NewTransactionHistory.class);
            startActivity(intent);

        } else if (id == R.id.settings) {
            Intent intent = new Intent(ctx,SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.contact_us) {
            Intent intent = new Intent(ctx,
                    ContactUsActivity.class);
            startActivity(intent);
        }
       else if (id == R.id.terms) {
            Intent intent = new Intent(ctx,
                    TermsAndConditions.class);
            startActivity(intent);
         }

        else if (id == R.id.log_out) {

            new LogoutMsgDialog().show(getFragmentManager(), null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static class LogoutMsgDialog extends DialogFragment
    {
        public Dialog onCreateDialog(Bundle paramBundle)
        {
            AlertDialog.Builder locaBuilder = new AlertDialog.Builder(getActivity());
            locaBuilder.setMessage("Are you sure you want to log out?").setCancelable(false).setTitle(getResources().getString(R.string.app_name)).setPositiveButton("yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
                {
                    startActivity(new Intent(getActivity(),RegOrLogActivity.class));
                    getActivity().finish();
                }
            }).setNegativeButton("no",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return locaBuilder.create();
        }
    }
}
