package za.co.empirestate.botspost.preamped;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;
import java.util.Locale;

/**
 * Created by George_Kapoya on 16/01/25.
 */
public class AirtimeAdapter extends  RecyclerView.Adapter<AirtimeAdapter.AirtimeViewHolder> implements View.OnClickListener  {

    private static final String LOG = "hey Gee" ;
    private List<History> historyList;
    private Context context;
    private Context mContext;
    private ImageLoader mImageLoader;
    public AirtimeAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
        mImageLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new LruBitmapCache());
    }

    @Override
    public AirtimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.airtime_item, viewGroup, false);
        final AirtimeViewHolder airtimeViewHolder= new AirtimeViewHolder(itemView);
        return airtimeViewHolder;
    }

    @Override
    public void onBindViewHolder(AirtimeViewHolder holder, int position) {
        try {
        History history = historyList.get(position);


            String   li_amount = history.gethAmount();

          Double ls_amount = Double.parseDouble(li_amount) / 100;

            holder.amount.setText("P" + String.format(Locale.ENGLISH ,"%.2f",ls_amount) );
            holder.token.setText(history.gethId());
            holder.dateTime.setText(history.gethTranDateTime());
            holder.ref.setText(history.gethRev());
        } catch (Exception e) {
            Log.d(LOG, "Failed to do something: " + e.getMessage());
        }
        String li_amount;
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onClick(View view) {

    }
    //set fields
    public class AirtimeViewHolder extends RecyclerView.ViewHolder {
        protected TextView ref,amount,dateTime,token;
        public AirtimeViewHolder(View itemView) {
            super(itemView);
            ref      = (TextView)itemView.findViewById(R.id.txtRef);
            amount   = (TextView)itemView.findViewById(R.id.txtAmount);
            dateTime = (TextView)itemView.findViewById(R.id.txtTime);
            token    = (TextView)itemView.findViewById(R.id.txtToken);
        }
    }
}
