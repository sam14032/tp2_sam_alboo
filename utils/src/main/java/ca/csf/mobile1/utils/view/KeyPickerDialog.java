package ca.csf.mobile1.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import ca.csf.mobile1.utils.R;

/**
 * Dialogue permettant de saisir une clé de chiffrement via une interface simplifiée.
 * <p>
 * Son utilisation est très simple et calquée sur celle de Snackbar. Voici un exemple :
 * <pre>
 * KeyPickerDialog.make(this, 5)
 *                .setKey(12345)
 *                .show();
 * </pre>
 * <p>
 * Pour être notifié des actions sur le dialogue, il vous faut envoyer aux fonctions
 * {@link KeyPickerDialog#setConfirmAction(ConfirmListener) setConfirmAction} et
 * {@link KeyPickerDialog#setCancelAction(CancelListener) setCancelAction} un objet implémentant
 * {@link ConfirmListener ConfirmListener} et
 * {@link CancelListener CancelListener}. Par exemple :
 * <pre>
 * KeyPickerDialog.make(this, 5)
 *                .setKey(12345)
 *                .setConfirmAction(this)
 *                .setCancelAction(this)
 *                .show();
 * </pre>
 */
public final class KeyPickerDialog {

    private static final int DEFAULT_KEY = 0;

    private final Context context;
    private final int keyLength;
    private ConfirmListener confirmListener;
    private CancelListener cancelListener;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private List<TextView> digitsTextViews;

    /**
     * Crée un nouveau KeyPickerDialog. Notez qu'après appel à cette fonction, le dialogue n'est pas encore affiché.
     *
     * @param context Contexte de création du dialogue. Généralement, l'activité courante.
     * @return KeyPickerDialog nouvellement créé.
     */
    public static KeyPickerDialog make(Context context, int keyLength) {
        return new KeyPickerDialog(context, keyLength);
    }

    private KeyPickerDialog(Context context, int keyLength) {
        if (context == null) {
            throw new IllegalArgumentException("\"context\" cannot be null.");
        }
        if (keyLength <= 0) {
            throw new IllegalArgumentException("\"keyLength\" must be greater than 0.");
        }

        this.context = context;
        this.keyLength = keyLength;

        createView();
    }

    /**
     * Assigne un {@link ConfirmListener} à ce KeyPickerDialog. Cet objet sera appellé lorsque
     * l'utilisateur cliquera sur "Ok".
     *
     * @param confirmListener {@link ConfirmListener} à notifier d'un clic sur le bouton "Ok".
     * @return KeyPickerDialog actuel.
     */
    public KeyPickerDialog setConfirmAction(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;

        return this;
    }

    /**
     * Assigne un {@link CancelListener} à ce KeyPickerDialog. Cet objet sera appellé lorsque
     * l'utilisateur cliquera sur "Cancel".
     *
     * @param cancelListener {@link CancelListener} à notifier d'un clic sur le bouton "Cancel".
     * @return KeyPickerDialog actuel.
     */
    public KeyPickerDialog setCancelAction(CancelListener cancelListener) {
        this.cancelListener = cancelListener;

        return this;
    }

    /**
     * Affiche le KeyPickerDialog.
     *
     * @return KeyPickerDialog actuel.
     */
    public KeyPickerDialog show() {
        dialog = dialogBuilder.create();
        dialog.show();
        return this;
    }

    /**
     * Modifie la clé affichée par ce KeyPickerDialog.
     *
     * @param key Clé à afficher.
     * @return KeyPickerDialog actuel.
     */
    @SuppressLint("DefaultLocale")
    public KeyPickerDialog setKey(int key) {
        if (key < 0) {
            throw new IllegalArgumentException("\"key\" must be greater or equal to 0");
        }
        if (key > (int) Math.pow(10, keyLength) - 1) {
            throw new IllegalArgumentException("\"key\" must be less than 10^" + keyLength);
        }

        String keyAsString = String.format("%0" + keyLength + "d", key);
        for (int i = 0; i < digitsTextViews.size(); i++) {
            digitsTextViews.get(i).setText(String.valueOf(keyAsString.charAt(i)));
        }

        return this;
    }

