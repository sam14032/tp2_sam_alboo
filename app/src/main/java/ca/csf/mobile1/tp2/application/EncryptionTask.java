package ca.csf.mobile1.tp2.application;

import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Samuel on 2018-03-19.
 */

public class EncryptionTask extends AsyncTask<String,Void,StringBuilder> implements JsonDecryptor.JsonListener {

    private List<EncryptionListener> encryptionListenerList;
    private StringBuilder encryptedMessage;
    private String[] inputChars;
    private String[] outputChars;

    public EncryptionTask() {
        encryptionListenerList = new LinkedList<>();
        encryptedMessage = new StringBuilder();
    }

    @Override
    protected StringBuilder doInBackground(String... strings) {
        EncryptWord(strings[0]);
        return encryptedMessage;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        notifyEndOfExecution(stringBuilder);
    }

    private void EncryptWord(String words)
    {
        encryptedMessage.append(words);
    }


    public void registerListener(EncryptionListener encryptionListener){encryptionListenerList.add(encryptionListener);}
    public void removeListener(EncryptionListener encryptionListener){encryptionListenerList.remove(encryptionListener);}

    public void notifyEndOfExecution(StringBuilder encryptedMessage)
    {
        for (EncryptionListener encryptionListener : encryptionListenerList )
        {
            encryptionListener.notifyEncriptionListener(encryptedMessage);
        }
    }

    @Override
    public void notifyJsonDecryption(String[] outputChars, String[] inputChars) {
        this.inputChars = inputChars;
        this.outputChars = outputChars;
    }

    public interface EncryptionListener
    {
        public void notifyEncriptionListener(StringBuilder message);
    }

}
