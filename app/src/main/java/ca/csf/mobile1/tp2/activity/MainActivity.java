package ca.csf.mobile1.tp2.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import ca.csf.mobile1.tp2.R;
import ca.csf.mobile1.tp2.application.EncryptionTask;
import ca.csf.mobile1.utils.view.CharactersFilter;
import ca.csf.mobile1.utils.view.KeyPickerDialog;

public class MainActivity extends Tp2Activity implements EncryptionTask.EncryptionListener {

    private static final int KEY_LENGTH = 5;
    private static final int MAX_KEY_VALUE = (int) Math.pow(10, KEY_LENGTH) - 1;

    private View rootView;
    private EditText inputEditText;
    private TextView outputTextView;
    private TextView currentKeyTextView;
    private ProgressBar progressBar;
    private Button encryptButton;

    EncryptionTask encryptionTask = new EncryptionTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createView();
        //KeyPickerDialog.make(this,5).setCancelAction();
    }

    private void createView() {
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootView);
        progressBar = findViewById(R.id.progressbar);
        inputEditText = findViewById(R.id.input_edittext);
        inputEditText.setFilters(new InputFilter[]{new CharactersFilter()});
        outputTextView = findViewById(R.id.output_textview);
        currentKeyTextView = findViewById(R.id.current_key_textview);
        encryptButton = findViewById(R.id.encrypt_button);
        encryptButton.setOnClickListener(encryptWord);
    }

    private View.OnClickListener encryptWord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            String input = inputEditText.getText().toString();
            encryptionTask.execute(input);
        }
    };

    @SuppressWarnings("ConstantConditions")
    private void putTextInClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_encrypted_text), text));
    }

    @Override
    public void notifyEncriptionListener(StringBuilder encryptedWord) {
        outputTextView.setText(encryptedWord);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
