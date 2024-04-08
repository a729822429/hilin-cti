package icu.hilin.cti.freeswitch.handler.fs.listener;

import cn.hutool.core.bean.BeanUtil;
import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import icu.hilin.cti.freeswitch.client.inbound.FsEventType;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import icu.hilin.cti.freeswitch.handler.fs.listener.util.LegUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ChannelRecordListener extends AbsEslEventListener {
    private final IEventHandler iEventHandler;
    private final FsClient fsClient;

    @Override
    public boolean shouldDeal(HilinEslEvent eslEvent) {
        return FsEventType.RECORD_STOP.name().equalsIgnoreCase(eslEvent.getEventName());
    }

    @Override
    public void deal(HilinEslEvent eslEvent) {
        String ani = eslEvent.getANI();
        EventCacheVo eventCacheVo = LegUtils.updateRecordPath(ani, eslEvent.getLegId(), eslEvent.getRecordpath());
        iEventHandler.callRecord(BeanUtil.copyProperties(eventCacheVo, EventCacheVo.class));
    }
}
