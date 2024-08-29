package icu.hilin.cti.core.constant;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class EventType {

    public final static EventType EVENT_READY = new EventType("EVENT_READY", "呼叫进入");
    public final static EventType EVENT_BRING = new EventType("EVENT_BRING", "响铃");
    public final static EventType EVENT_SWITCH = new EventType("EVENT_SWITCH", "席位切换");
    public final static EventType EVENT_ANSWER = new EventType("EVENT_ANSWER", "通话接通");
    public final static EventType EVENT_HANGUP = new EventType("EVENT_HANGUP", "通话结束");
    public final static EventType EVENT_RECORD = new EventType("EVENT_RECORD", "录音回调");

    private static final EventType[] VALUES;

    static {
        List<EventType> values = new ArrayList<>();
        for (Field declaredField : EventType.class.getDeclaredFields()) {
            // 判断是否是静态变量，并且类型时EventType
            if (Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType() == EventType.class) {
                try {
                    values.add((EventType) declaredField.get(null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        VALUES = values.toArray(new EventType[]{});
    }

    public static EventType[] values() {
        return VALUES;
    }

    private final String code;
    private final String name;

    private EventType(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
