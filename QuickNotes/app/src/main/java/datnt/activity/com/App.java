package datnt.activity.com;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Admin on 11/13/2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
