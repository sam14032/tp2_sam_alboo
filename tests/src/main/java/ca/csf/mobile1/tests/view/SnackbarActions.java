package ca.csf.mobile1.tests.view;

import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Lot de « ViewMatches » pour des tests Espresso visant spécifiquement {@link android.support.design.widget.Snackbar Snackbar}.
 */
public final class SnackbarActions {

    /**
     * ViewMatcher pour le {@link android.widget.TextView TextView} dans un {@link android.support.design.widget.Snackbar Snackbar}.
     * @return ViewMatcher correspondant.
     */
    public static Matcher<View> snackbar() {
        return withId(android.support.design.R.id.snackbar_text);
    }

    /**
     * ViewMatcher pour le {@link android.widget.Button Button} dans un {@link android.support.design.widget.Snackbar Snackbar}.
     * @return ViewMatcher correspondant.
     */
    public static Matcher<View> snackbarButton() {
        return withId(android.support.design.R.id.snackbar_action);
    }

}
