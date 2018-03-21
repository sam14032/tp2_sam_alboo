package ca.csf.mobile1.tp2.application;

import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Samuel on 2018-03-20.
 */

public class DecryptionTask extends AsyncTask<String, Void, StringBuilder>
{
    List<DecryptionListener> decryptionListenerList;
    StringBuilder decryptedMessage;
    public DecryptionTask() {
        decryptionListenerList = new LinkedList<>();
        decryptedMessage = new StringBuilder();
    }

    @Override
    protected StringBuilder doInBackground(String... strings) {
        decryptMessage(strings[0]);
        return decryptedMessage;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        notifyEndOfExecution(stringBuilder);
    }

    private void decryptMessage(String cryptedMessage)
    {
        StringBuilder decryptedMessage = new StringBuilder();
        this.decryptedMessage = decryptedMessage;
    }

    public void registerListener(DecryptionListener listener){decryptionListenerList.add(listener);}
    public void removeDecrypterListener(DecryptionListener listener){decryptionListenerList.remove(listener);}

    private void notifyEndOfExecution(StringBuilder decryptedMessage)
    {
        for (DecryptionListener decryptionListener : decryptionListenerList)
        {
            decryptionListener.notifyDecriptionListener(decryptedMessage);
        }
    }

    public interface DecryptionListener
    {
        public void notifyDecriptionListener(StringBuilder message);
    }

}
