package ca.csf.mobile1.tests.activity;

import android.content.Intent;
import android.provider.Settings;

import org.hamcrest.Matcher;

import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * Lot de « IntentMatcher » pour des tests Espresso.
 */
public class IntentActions {

    /**
     * Match un intent vers les Settings Wifi de l'appareil.
     * @return IntentMatcher correspondant.
     */
    public static Matcher<Intent> toWifiSettingsActivity() {
        return hasAction(Settings.ACTION_WIFI_SETTINGS);
    }

}