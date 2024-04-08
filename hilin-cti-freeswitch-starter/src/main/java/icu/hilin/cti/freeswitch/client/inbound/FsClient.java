package icu.hilin.cti.freeswitch.client.inbound;

import icu.hilin.cti.freeswitch.HilinFreeswitchConfig;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.transport.CommandResponse;
import org.freeswitch.esl.client.transport.event.EslEventHeaderNames;

@Slf4j
public class FsClient {

    private static final Client FS_CLIENT = new Client();

    private long lastHeartbeatTimestamp;

    private HilinFreeswitchConfig fsConfig;
    private IEslEventListener iEslEventListener;

    public FsClient(HilinFreeswitchConfig fsConfig, IEslEventListener iEslEventListener) throws Exception {
        log.info("FsClient create");
        this.fsConfig = fsConfig;
        this.iEslEventListener = iEslEventListener;
        init();
    }

    public void refreshHeartbeatTime() {
        this.lastHeartbeatTimestamp = System.currentTimeMillis();
    }

    public void init() throws Exception {
        FS_CLIENT.connect(fsConfig.getHost(), fsConfig.getPort(), fsConfig.getPassword(), fsConfig.getTimeout());
        setEventFilter();
        setEventSubscriptions();
        addEventListener();
    }


    public void setEventFilter() {
        if (FS_CLIENT.canSend()) {
            for (FsEventType fsEventType : FsEventType.values()) {
                CommandResponse response = FS_CLIENT.addEventFilter(EslEventHeaderNames.EVENT_NAME, fsEventType.name());
                log.info("监听事件: {} result:{}", fsEventType.name(), response.getReplyText());
            }
        }
    }

    private void setEventSubscriptions() {
        if (canSend()) {
            FS_CLIENT.setEventSubscriptions("plain", "ALL");
        }
    }

    private void addEventListener() {
        if (canSend()) {
            FS_CLIENT.addEventListener(iEslEventListener);
        }
    }

    public boolean canSend() {
        return lastHeartbeatTimestamp + 90 * 1000 > System.currentTimeMillis() && FS_CLIENT.canSend();
    }

    public static Client getFsClient() {
        return FS_CLIENT;
    }
}
