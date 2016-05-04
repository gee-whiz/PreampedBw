package za.co.empirestate.botspost.preamped;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.empirestate.botspost.sqlite.MySQLiteFunctions;


/**
 * A simple {@link Fragment} subclass.
 */
public class AirtimeFragment extends android.app.Fragment {

    private static final String LOG = "hey gee";
    private static final String TAG = "hey Gee";
    RecyclerView airtimeList, electList, pobList;
    View backButton;
    String email, phone, newPhone;
    Context ctx;
    ImageButton backImage;
    Context mContext;
    CardView cardView;
    TextView pleaseWait;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ProgressDialog pDialog;
    private MySQLiteFunctions mysqliteFunction;
    private AirtimeAdapter airtimeAdapter;
    private List<History> AirtimeList = new ArrayList<History>();
    private View rootView;

    public AirtimeFragment() {
        // Required empty public constructor
    }


    public static AirtimeFragment newInstance(int pos) {
        AirtimeFragment fragment = new AirtimeFragment();
        Bundle args = new Bundle();
        args.putInt("kapoya", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_airtime, container, false);
        ctx = getActivity().getApplicationContext();
        mysqliteFunction = new MySQLiteFunctions(ctx);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        email = mysqliteFunction.getEmail();
        phone = mysqliteFunction.getPhone();
        newPhone = phone.substring(1);
        AirtimeList.clear();
        if (AirtimeList.isEmpty()) {
            pDialog.show();

        }
        getTransactionHistory(email, newPhone, "Airtime");
        electList = (RecyclerView) rootView.findViewById(R.id.rvaAirtimeList);
        LinearLayoutManager llm = new LinearLayoutManager(ctx);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        electList.setLayoutManager(llm);
        setupSwipeRefreshLayout();
        airtimeAdapter = new AirtimeAdapter(ctx, AirtimeList);
        electList.setAdapter(airtimeAdapter);
        electList.setHasFixedSize(true);
        return rootView;
    }


    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                AirtimeList.clear();
                getTransactionHistory(email, newPhone, "Airtime");
                airtimeAdapter.notifyDataSetChanged();

            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    //get transaction history from the database
    public void getTransactionHistory(final String email, final String phone, final String reportType) {
        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                AppConfig.URL_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(LOG, "transaction history response " + response.toString());
                int size = response.length();
                for (int i = 0; i < 100; i++) {
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        History history = new History();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String hId = jsonObject.getString("id");
                        String  hAmount = jsonObject.getString("Amount");
                        String hRef = jsonObject.getString("Ref");
                        String hDateTime = jsonObject.getString("tranDateTime");

                        history.sethAmount(hAmount);
                        history.sethId(hId);
                        history.sethRev(hRef);
                        history.sethTranDateTime(hDateTime);
                        AirtimeList.add(history);


                    } catch (JSONException e) {
                    }
                }
                airtimeAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                pDialog.dismiss();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("phone", phone);
                params.put("reportType", reportType);
                Log.d(LOG, "values sent from the device  " + params);
                return params;
            }

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);


    }

}