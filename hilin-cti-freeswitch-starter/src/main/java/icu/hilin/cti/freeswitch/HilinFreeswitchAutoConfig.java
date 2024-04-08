package icu.hilin.cti.freeswitch;

import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import icu.hilin.cti.freeswitch.client.inbound.HilinEslListener;
import icu.hilin.cti.freeswitch.handler.biz.listener.DefaultEventHandler;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(
        prefix = "icu.hilin.freeswitch",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties({
        HilinFreeswitchConfig.class
})
@Slf4j
public class HilinFreeswitchAutoConfig {

    @Autowired
    private HilinFreeswitchConfig fsConfig;
    @Autowired
    private ApplicationContext context;

    private IEventHandler iEventHandler = new DefaultEventHandler();

    @Autowired(required = false)
    public void setiEventHandler(IEventHandler iEventHandler) {
        this.iEventHandler = iEventHandler;
    }

    public HilinEslListener hilinEslListener() {
        return new HilinEslListener(context, iEventHandler);
    }

    @Bean
    public FsClient fsClient() {
        try {
            return new FsClient(fsConfig, hilinEslListener());
        } catch (Exception e) {
            log.warn("fs客户端初始化失败", e);
            System.exit(0);
        }
        return null;
    }
}