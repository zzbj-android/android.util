package com.zizibujuan.android.util;


import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * HTTP POST
 *
 * @author jinzw
 * @since 0.0.1
 * Created by jzw on 12/30/13.
 */
public class HttpPostTask extends AsyncTask<Map<String, Object>, Void, Map<String, Object>> {

    //private static final String SERVER_LOCATION = "http://10.83.5.115:8088/"; //TODO:存到手机数据库上
    private String url;
    private ResponseObject responseObject;
    private ResponseObject errorCallback;
    private Map<String, Object> errorObject;

    public HttpPostTask(String url, ResponseObject responseObject, ResponseObject errorCallback) {
        this.url = url;
        this.responseObject = responseObject;
        this.errorCallback = errorCallback;
    }

    @Override
    protected Map<String, Object> doInBackground(Map<String, Object>... params) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(prepareUrl());
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            connection.setDoOutput(true);
            //connection.setRequestMethod("POST");

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            out.write(prepareParams(params).getBytes());

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(connection.getInputStream());

                return JsonUtil.fromJsonObject(in);
            }else{
                InputStream in = new BufferedInputStream(connection.getErrorStream());
                errorObject = JsonUtil.fromJsonObject(in);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Map<String, Object> data) {
        super.onPostExecute(data);

        if(errorObject != null){
            errorCallback.callback(errorObject);
        }else{
            responseObject.callback(data);
        }

    }

    private String prepareUrl(){
        StringBuilder sbUrl = new StringBuilder(ConfigurationScope.serverLocation);
        sbUrl.append(this.url);
        return sbUrl.toString();
    }

    private String prepareParams(Map<String, Object>...params){
        StringBuilder sbUrl = new StringBuilder();
        if(params != null){
            Map<String, Object> param = params[0];
            if(param != null && !param.isEmpty()){
                sbUrl.append("?");
                int count = 0;
                for(Map.Entry entry : param.entrySet()){
                    if(count != 0){
                        sbUrl.append("&");
                    }
                    sbUrl.append(entry.getKey()).append("=").append(entry.getValue());
                    count++;
                }
            }
        }
        return sbUrl.toString();
    }
}
