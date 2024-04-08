package icu.hilin.cti.core.entity;

import org.freeswitch.esl.client.transport.event.EslEvent;

import java.util.Map;

public class HilinEslEvent {

    private final EslEvent eslEvent;

    public HilinEslEvent(EslEvent eslEvent) {
        this.eslEvent = eslEvent;
    }

    /**
     * 获取主叫号码
     */
    public String getANI() {
        return eslEvent.getEventHeaders().get("Caller-ANI");
    }

    /**
     * 获取被叫号码
     */
    public String getDNI() {
        return eslEvent.getEventHeaders().get("Caller-Destination-Number");
    }

    /**
     * 获取事件类型
     */
    public String getEventName() {
        return eslEvent.getEventName();
    }

    /**
     * 获取当前leg的id
     */
    public String getLegId() {
        return eslEvent.getEventHeaders().get("Unique-ID");
    }

    public Map<String, String> getEventHeaders() {
        return eslEvent.getEventHeaders();
    }

    public String getRecordpath() {
        return eslEvent.getEventHeaders().get("Record-File-Path");
    }

}