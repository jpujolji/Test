package com.jpujolji.www.test.network;

import android.content.Context;
import android.util.Log;

import com.jpujolji.www.test.interfaces.HttpInterface;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * La clase HttpClient es usada para realizar las peticiones HTTP en la aplicación Android, utilizando
 * la librería externa <a href="http://loopj.com/android-async-http">Android Asynchronous Http Client</a>.
 */
public class HttpClient {

    HttpInterface mHttpInterface;
    Context mContext;

    private static final String URL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    public HttpClient(HttpInterface httpInterface, Context context) {
        mHttpInterface = httpInterface;
        mContext = context;
    }

    public void httpRequest() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20000);

        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                long progressPercentage = 100 * bytesWritten
                        / totalSize;
                mHttpInterface.onProgress(progressPercentage);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("Depuracion", "errorResponse " + errorResponse + " throwable " + throwable);
                mHttpInterface.onFailed(errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("Depuracion", "response " + response);
                mHttpInterface.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("Depuracion", "Error " + statusCode + " responseString " + responseString);
            }
        });
    }
}