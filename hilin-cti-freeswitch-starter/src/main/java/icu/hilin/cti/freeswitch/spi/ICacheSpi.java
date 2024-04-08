package icu.hilin.cti.freeswitch.spi;

/**
 * 缓存对接接口
 */
public interface ICacheSpi {

    /**
     * 写入缓存
     */
    void put(String key, String value);

    /**
     * 写入缓存
     */
    void put(String key, String value, long timeoutSeconds);

    /**
     * 获取缓存信息
     */
    String get(String key);

    /**
     * 删除
     */
    void del(String key);

}
