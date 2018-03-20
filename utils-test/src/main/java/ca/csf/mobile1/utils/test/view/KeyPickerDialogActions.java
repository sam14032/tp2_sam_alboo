package ca.csf.mobile1.utils.test.view;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import ca.csf.mobile1.utils.R;
import ca.csf.mobile1.utils.view.KeyPickerDialog;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Lot de « ViewActons » pour des tests Espresso affectant les {@link KeyPickerDialog}.
 */
public final class KeyPickerDialogActions {

    /**
     * ViewAction modifiant la valeur du {@link KeyPickerDialog}.
     * @param keyValue Nouvelle valeur à saisir dans le {@link KeyPickerDialog}
     * @return ViewAction correspondant.
     */
    public static ViewAction setValue(final int keyValue) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "change key to " + keyValue;
            }

            @Override
            public void perform(UiController uiController, View view) {
                KeyPickerDialog keyPickerDialog = (KeyPickerDialog) view.getTag(R.id.keyPickerDialogTag);
                keyPickerDialog.setKey(keyValue);
            }
        };
    }

    /**
     * ViewAction cliquant sur le bouton "OK" d'un {@link KeyPickerDialog}.
     * @return ViewAction correspondant.
     */
    public static ViewAction ok() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "performed OK";
            }

            @Override
            public void perform(UiController uiController, View view) {
                KeyPickerDialog keyPickerDialog = (KeyPickerDialog) view.getTag(R.id.keyPickerDialogTag);
                keyPickerDialog.performOk();
            }
        };
    }

    /**
     * ViewAction cliquant sur le bouton "Annuler" d'un {@link KeyPickerDialog}.
     * @return ViewAction correspondant.
     */
    public static ViewAction cancel() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "performed Cancel";
            }

            @Override
            public void perform(UiController uiController, View view) {
                KeyPickerDialog keyPickerDialog = (KeyPickerDialog) view.getTag(R.id.keyPickerDialogTag);
                keyPickerDialog.performCancel();
            }
        };
    }

}
