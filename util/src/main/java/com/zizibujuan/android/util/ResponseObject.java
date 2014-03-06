package com.zizibujuan.android.util;

import java.util.Map;

/**
 * http响应数据是对象时，要处理的回调函数
 *
 * @author jinzw
 * @since 0.0.1
 * Created by jzw on 12/30/13.
 */
public interface ResponseObject {

    public void callback(Map<String, Object> data);
}
