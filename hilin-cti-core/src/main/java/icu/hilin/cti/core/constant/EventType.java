package icu.hilin.cti.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventType {

    EVENT_READY("EVENT_READY", "呼叫进入"),
    EVENT_BRING("EVENT_BRING", "响铃"),
    EVENT_SWITCH("EVENT_SWITCH", "席位切换"),
    EVENT_ANSWER("EVENT_ANSWER", "通话接通"),
    EVENT_HANGUP("EVENT_HANGUP", "通话结束"),
    EVENT_RECORD("EVENT_RECORD", "录音回调");

    private final String code;
    private final String name;
}
