package ca.csf.mobile1.tests.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;


/**
 * Lot de « ViewActons » pour des tests Espresso affectant l'orientation de l'écran.
 */
public final class OrientationChangeActions {

    /**
     * Tourne l'activité en mode paysage.
     * @param activity Activité à tourner.
     */
    public static void changeOrientationToLandscapeOn(Activity activity) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        closeSoftKeyboard();
    }

    /**
     * Tourne l'activité en mode portrait.
     * @param activity Activité à tourner.
     */
    public static void changeOrientationToPortraitOn(Activity activity) {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        closeSoftKeyboard();
    }

}
