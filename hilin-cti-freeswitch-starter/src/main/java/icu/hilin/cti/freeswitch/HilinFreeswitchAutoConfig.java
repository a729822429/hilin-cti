package icu.hilin.cti.freeswitch;

import cn.hutool.json.JSONUtil;
import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import icu.hilin.cti.freeswitch.client.inbound.HilinEslListener;
import icu.hilin.cti.freeswitch.client.inbound.cmd.ExtensionNumberCmd;
import icu.hilin.cti.freeswitch.client.outbound.OutboundServer;
import icu.hilin.cti.freeswitch.handler.biz.listener.DefaultEventHandler;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import icu.hilin.cti.freeswitch.handler.fs.listener.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class HilinFreeswitchAutoConfig implements ApplicationRunner {

    @Autowired
    private HilinFreeswitchConfig fsConfig;
    @Autowired
    private ApplicationContext context;

    private IEventHandler iEventHandler = new DefaultEventHandler();
    private HilinEslListener hilinEslListener;

    @Autowired(required = false)
    public void setiEventHandler(IEventHandler iEventHandler) {
        this.iEventHandler = iEventHandler;
    }

    @Bean
    public HilinEslListener hilinEslListener() {
        hilinEslListener = new HilinEslListener(context, iEventHandler);
        return hilinEslListener;
    }

    @Bean
    public ChannelCreateListener channelCreateListener() {
        return new ChannelCreateListener(iEventHandler);
    }

    @Bean
    public ChannelAnswerListener channelAnswerListener() {
        return new ChannelAnswerListener(iEventHandler);
    }

    @Bean
    public ChannelHangupListener channelHangupListener() {
        return new ChannelHangupListener(iEventHandler);
    }

    @Bean
    public ChannelHeartbeatListener channelHeartbeatListener() {
        return new ChannelHeartbeatListener(iEventHandler);
    }

    @Bean
    public ChannelRecordListener channelRecordListener() {
        return new ChannelRecordListener(iEventHandler);
    }

    @Bean
    public OutboundServer obClient() {
        return new OutboundServer();
    }

    private ExtensionNumberCmd extensionNumberCmd;
    @Bean
    public ExtensionNumberCmd extensionNumberCmd() {
        extensionNumberCmd =  new ExtensionNumberCmd();
        return extensionNumberCmd;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        FsClient.init(fsConfig, hilinEslListener);
        System.out.println(JSONUtil.toJsonStr(extensionNumberCmd.getAll()));
    }
}