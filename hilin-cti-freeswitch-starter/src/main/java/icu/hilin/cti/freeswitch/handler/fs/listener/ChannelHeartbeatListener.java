package icu.hilin.cti.freeswitch.handler.fs.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import icu.hilin.cti.freeswitch.client.inbound.FsEventType;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import icu.hilin.cti.freeswitch.handler.fs.listener.util.LegUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@AllArgsConstructor
public class ChannelHeartbeatListener extends AbsEslEventListener {
    private final IEventHandler iEventHandler;

    @Override
    public boolean shouldDeal(HilinEslEvent eslEvent) {
        return FsEventType.HEARTBEAT.name().equalsIgnoreCase(eslEvent.getEventName());
    }

    @Override
    public void deal(HilinEslEvent eslEvent) {
        log.info("收到心跳");
        FsClient.refreshHeartbeatTime();
    }
}
