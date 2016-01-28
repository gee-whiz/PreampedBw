package za.co.empirestate.botspost.preamped;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by joel on 15/02/26.
 */
public class ConfirmTransactionService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ConfirmTransactionService() {
        super("ConfirmTransactionService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String ref = intent.getStringExtra("reference");

        new UpdateTransactionTask().execute(ref);
    }


    private class UpdateTransactionTask extends AsyncTask<String,Void,String> {
        String resp;

        @Override
        protected String doInBackground(String... params) {

            Request request = new Request();
            resp = request.addParamatersToConfirmTrans(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            resp = this.resp;
            if(resp.equals("Invalid server response")){
                Toast.makeText(getApplicationContext(), "error while updating transaction",
                        Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), resp,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO do something useful
        Toast.makeText(getApplicationContext(), "updating transaction...",
                Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);

    }
}
