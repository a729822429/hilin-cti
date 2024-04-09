package icu.hilin.cti.freeswitch.client.inbound.cmd;

import cn.hutool.core.util.ObjectUtil;
import icu.hilin.cti.core.entity.ExtensionNumber;
import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 分机号命令
 */
@AllArgsConstructor
@Slf4j
public class ExtensionNumberCmd extends BaseCmd {

    public static final String RETURN_SUCCESS = "+OK";

    public ExtensionNumber get(String number) {

        List<String> msgs = exetQueryCommand("list_users user", number);
        if (msgs == null) {
            return null;
        }
        return msgs.stream().map(this::getUser).filter(Objects::nonNull).findFirst().get();
    }


    public List<ExtensionNumber> getAll() {

        List<String> msgs = FsClient.getFsClient().sendSyncApiCommand("list_users", "").getBodyLines();
        if (msgs == null) {
            return null;
        }
        return msgs.stream().map(this::getUser).filter(Objects::nonNull).toList();
    }

    private ExtensionNumber getUser(String userStr) {
        // 第一条数据是字段，所以要排除
        if (ObjectUtil.isEmpty(userStr) || userStr.startsWith("userid|context|domain|group|contact") || userStr.startsWith(RETURN_SUCCESS)) {
            return null;
        }
        String[] userArray = userStr.split("\\|");
        // userid|context|domain|group|contact|callgroup|effective_caller_id_name|effective_caller_id_number
        return ExtensionNumber.builder()
                .number(userArray.length > 0 ? userArray[0] : "")
                .context(userArray.length > 1 ? userArray[1] : "")
                .domain(userArray.length > 2 ? userArray[2] : "")
                .group(userArray.length > 3 ? userArray[3] : "")
                .contact(userArray.length > 4 ? userArray[4] : "")
                .callgroup(userArray.length > 5 ? userArray[5] : "")
                .effective_caller_id_name(userArray.length > 6 ? userArray[6] : "")
                .effective_caller_id_number(userArray.length > 7 ? userArray[7] : "")
                .build();
    }

}
