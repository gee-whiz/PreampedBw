package za.co.empirestate.botspost.preamped;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by George_Kapoya on 16/01/25.
 */
public class AirtimeAdapter extends  RecyclerView.Adapter<AirtimeAdapter.AirtimeViewHolder> implements View.OnClickListener  {

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

        History history = historyList.get(position);
        holder.amount.setText("P"+history.gethAmount());
        holder.token.setText(history.gethId());
        holder.dateTime.setText(history.gethTranDateTime());
        holder.ref.setText(history.gethRev());

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
