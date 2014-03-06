package com.zizibujuan.android.util;

import android.os.AsyncTask;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http GET 请求任务
 *
 * @author jinzw
 * @since 0.0.1
 * Created by jzw on 12/11/13.
 */
public class HttpGetTask extends AsyncTask<Map<String, Object>, Void, List<Map<String, Object>>>{


    //private static final String SERVER_LOCATION = "http://10.83.5.115:8088/"; //TODO:存到手机数据库上
    private String url;
    private ResponseArray responseArray;
    private ResponseObject responseObject;
    private ResponseObject errorCallback;
    private Map errorObject;
    private boolean responseIsArray = true;

    public HttpGetTask(String url, ResponseArray responseArray, ResponseObject errorCallback) {
        this.url = url;
        this.responseArray = responseArray;
        this.errorCallback = errorCallback;
        responseIsArray = true;


    }

    public HttpGetTask(String url, ResponseObject responseObject, ResponseObject errorCallback){
        this.url = url;
        this.responseObject = responseObject;
        this.errorCallback = errorCallback;
        responseIsArray = false;
    }

    @Override
    protected List<Map<String, Object>> doInBackground(Map<String, Object>... params) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(prepareUrl(params));
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                if(responseIsArray){
                    return JsonUtil.fromJsonArray(inputStream);
                }else{
                    Map<String, Object> data = JsonUtil.fromJsonObject(inputStream);
                    if(data == null){
                        data = new HashMap<String, Object>();
                    }
                    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                    result.add(data);
                    return result;
                }

            }else{
                // TODO: 处理错误
                InputStream in = new BufferedInputStream(connection.getErrorStream());
                errorObject = JsonUtil.fromJsonObject(in);
                // httpURLConnection.getErrorStream();
                System.out.print("错误状态码：" + connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // httpURLConnection.getErrorStream();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    private String prepareUrl(Map<String, Object>...params){
        StringBuilder sbUrl = new StringBuilder(ConfigurationScope.serverLocation);
        sbUrl.append(this.url);
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

    @Override
    protected void onPostExecute(List<Map<String, Object>> data) {
        super.onPostExecute(data);
        // 如果data为null，说明获取数据出错了
        if(errorObject != null){
            errorCallback.callback(errorObject);
        }else{
            if(responseIsArray){
                responseArray.callback(data);
            }else{
                responseObject.callback(data.get(0));
            }
        }

    }

}
