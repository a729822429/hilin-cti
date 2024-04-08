package icu.hilin.cti.freeswitch.client.inbound;

public enum FsEventType {
    /**
     * 跟呼叫相关的通道事件
     */
    // 创建事件。
    CHANNEL_CREATE,
    // 呼叫应答事件。
    CHANNEL_ANSWER,
    // 挂机完成事件。
    CHANNEL_HANGUP_COMPLETE,
    CHANNEL_HANGUP,
    // 通话录制完成
    RECORD_STOP,
    //心跳
    HEARTBEAT,
    // 通道桥接
    CHANNEL_BRIDGE
}
