package com.sshakya.accelexample;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sshakya on 8/16/16.
 */
public class CustomRequest extends Request<JSONObject> {
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            //  Log.d("JSONsend","CUSTOM REQ BHITRA-- response = "+ response.toString());
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //Log.d("JSONsend","CUSTOM REQ BHITRA -------" + jsonString);
            JSONObject jobj = new JSONObject(jsonString);
            // Log.d("JSONsend","CUSTOM REQ BHITRA -- jsonobj = " + jobj.toString());
            return Response.success(jobj, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (JSONException e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        //Log.d("JSONsend","DELIVER HUDIA CHA -------" + response.toString());
        listener.onResponse(response);
    }

    private Response.Listener<JSONObject> listener;
    private Map<String,String> params;

    public CustomRequest(String url, Map<String,String> params, Response.Listener<JSONObject> responseListener,
                         Response.ErrorListener errorListener){
        super(Method.GET, url, errorListener);

        this.listener =  responseListener;
        this.params = params;
    }
    public CustomRequest(int method, String url, Map<String,String> params, Response.Listener<JSONObject> responseListener,
                         Response.ErrorListener errorListener){
        super(method, url, errorListener);

        this.listener =  responseListener;
        this.params = params;
    }

    protected Map<String,String> getParams() throws com.android.volley.AuthFailureError{
        return params;
    };




}