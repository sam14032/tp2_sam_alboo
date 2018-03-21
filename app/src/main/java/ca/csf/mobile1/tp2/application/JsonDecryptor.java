package ca.csf.mobile1.tp2.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Samuel on 2018-03-21.
 */

public class JsonDecryptor {

    List<JsonListener> jsonListenerList;
    private Integer id;
    private String[] outputChars;
    private String[] inputChars;

    @JsonCreator
    public JsonDecryptor(Integer id, String[] outputChars, String[] inputChars) {
        this.id =id;
        this.outputChars = outputChars;
        this.inputChars = inputChars;
        jsonListenerList = new LinkedList<>();
        notifyEndOfFetch();
    }

    private String[] getOutputChars()
    {
        return outputChars;
    }

    private String[] getInputChars()
    {
        return inputChars;
    }

    public void notifyEndOfFetch()
    {
        for (JsonListener jsonListener : jsonListenerList)
        {
            jsonListener.notifyJsonDecryption(getOutputChars(),getInputChars());
        }
    }

    public interface JsonListener
    {
        public void notifyJsonDecryption(String[] outputChars, String[] inputChars);
    }

}
