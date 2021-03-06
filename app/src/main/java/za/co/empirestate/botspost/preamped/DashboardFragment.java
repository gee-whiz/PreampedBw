package za.co.empirestate.botspost.preamped;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DashboardFragment extends Fragment implements View.OnClickListener
{

    public DashboardFragment () {
    }

    public static DashboardFragment  newInstance() {
        DashboardFragment  fragment = new DashboardFragment ();
        return fragment;
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dashboard, container,
                false);

        Button btnBuy = (Button) rootView.findViewById(R.id.btn_buy);
       Button  btnAirtime  = (Button)rootView.findViewById(R.id.btn_airtime);

        btnBuy.setOnClickListener(this);
        btnAirtime.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_buy){
            Intent localIntent = new Intent(getActivity(), AccountDetailsActivity.class);
            startActivity(localIntent);

        }
        else
        if (v.getId() == R.id.btn_airtime){
            Intent localIntent = new Intent(getActivity(), AirtimeActivity.class);
            startActivity(localIntent);

        }
    }
}