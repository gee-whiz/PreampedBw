package za.co.empirestate.botspost.preamped;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by joel on 15/04/23.
 */
public class PendingActivityService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PendingActivityService() {
        super("PendingActivityService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
