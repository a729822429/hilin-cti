package icu.hilin.cti.core.entity;

import icu.hilin.cti.core.constant.CallType;
import icu.hilin.cti.core.constant.EventType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class HilinCtiEvent {

    /**
     * 主叫号码
     */
    private String ani;

    /**
     * 被叫号码
     */
    private String dni;

    /**
     * 呼叫类型
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private CallType callType;

    /**
     * 事件类型
     */
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private EventType eventType;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 历史事件列表，当前事件也包含在这列表
     */
    private List<HilinCtiEvent> historyEvents;


    public void setCallType(String callType) {
        if (Arrays.stream(CallType.values()).anyMatch(ct -> ct.getCode().equalsIgnoreCase(callType))) {
            this.callType = Arrays.stream(CallType.values()).filter(ct -> ct.getCode().equalsIgnoreCase(callType)).findFirst().get();
        } else {
            throw new RuntimeException("未识别的calltype : " + callType);
        }
    }

    public String getCallType() {
        return callType.getCode();
    }

    public String getCallTypeName() {
        return callType.getName();
    }

    public void setEventType(String eventType) {
        if (Arrays.stream(EventType.values()).anyMatch(ct -> ct.getCode().equalsIgnoreCase(eventType))) {
            this.eventType = Arrays.stream(EventType.values()).filter(ct -> ct.getCode().equalsIgnoreCase(eventType)).findFirst().get();
        } else {
            throw new RuntimeException("未识别的eventType : " + eventType);
        }
    }

    public String getEventType() {
        return eventType.getCode();
    }

    public String getEventTypeName() {
        return eventType.getName();
    }
}
