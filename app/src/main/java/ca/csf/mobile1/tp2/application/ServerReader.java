package ca.csf.mobile1.tp2.application;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Samuel on 2018-03-20.
 */

public class ServerReader extends AsyncTask<Integer,Void,StringBuilder> {
    Tp2Application service = new Tp2Application();
    public ServerReader() {
    }

    @Override
    protected StringBuilder doInBackground(Integer... integers) {
        String address = "http://35.226.88.28/cypher/substitution/key/" + integers[0].toString();
        fetchDataFromServer(address);
        return null;
    }

    public StringBuilder fetchDataFromServer(String address)
    {
        try
        {
            OkHttpClient client = service.getOkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful())
            {
                String hson = response.body().string();
                ObjectMapper objectMapper = service.getObjectMapper();

            }
        }
        catch (IOException e)
        {

        }
        return null;
    }


}
