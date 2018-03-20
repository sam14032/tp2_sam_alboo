package ca.csf.mobile1.tp2.activity;

import android.app.Instrumentation;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.csf.mobile1.tp2.R;
import ca.csf.mobile1.tp2.application.Tp2Application;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.csf.mobile1.tests.activity.IntentActions.toWifiSettingsActivity;
import static ca.csf.mobile1.tests.activity.OrientationChangeActions.changeOrientationToLandscapeOn;
import static ca.csf.mobile1.tests.activity.OrientationChangeActions.changeOrientationToPortraitOn;
import static ca.csf.mobile1.tests.application.ApplicationActions.theApplication;
import static ca.csf.mobile1.tests.service.OkHttpClientActions.okHttpClientThatDoConnectivityError;
import static ca.csf.mobile1.tests.service.OkHttpClientActions.okHttpClientThatDoServerError;
import static ca.csf.mobile1.tests.view.SnackbarActions.snackbar;
import static ca.csf.mobile1.tests.view.SnackbarActions.snackbarButton;
import static ca.csf.mobile1.utils.test.view.KeyPickerDialogActions.cancel;
import static ca.csf.mobile1.utils.test.view.KeyPickerDialogActions.ok;
import static ca.csf.mobile1.utils.test.view.KeyPickerDialogActions.setValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void canSeeKeyId() {
        show();

        changeKeyIdTo(11977);

        checkKeyIdIs(11977);
    }

    @Test
    public void canChangeKeyId() {
        show();

        openKeyPickerDialog();
        typeInPickerDialog(11977);
        closeKeyPickerDialog();

        checkKeyIdIs(11977);
    }

    @Test
    public void canEncryptText() {
        show();

        changeKeyIdTo(15547);
        changeInputTextTo("Bonjour madame.");
        pressEncryptButton();

        checkOutputIs("uGqvGhLUSNaNSIi");
    }

    @Test
    public void canDecryptText() {
        show();

        changeKeyIdTo(15547);
        changeInputTextTo("uGqvGhLUSNaNSIi");
        pressDecryptButton();

        checkOutputIs("Bonjour madame.");
    }

    @Test
    public void canCopyOutputTextToClipboard() {
        show();

        changeKeyIdTo(15547);
        changeInputTextTo("Bonjour madame.");
        pressEncryptButton();
        pressCopyButton();

        checkClipboardIs("uGqvGhLUSNaNSIi");
        checkSnackbarMessageIs(R.string.text_copied_output);
    }

    @Test
    public void keyPickerDialogIsShownWhenReceivingTextFromOtherApplication() {
        show("uGqvGhLUSNaNSIi");

        checkKeyPickerDialogIsOpen();
    }

    @Test
    public void canEncryptTextFromOtherApplication() {
        show("Bonjour madame.");

        //KeyPickerDialog show be opened already. No need to open it.
        typeInPickerDialog(15547);
        closeKeyPickerDialog();
        pressEncryptButton();

        checkOutputIs("uGqvGhLUSNaNSIi");
    }

    @Test
    public void canDecryptTextFromOtherApplication() {
        show("uGqvGhLUSNaNSIi");

        //KeyPickerDialog show be opened already. No need to open it.
        typeInPickerDialog(15547);
        closeKeyPickerDialog();
        pressDecryptButton();

        checkOutputIs("Bonjour madame.");
    }

    @Test
    public void cantTypeUnsupportedCharacters() {
        show();

        changeInputTextTo("?@!A%$&l#<)l(,:o-;_");

        checkInputIs("Allo");
    }

    @Test
    public void orientationChangeKeepsData() {
        show();

        changeKeyIdTo(15547);
        changeInputTextTo("Bonjour madame.");
        pressEncryptButton();

        rotateToLandscape();
        checkKeyIdIs(15547);
        checkInputIs("Bonjour madame.");
        checkOutputIs("uGqvGhLUSNaNSIi");

        rotateToPortrait();
        checkKeyIdIs(15547);
        checkInputIs("Bonjour madame.");
        checkOutputIs("uGqvGhLUSNaNSIi");
    }

    @Test
    public void orientationChangeKeepsKeyPickerDialogOpen() {
        show();

        openKeyPickerDialog();

        rotateToLandscape();
        checkKeyPickerDialogIsOpen();

        rotateToPortrait();
        checkKeyPickerDialogIsOpen();
    }

    @Test
    public void showConnectivityErrorMessageIfNoWifi() {
        showWithNoWifi();

        checkSnackbarMessageIs(R.string.text_connectivity_error);
    }

    @Test
    public void showServerErrorMessageIfServerResponseIsInvalid() {
        showWithServerDown();

        checkSnackbarMessageIs(R.string.text_server_error);
    }

    @Test
    public void pressingActivateWifiButtonOpenWifiSettings() {
        showWithNoWifi();

        pressActivateWifiButton();

        checkWifiSettingsIsOpen();
    }

    @Test
    public void whenReceiveTextFromOtherApplicationIfKeyPickerDialogIsCanceledThenActivityIsFinished() {
        show("uGqvGhLUSNaNSIi");

        cancelKeyPickerDialog();

        checkActivityIsFinished();
    }

    //region Test tools

    @Before
    public void before() {
        Intents.init();
    }

    @After
    public void after() {
        Intents.release();
        reset(theApplication(Tp2Application.class));
    }

    private void show() {
        activityRule.launchActivity(null);

        rotateToPortrait();
    }

    private void show(String textToDecrypt) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textToDecrypt);

        activityRule.launchActivity(intent);
    }

    private void showWithNoWifi() {
        simulateConnectivityError();

        show();
    }

    private void showWithServerDown() {
        simulateServerError();

        show();
    }

    private void openKeyPickerDialog() {
        onView(withId(R.id.select_key_button)).perform(click());
    }

    private void typeInPickerDialog(int keyId) {
        onView(withId(R.id.keyPickerDialog)).perform(setValue(keyId));
    }

    private void cancelKeyPickerDialog() {
        onView(withId(R.id.keyPickerDialog)).perform(cancel());
    }

    private void closeKeyPickerDialog() {
        onView(withId(R.id.keyPickerDialog)).perform(ok());
    }

    private void changeKeyIdTo(int key) {
        openKeyPickerDialog();
        typeInPickerDialog(key);
        closeKeyPickerDialog();
    }

    private void changeInputTextTo(String text) {
        onView(withId(R.id.input_edittext)).perform(clearText(), typeText(text), replaceText(text));
        closeSoftKeyboard();
    }

    private void pressEncryptButton() {
        onView(withId(R.id.encrypt_button)).perform(click());
    }

    private void pressDecryptButton() {
        onView(withId(R.id.decrypt_button)).perform(click());
    }

    private void pressCopyButton() {
        onView(withId(R.id.copy_button)).perform(click());
    }

    private void pressActivateWifiButton() {
        onView(snackbarButton()).perform(click());
    }

    private void rotateToLandscape() {
        changeOrientationToLandscapeOn(activityRule.getActivity());
    }

    private void rotateToPortrait() {
        changeOrientationToPortraitOn(activityRule.getActivity());
    }

    private void checkKeyIdIs(int key) {
        String keyText = activityRule.getActivity().getResources().getString(R.string.text_current_key);
        onView(withId(R.id.current_key_textview)).check(matches(withText(String.format(keyText, key))));
    }

    private void checkKeyPickerDialogIsOpen() {
        onView(withId(R.id.keyPickerDialog)).check(matches(isDisplayed()));
    }

    private void checkInputIs(String text) {
        onView(withId(R.id.input_edittext)).check(matches(withText(text)));
    }

    private void checkOutputIs(String text) {
        onView(withId(R.id.output_textview)).check(matches(withText(text)));
    }

    private void checkSnackbarMessageIs(@StringRes int resourceId) {
        onView(snackbar()).check(matches(isDisplayed()));
        onView(snackbar()).check(matches(withText(resourceId)));
    }

    private void checkActivityIsFinished() {
        assertTrue(activityRule.getActivity().isFinishing());
    }

    @SuppressWarnings("ConstantConditions")
    private void checkClipboardIs(String text) {
        ClipboardManager clipboard = (ClipboardManager) activityRule.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        assertEquals(text, clipboard.getPrimaryClip().getItemAt(0).getText().toString());
    }

    private void checkWifiSettingsIsOpen() {
        intended(toWifiSettingsActivity());
    }

    private void simulateConnectivityError() {
        doReturn(okHttpClientThatDoConnectivityError()).when(theApplication(Tp2Application.class)).getOkHttpClient();
        intending(toWifiSettingsActivity()).respondWith(new Instrumentation.ActivityResult(0, new Intent()));
    }

    private void simulateServerError() {
        doReturn(okHttpClientThatDoServerError()).when(theApplication(Tp2Application.class)).getOkHttpClient();
        intending(toWifiSettingsActivity()).respondWith(new Instrumentation.ActivityResult(0, new Intent()));
    }

    //endregion

}