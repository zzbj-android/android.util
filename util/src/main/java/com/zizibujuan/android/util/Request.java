package com.zizibujuan.android.util;

import java.util.Map;

/**
 * http请求帮助类
 *
 * @author jinzw
 * @since 0.0.1
 *
 * Created by jzw on 12/10/13.
 */
public class Request {

    public static void get(String url,
                           Map<String, Object> params,
                           ResponseArray response,
                           ResponseObject error){

        HttpGetTask task = new HttpGetTask(url, response, error);

        Map[] map = new Map[1];
        map[0] = params;
        task.execute(map);
    }

    public static void get(String url,
                           Map<String, Object> params,
                           ResponseObject response,
                           ResponseObject error){
        HttpGetTask task = new HttpGetTask(url, response, error);

        Map[] map = new Map[1];
        map[0] = params;
        task.execute(map);
    }


    public static void post(String url,
                            Map<String, Object> params,
                            ResponseObject response,
                            ResponseObject error){
        HttpPostTask task = new HttpPostTask(url, response, error);
        Map[] map = new Map[1];
        map[0] = params;
        task.execute(map);
    }

    // TODO: put, post, delete

}
