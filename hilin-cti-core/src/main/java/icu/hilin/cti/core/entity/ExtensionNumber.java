package icu.hilin.cti.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分机号信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionNumber {

    /**
     * 分机号
     */
    private String number;
    private String context;
    private String domain;
    private String group;
    private String contact;
    private String callgroup;
    private String effective_caller_id_name;
    private String effective_caller_id_number;

}
