package ca.csf.mobile1.utils.view;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.csf.mobile1.utils.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class KeyPickerDialogTest {

    @Rule
    public final ActivityTestRule<AppCompatActivity> activityRule = new ActivityTestRule<>(AppCompatActivity.class);

    private KeyPickerDialog.ConfirmListener confirmListener;
    private KeyPickerDialog.CancelListener cancelListener;

    @Before
    public void before() {
        confirmListener = mock(KeyPickerDialog.ConfirmListener.class);
        cancelListener = mock(KeyPickerDialog.CancelListener.class);
    }


    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateDialogWithNullContext() {
        KeyPickerDialog.make(null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateDialogWithZeroLengthKey() {
        KeyPickerDialog.make(activityRule.getActivity(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateDialogWithLessThanZeroLengthKey() {
        KeyPickerDialog.make(activityRule.getActivity(), -1);
    }

    @Test
    public void canOpenDialog() throws Throwable {
        checkDialogIsNotShown();

        show(0);

        checkDialogIsShown();
    }

    @Test
    public void canPerformOkOnDialog() throws Throwable {
        show(0);

        pressOkButton();

        checkDialogIsNotShown();
        checkConfirmListenerIsCalled(0);
    }

    @Test
    public void canPerformCancelOnDialog() throws Throwable {
        show(0);

        pressCancelButton();

        checkDialogIsNotShown();
        checkCancelListenerIsCalled();
    }

    @Test
    public void canSetKeyOnDialog() throws Throwable {
        show(15124);

        pressOkButton();

        checkDialogIsNotShown();
        checkConfirmListenerIsCalled(15124);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotSetNegativeKey() throws Throwable {
        KeyPickerDialog.make(activityRule.getActivity(), 5)
                       .setKey(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotSetKeyLargerThanItsSize() throws Throwable {
        KeyPickerDialog.make(activityRule.getActivity(), 5)
                       .setKey(100000);
    }

    //region Test tools

    private void show(int key) throws Throwable {
        activityRule.runOnUiThread(() -> KeyPickerDialog.make(activityRule.getActivity(), 5)
                                                        .setConfirmAction(confirmListener)
                                                        .setCancelAction(cancelListener)
                                                        .setKey(key)
                                                        .show());
    }

    private void pressOkButton() {
        onView(withId(android.R.id.button1)).perform(click());
    }

    private void pressCancelButton() {
        onView(withId(android.R.id.button2)).perform(click());
    }

    private void checkDialogIsNotShown() {
        onView(ViewMatchers.withId(R.id.keyPickerDialog)).check(doesNotExist());
    }

    private void checkDialogIsShown() {
        onView(ViewMatchers.withId(R.id.keyPickerDialog)).check(matches(isDisplayed()));
    }

    private void checkConfirmListenerIsCalled(int key) {
        verify(confirmListener).onKeyPickerDialogConfirm(eq(key));
    }

    private void checkCancelListenerIsCalled() {
        verify(cancelListener).onKeyPickerDialogCancel();
    }

    //endregion

}