package com.eveb.saasops.batch.sys.constants;

/**
 * @Author: Miracle
 * @Description:系统配置表定义的groups类型
 * @Date: 17:10 2017/12/26
 **/
public enum SysEnum {

    /**OKHttp 代理**/
    enumProxy("proxy");

    private String key;

    SysEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
