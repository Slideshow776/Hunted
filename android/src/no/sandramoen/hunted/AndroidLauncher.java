package no.sandramoen.hunted;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.Locale;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        initialize(new HuntedGame(null, getAppLocale()), config);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private String getAppLocale() {
        try {
            if (getResources().getConfiguration().getLocales().get(0).toString().equals("nb_NO") || getResources().getConfiguration().getLocales().get(0).toString().equals("nn_NO"))
                return "no";
            if (Locale.getDefault().toString().equals("nb_NO") || Locale.getDefault().toString().equals("nn_NO"))
                return "no";
            if (Resources.getSystem().getConfiguration().locale.toString().equals("nb_NO") || Resources.getSystem().getConfiguration().locale.toString().equals("nn_NO"))
                return "no";
        } catch (Exception e) {
            System.err.println("Error setting up locale in AndroidLauncher.java: " + e);
        }
        return "en";
    }
}
