package icu.hilin.cti.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallType {

    CALL_IN("CALL_IN", "呼入"),
    CALL_OUT("CALL_OUT", "呼出"),
    INNER("INNER", "内线互呼");

    private final String code;
    private final String name;

}
