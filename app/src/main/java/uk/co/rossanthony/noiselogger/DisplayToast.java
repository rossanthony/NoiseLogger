/**
 * @package uk.co.rossanthony.noiselogger
 * @class   DisplayToast
 * @author  Ross Anthony
 * @version 1.0
 */
package uk.co.rossanthony.noiselogger;

import android.widget.Toast;
import android.content.Context;

/**
 * Code pulled from: http://stackoverflow.com/questions/3955410/create-toast-from-intentservice/3955826#3955826
 */
public class DisplayToast implements Runnable {
    private final Context mContext;
    String mText;

    public DisplayToast(Context mContext, String text){
        this.mContext = mContext;
        mText = text;
    }

    public void run(){
        Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
    }
}