package uk.co.rossanthony.noiselogger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.app.IntentService;
import android.content.Intent;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;




public class RecorderService extends IntentService {

    private MediaRecorder recorder = null;
    private String IMEI = null;
    private LocationManager lm;
    private Handler mHandler;
    private int amp;
    private TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    public RecorderService() {
        super("RecorderService");
        mHandler = new Handler();
        IMEI = telephonyManager.getDeviceId();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(checkCon()) {
            Log.i("RecorderService", "RecorderService is running...");

            // Start recording audio
            startRecording();

            // First call to getMaxAmplitude is always zero
            amp = recorder.getMaxAmplitude();

            // Halt for 1 sec, to allow enough sound to be captured for getMaxAmplitude to return a useful value
            SystemClock.sleep(1000);
            // Obviously not ideal to sleep a process like this, but since this is a background service
            // and is therefore running as a separate thread it won't block the UI of the app itself.

            amp = recorder.getMaxAmplitude();
            Log.i("getMaxAmplitude", "amp: " + amp);

            // Stop recording sound, to save resources
            stopRecording();

            // Display popup message in the app UI as notification of last sound sample taken
            mHandler.post(new DisplayToast(this, "Last Max Amplitude: " + amp));

            // Send the sample data to the API
            sendDataToApi(amp);
        }
    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");
        try {
            recorder.prepare();
        } catch (Exception e) {
            Log.e("prepare", "exception", e);
        }
        try {
            recorder.start();
        } catch (Exception e) {
            Log.e("start", "exception", e);
        }
    }

    public void stopRecording() {
        recorder.stop();
        recorder.reset();
        recorder.release();
    }

    /**
     * Send data off to the thingspeak API
     * @param amp
     */
    private void sendDataToApi(int amp) {

        String postParams = "api_key=8DOZPI1LTOFZ0PSZ&field1=" + amp
                            + getLocation()
                            + "&field4=" + IMEI;

        Log.i("postParameters", postParams);

        try{
            URL urlToRequest = new URL("https://api.thingspeak.com/update");
            try{
                HttpURLConnection conn = (HttpURLConnection) urlToRequest.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setFixedLengthStreamingMode(postParams.getBytes().length);
                OutputStream out = conn.getOutputStream();
                out.write(postParams.getBytes("UTF8"));
                int http_status = conn.getResponseCode();
                out.close();
                Log.i("openConnection", "Response: " + http_status);
                conn.disconnect();

            }catch(IOException e){
                Log.i("openConnection", "Failed");
            }
        }catch(MalformedURLException e){
            Log.i("new URL failed", "For: https://api.thingspeak.com/update");
        }
    }

    /**
     * Get the location
     * Based on method here: http://www.coders-hub.com/2013/10/android-location-address-and-distance.html
     *
     * @return String of location parameters or empty string if location unavailable (i.e. GPS off and no Network)
     */
    private String getLocation(){

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        String provider = lm.getBestProvider(c, false);
        Location l = lm.getLastKnownLocation(provider);

        if(l!=null)
        {
            double lng = l.getLongitude();
            double lat = l.getLatitude();
            return "&field2=" + lat + "&field3=" + lng;
        }
        else
        {
            return "";
        }
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