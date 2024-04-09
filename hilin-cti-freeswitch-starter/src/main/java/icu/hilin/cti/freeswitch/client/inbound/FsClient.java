package icu.hilin.cti.freeswitch.client.inbound;

import icu.hilin.cti.freeswitch.HilinFreeswitchConfig;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.transport.CommandResponse;
import org.freeswitch.esl.client.transport.event.EslEventHeaderNames;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FsClient {

    private static final Client FS_CLIENT = new Client();

    private static long lastHeartbeatTimestamp;

    private static HilinFreeswitchConfig fsConfig;
    private static IEslEventListener iEslEventListener;

    static {
        // 每五秒检查客户端状态
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(() -> {
            if (fsConfig != null && iEslEventListener != null && !FS_CLIENT.canSend()) {
                try {
                    init(fsConfig, iEslEventListener);
                } catch (Exception e) {
                    log.warn("客户端重启失败", e);
                }
            }

        }, 10, 5, TimeUnit.SECONDS);
    }

    public FsClient() throws Exception {
    }

    public static void refreshHeartbeatTime() {
        FsClient.lastHeartbeatTimestamp = System.currentTimeMillis();
    }

    public static void init(HilinFreeswitchConfig fsConfig, IEslEventListener iEslEventListener) throws Exception {
        FsClient.fsConfig = fsConfig;
        FsClient.iEslEventListener = iEslEventListener;
        if (FS_CLIENT.canSend()) {
            // 如果客户端可用，先关闭，再启动
            FS_CLIENT.close();
        }
        FS_CLIENT.connect(fsConfig.getHost(), fsConfig.getPort(), fsConfig.getPassword(), fsConfig.getTimeout());
        refreshHeartbeatTime();
        setEventFilter();
        setEventSubscriptions();
        addEventListener();
    }


    private static void setEventFilter() {
        if (FS_CLIENT.canSend()) {
            for (FsEventType fsEventType : FsEventType.values()) {
                CommandResponse response = FS_CLIENT.addEventFilter(EslEventHeaderNames.EVENT_NAME, fsEventType.name());
                log.info("setEventFilter: {} result:{}", fsEventType.name(), response.getReplyText());
            }
        }
    }

    private static void setEventSubscriptions() {
        if (canSend()) {
            CommandResponse response = FS_CLIENT.setEventSubscriptions("plain", "ALL");
            log.info("setEventSubscriptions: plain(ALL) result:{}", response.getReplyText());
        }
    }

    private static void addEventListener() {
        if (canSend()) {
            FS_CLIENT.addEventListener(iEslEventListener);
            log.info("addEventListener");
        }
    }

    public static boolean canSend() {
        return lastHeartbeatTimestamp + 90 * 1000 > System.currentTimeMillis() && FS_CLIENT.canSend();
    }

    public static Client getFsClient() {
        return FS_CLIENT;
    }
}
