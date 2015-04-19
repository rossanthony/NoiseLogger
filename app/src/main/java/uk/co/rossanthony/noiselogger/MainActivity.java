/**
 * @package uk.co.rossanthony.noiselogger
 * @class   MainActivity
 * @author  Ross Anthony
 * @version 1.0
 */
package uk.co.rossanthony.noiselogger;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

public class MainActivity extends ActionBarActivity implements OnCheckedChangeListener {

    private int loggerInterval = 15000; // 15 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch s = (Switch) findViewById(R.id.logger_switch);

        if (s != null) {
            s.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "Logging is " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();

        if(!this.checkCon()) {
            // Not connected to the Net, therefore cannot run the logger as data cannot be posted to API
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }else if(isChecked) {
            scheduleAlarm();
        } else {
            cancelAlarm();
        }
    }

    /**
     * Alarm scheduler - triggers background process to run
     *
     * Code taken from example here: https://guides.codepath.com/android/Starting-Background-Services
     */
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 15 seconds
        long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, loggerInterval, pIntent);
    }

    /**
     * Kill the background process
     *
     * Code taken from example here: https://guides.codepath.com/android/Starting-Background-Services
     */
    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Check if the phone is connected to the internet
     * Code from: http://stackoverflow.com/questions/13344031/repeat-a-task-for-every-10-seconds-using-threads-and-services-in-android#answer-13344179
     * @return
     */
    private boolean checkCon() {
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                &&  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return true;
    }
}
