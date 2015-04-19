package uk.co.rossanthony.noiselogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Triggered by the Alarm periodically (starts the service to run task)
        context.startService(new Intent(context, RecorderService.class));
    }

}
