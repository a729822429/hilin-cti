package icu.hilin.cti.core.constant;

import cn.hutool.json.JSONUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 呼叫类型
 * 不允许继承重构和新创建实体
 * 不使用枚举，是因为类序列号更加通用
 */
@Getter
public final class CallType {

    public static final CallType CALL_IN = new CallType("CALL_IN", "呼入");
    public static final CallType CALL_OUT = new CallType("CALL_OUT", "呼出");
    public static final CallType INNER = new CallType("INNER", "内线互呼");

    private static final CallType[] VALUES;

    static {
        List<CallType> values = new ArrayList<>();
        for (Field declaredField : CallType.class.getDeclaredFields()) {
            // 判断是否是静态变量，并且类型时CallType
            if (Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType() == CallType.class) {
                try {
                    values.add((CallType) declaredField.get(null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        VALUES = values.toArray(new CallType[]{});
    }

    private final String code;
    private final String name;

    private CallType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static CallType[] values() {
        return VALUES;
    }

}
