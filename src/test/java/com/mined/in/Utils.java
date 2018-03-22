package com.mined.in;

import static okhttp3.Protocol.HTTP_2;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Utils for tests.
 *
 * @author Dmitry Tverdokhleb
 *
 */
public class Utils {

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    /**
     * Returns HTTP client with custom JSON response.
     *
     * @param response JSON response
     * @param responseCode HTTP code
     * @return HTTP client with custom JSON response
     */
    public static OkHttpClient getHttpClient(String response, int responseCode) {
        Interceptor replaceJSONInterceptor = chain -> {
            Request request = chain.request();
            ResponseBody body = ResponseBody.create(MEDIA_JSON, response);
            return new Response.Builder().body(body).request(request).protocol(HTTP_2).code(responseCode).message("").build();
        };
        return new OkHttpClient.Builder().addInterceptor(replaceJSONInterceptor).build();
    }

}
