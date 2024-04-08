package icu.hilin.cti.freeswitch.client.inbound.cmd;

import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.transport.message.EslMessage;

import java.util.List;

@Slf4j
public class BaseCmd {
    public static final String RETURN_SUCCESS = "+OK";

    /**
     * 底层调用FS执行同步命令，返回执行是否成功的结果
     *
     * @param command
     * @param arg
     */
    public boolean exetCommand(String command, String arg) {
        List<String> msgs = exetQueryCommand(command, arg);
        if (msgs == null) {
            return false;
        }
        return handleResult(msgs.get(0));
    }

    public boolean exetCommand(String command) {
        List<String> msgs = exetQueryCommand(command, "");
        if (msgs == null) {
            return false;
        }
        return handleResult(msgs.get(0));
    }

    /**
     * 底层调用FS执行查询数据类命令，返回查询结果
     *
     * @param command
     * @param arg
     */
    public List<String> exetQueryCommand(String command, String arg) {
        if (FsClient.getFsClient() == null) {
            log.error("client为空,执行命令：{}，参数：{}", command, arg);
            return null;
        }
        EslMessage msg = FsClient.getFsClient().sendSyncApiCommand(command, arg);
        log.info("client_id : {},  执行命令：{}，参数：{}，返回结果：{}", FsClient.getFsClient().hashCode(), command, arg, msg.getBodyLines());
        return msg.getBodyLines();
    }

    /**
     * 底层调用FS执行异步命令
     *
     * @param command
     * @param arg
     */
    /*public boolean exetAsyncCommand(String command, String arg) {
        log.info("执行异步命令：{}，参数：{}", command, arg);
        String msg = fsClient.sendAsyncApiCommand(command, arg);
        log.info("执行异步命令：{}，参数：{}，返回结果：{}", command, arg, msg);
        return handleResult(msg);
    }*/

    /**
     * 处理响应消息，如果带有+OK，则表示成功。
     *
     * @param msg
     * @return
     */
    public boolean handleResult(String msg) {
        // FIXME 如果需要更加严谨，后续加响应头一起判断。或者看看有没有对应的响应码
        // FIXME 由于采用的是socket长连接，结果没有包含响应码
        // FIXME 测试调用api方法后，响应头为api/response，不会改变
        // 执行命令有两种响应结果，+OK或boolean值。+OK是告知执行结果；boolean值是查询指定内容是否存在等。
        return msg != null && (RETURN_SUCCESS.startsWith(msg) || msg.startsWith(RETURN_SUCCESS) || "true".equalsIgnoreCase(msg) || msg.contains("already exist"));
    }
}
