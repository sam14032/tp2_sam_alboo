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
    }

    @Override
    protected StringBuilder doInBackground(String... strings) {

        return null;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        notifyEndOfExecution();
    }

    private void decryptMessage(String cryptedMessage)
    {
        StringBuilder decryptedMessage = null;
        this.decryptedMessage = decryptedMessage;
    }

    public void addDecryterListener(DecryptionListener listener){decryptionListenerList.add(listener);}
    public void removeDecrypterListener(DecryptionListener listener){decryptionListenerList.remove(listener);}

    private void notifyEndOfExecution()
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
