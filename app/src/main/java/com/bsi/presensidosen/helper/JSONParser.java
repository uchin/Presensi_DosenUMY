package com.bsi.presensidosen.helper;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.apache.http.params.HttpConnectionParams.setConnectionTimeout;

/**
 * Created by Mukhlasin on 9/14/2016.
 */
/*public class JSONParser {
    static InputStream IS = null;
    static JSONObject JB = null;
    static String json = "";

    //This a Constructor
    public JSONParser() {

    }

    public JSONObject makeHttpjsonRequest(String url, String method, List<NameValuePair> params) {
        try {
            // now let's check for request method (POST or GET)
            if (method == "POST") {
                //now request method is POST
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

            } else if (method == "GET") {
                // now request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                IS = httpEntity.getContent();

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(IS, "iso-8859"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append((line + "\n"));
            }
            IS.close();
            json = sb.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e("Buffer Error", "Cnversion Error"+e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JB = new JSONObject(json);
        } catch (JSONException e){
            Log.e("JSON parser", e.toString());
        }
        return JB;
    }

}*/



public class JSONParser
{

    private final static String TAG = JSONParser.class.getSimpleName();

    private InputStream inputStream;
    private JSONObject jsonObject;
    private String jsonText;

    JSONParser()
    {
        inputStream = null;
        jsonObject = null;
        jsonText = "";
    }


 //Digunakan untuk mengambambil objek JSON dengan menggunakan method GET

    /*JSONObject parseUsingGET(String url)
    {
        URI uri = null;
        try
        {
            uri = new URI(url);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setParams(params);
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(uri);
        try
        {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
            jsonText = stringBuilder.toString();
            Log.i(TAG, "Got JSON = " + jsonText);
        } catch (Exception e)
        {
            Log.e(TAG, "JSON buffer error " + e.toString());
        }

        try
        {
            jsonObject = new JSONObject(jsonText);
        } catch (JSONException e)
        {
            Log.e(TAG, "JSON error parsing data " + e.toString());
        }

        return jsonObject;
    }*/

    public JSONObject parseUsingGET(String url) throws ClientProtocolException {
        URI uri = null;
        try
        {
            uri = new URI(url);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        BasicHttpParams params = new BasicHttpParams();
        setConnectionTimeout(params, 2000);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setParams(params);
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(uri);
        try
        {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            jsonText = stringBuilder.toString();
            Log.i(TAG, "Got JSON = " + jsonText);
        } catch (Exception e)
        {
            Log.e(TAG, "JSON buffer error " + e.toString());
        }

        try
        {
            jsonObject = new JSONObject(jsonText);
        } catch (JSONException e)
        {
            Log.e(TAG, "JSON error parsing data " + e.toString());
        }

        return jsonObject;
    }

    /**
     * Digunakan untuk mengambambil objek JSON dengan menggunakan method POST
     */
    public JSONObject parseUsingPOST(String url, List<NameValuePair> params)
    {
        HttpParams httpParams = new BasicHttpParams();
        setConnectionTimeout(httpParams, 6000);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setParams(httpParams);
        try
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            jsonText = stringBuilder.toString();
            Log.i(TAG, "Got JSON = " + jsonText);
        } catch (Exception e)
        {
            Log.e(TAG, "JSON buffer error " + e.toString());
        }

        try
        {
            jsonObject = new JSONObject(jsonText);
        } catch (JSONException e)
        {
            Log.e(TAG, "JSON error parsing data " + e.toString());
        }

        return jsonObject;
    }
}