    /**
     * Appelle le {@link ConfirmListener} du dialogue, s'il existe. Effectue aussi toutes les actions reliées à un clic,
     * tel que les animations, comme si un utilisateur avait effectué le clic.
     */
    public void performOk() {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
    }

    /**
     * Appelle le {@link CancelListener} du dialogue, s'il existe. Effectue aussi toutes les actions reliées à un clic,
     * tel que les animations, comme si un utilisateur avait effectué le clic.
     */
    public void performCancel() {
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
    }

    @SuppressLint("InflateParams")
    private void createView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        GridLayout gridLayout = (GridLayout) layoutInflater.inflate(R.layout.view_key_picker_dialog, null);

        gridLayout.setColumnCount(keyLength);
        gridLayout.setTag(R.id.keyPickerDialogTag, this);

        //Top "Up" buttons
        for (int i = 0; i < keyLength; i++) {
            layoutInflater.inflate(R.layout.view_arrow_up, gridLayout);

            final int index = i;
            ImageButton imageButton = (ImageButton) gridLayout.getChildAt(i);
            imageButton.setOnClickListener(view -> onArrowButtonClicked(index, 1));
        }

        //Middle "Digits" text views
        digitsTextViews = new LinkedList<>();
        for (int i = 0; i < keyLength; i++) {
            layoutInflater.inflate(R.layout.view_number, gridLayout);

            TextView textView = (TextView) gridLayout.getChildAt(i + keyLength);
            digitsTextViews.add(textView);
        }

        //Bottom "Down" buttons
        for (int i = 0; i < keyLength; i++) {
            layoutInflater.inflate(R.layout.view_arrow_down, gridLayout);

            final int index = i;
            ImageButton button = (ImageButton) gridLayout.getChildAt(i + keyLength * 2);
            button.setOnClickListener(view -> onArrowButtonClicked(index, -1));
        }

        //Set initial key
        setKey(DEFAULT_KEY);

        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(R.string.text_key);
        dialogBuilder.setView(gridLayout);
        dialogBuilder.setPositiveButton(android.R.string.ok, (dialog, which) -> onOkButtonClicked());
        dialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> onCancelButtonClicked());
    }

    private void onArrowButtonClicked(int index, int increment) {
        TextView numberTextView = digitsTextViews.get(index);

        int currentValue = Integer.valueOf(numberTextView.getText().toString());
        currentValue = (currentValue + increment) % 10;
        currentValue = currentValue < 0 ? currentValue + 10 : currentValue;

        numberTextView.setText(String.valueOf(currentValue));
    }

    private void onOkButtonClicked() {
        if (confirmListener != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (TextView textView : digitsTextViews) {
                stringBuilder.append(textView.getText());
            }
            confirmListener.onKeyPickerDialogConfirm(Integer.valueOf(stringBuilder.toString()));
        }
        dialog.dismiss();
    }

    private void onCancelButtonClicked() {
        if (cancelListener != null) {
            cancelListener.onKeyPickerDialogCancel();
        }
        dialog.cancel();
    }

    /**
     * Interface que tous les objets désirant être notifié du clic sur le bouton "Ok" du {@link KeyPickerDialog}
     * doivent implémenter. Voir {@link KeyPickerDialog#setConfirmAction(ConfirmListener)}.
     */
    public interface ConfirmListener {
        /**
         * Survient lorsque le bouton "Ok" d'un {@link KeyPickerDialog} a été appuyé par l'utilisteur.
         *
         * @param key Clé sélectionnée par l'utilisateur via le {@link KeyPickerDialog}.
         */
        void onKeyPickerDialogConfirm(int key);
    }

    /**
     * Interface que tous les objets désirant être notifié du clic sur le bouton "Cancel" du {@link KeyPickerDialog}
     * doivent implémenter. Voir {@link KeyPickerDialog#setCancelAction(CancelListener)}.
     */
    public interface CancelListener {
        /**
         * Survient lorsque le bouton "Cancel" d'un {@link KeyPickerDialog} a été appuyé par l'utilisteur.
         */
        void onKeyPickerDialogCancel();
    }

}
