package com.bsi.presensidosen.helper;

import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

public class ResponseHandler extends AsyncTask<String, Void, JSONObject> {
    private String url;
    private int reqCode;
    private JSONParser jsonParser;
    private ResponseListener responseListener;

    public ResponseHandler(String url, int reqCode, ResponseListener responseListener)
    {
        this.url = url;
        this.reqCode = reqCode;
        this.responseListener = responseListener;
        jsonParser = new JSONParser();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params)
    {
        JSONObject jObject = null;
        try {
            jObject = jsonParser.parseUsingGET(url);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        return jObject;
    }

    @Override
    protected void onPostExecute(JSONObject result)
    {
        super.onPostExecute(result);
        try
        {
            responseListener.onSuccess(result, reqCode);
        } catch (Exception e)
        {
            e.printStackTrace();
            responseListener.onError(e.getMessage(), reqCode);
        }
    }

    public void setResponseListener(ResponseListener responseListener)
    {
        this.responseListener = responseListener;
    }

    public interface ResponseListener
    {
        void onSuccess(JSONObject jObject, int reqCode);
        void onError(String error, int reqCode);
    }

}
