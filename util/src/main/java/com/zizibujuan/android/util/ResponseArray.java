package com.zizibujuan.android.util;

import java.util.List;
import java.util.Map;

/**
 * http响应数据是数组对象，要处理的回调函数
 *
 * @author jinzw
 * @since 0.0.1
 * Created by jzw on 12/11/13.
 */
public interface ResponseArray {

    public void callback(List<Map<String, Object>> data);
}
