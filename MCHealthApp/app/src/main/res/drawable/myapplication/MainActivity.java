package drawable.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by kapish on 20-08-2015.
 */
public class ShowDetails extends Activity {
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("INFO", "State of Activity 2<ShowDetails> changed from " + status + " to Restarted");
        status="Restarted";
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("INFO", "State of Activity 2<ShowDetails> changed from " + status + " to Resumed");
        status="Resumed";
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("INFO","State of Activity 2<ShowDetails> changed from "+status+" to Paused");
        status="Paused";
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("INFO","State of Activity 2<ShowDetails> changed from "+status+" to Stopped");
        status="Stopped";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("INFO", "State of Activity 2<ShowDetails> changed from " + status + " to Destroyed");
        status="Destroyed";
    }
}
