package ca.csf.mobile1.tp2.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import ca.csf.mobile1.tp2.R;
import ca.csf.mobile1.tp2.application.DecryptionTask;
import ca.csf.mobile1.tp2.application.EncryptionTask;
import ca.csf.mobile1.utils.view.CharactersFilter;
import ca.csf.mobile1.utils.view.KeyPickerDialog;

public class MainActivity extends Tp2Activity implements EncryptionTask.EncryptionListener,DecryptionTask.DecryptionListener {

    private static final int KEY_LENGTH = 5;
    private static final int MAX_KEY_VALUE = (int) Math.pow(10, KEY_LENGTH) - 1;

    private View rootView;
    private EditText inputEditText;
    private TextView outputTextView;
    private TextView currentKeyTextView;
    private ProgressBar progressBar;
    private Button encryptButton;
    private Button decryptButton;
    private ImageButton copyButton;

    EncryptionTask encryptionTask = new EncryptionTask();
    DecryptionTask decryptionTask = new DecryptionTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createView();
        //KeyPickerDialog.make(this,5).setCancelAction();
    }

    private void createView() {
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootView);
        inputEditText = findViewById(R.id.input_edittext);
        inputEditText.setFilters(new InputFilter[]{new CharactersFilter()});
        outputTextView = findViewById(R.id.output_textview);
        currentKeyTextView = findViewById(R.id.current_key_textview);


        copyButton = findViewById(R.id.copy_button);
        copyButton.setOnClickListener(copyTextToClipboard);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        encryptButton = findViewById(R.id.encrypt_button);
        decryptButton = findViewById(R.id.decrypt_button);


        decryptButton.setOnClickListener(decryptWord);
        encryptButton.setOnClickListener(encryptWord);
    }

    private View.OnClickListener encryptWord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            String input = inputEditText.getText().toString();
            startEncryption(input);
        }
    };

    private  View.OnClickListener decryptWord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            String input = inputEditText.getText().toString();
            startDecription(input);
        }
    };

    private View.OnClickListener copyTextToClipboard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String input = inputEditText.getText().toString();
            putTextInClipboard(input);
            validateCopy();
        }
    };

    @SuppressWarnings("ConstantConditions")
    private void putTextInClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_encrypted_text), text));
    }

    private void validateCopy()
    {
        Toast.makeText(this,"The text is copied in clipboard.",Toast.LENGTH_SHORT).show();
    }
    private void startEncryption(String input)
    {
        try{
            encryptionTask = new EncryptionTask();
            encryptionTask.registerListener(MainActivity.this::notifyEncriptionListener);
            encryptionTask.execute(input);
        }
        catch (Exception exception)
        {

        }
    }

    private void startDecription(String input)
    {
        try
        {
            decryptionTask = new DecryptionTask();
            decryptionTask.registerListener(MainActivity.this::notifyDecriptionListener);
            decryptionTask.execute(input);
        }
        catch(Exception exception)
        {

        }
    }

    @Override
    public void notifyEncriptionListener(StringBuilder encryptedWord) {
        encryptionTask.removeListener(MainActivity.this::notifyEncriptionListener);
        outputTextView.setText(encryptedWord);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void notifyDecriptionListener(StringBuilder message) {
        outputTextView.setText(message);
        decryptionTask.removeDecrypterListener(MainActivity.this::notifyDecriptionListener);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
