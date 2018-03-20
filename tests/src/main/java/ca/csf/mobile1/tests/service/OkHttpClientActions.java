package ca.csf.mobile1.tests.service;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Fabrique de mocks de {@link OkHttpClient}.
 */
public class OkHttpClientActions {

    /**
     * Fourni un {@link OkHttpClient} qui produira un erreur de connectivité lors d'un appel réseau.
     * @return {@link OkHttpClient} configuré.
     */
    public static OkHttpClient okHttpClientThatDoConnectivityError() {
        try {
            OkHttpClient okHttpClient = mock(OkHttpClient.class);
            Call call = mock(Call.class);

            doReturn(call).when(okHttpClient).newCall(any(Request.class));
            doThrow(new IOException()).when(call).execute();

            return okHttpClient;
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception while simulating a connectivity error.", e);
        }
    }

    /**
     * Fourni un {@link OkHttpClient} qui produira un erreur de serveur (code 500) lors d'un appel réseau.
     * @return {@link OkHttpClient} configuré.
     */
    public static OkHttpClient okHttpClientThatDoServerError() {
        try {
            OkHttpClient okHttpClient = mock(OkHttpClient.class);
            Call call = mock(Call.class);

            Request request = new Request.Builder()
                    .url("http://localhost:8080/")
                    .build();
            Response response = new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(500)
                    .message("{}")
                    .body(ResponseBody.create(MediaType.parse("application/json"), "{}"))
                    .build();

            doReturn(call).when(okHttpClient).newCall(any(Request.class));
            doReturn(response).when(call).execute();

            return okHttpClient;
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception while simulating a server error.", e);
        }
    }

}
