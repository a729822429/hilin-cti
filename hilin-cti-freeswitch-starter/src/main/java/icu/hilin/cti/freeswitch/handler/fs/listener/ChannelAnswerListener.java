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
public class ChannelAnswerListener extends AbsEslEventListener {
    private final IEventHandler iEventHandler;
    private final FsClient fsClient;

    @Override
    public boolean shouldDeal(HilinEslEvent eslEvent) {
        return FsEventType.CHANNEL_ANSWER.name().equalsIgnoreCase(eslEvent.getEventName());
    }

    @Override
    public void deal(HilinEslEvent eslEvent) {
        String ani = eslEvent.getANI();

        if (LegUtils.isALegByLegId(ani, eslEvent.getLegId())) {
            // 修改aLeg状态
            LegUtils.updateALegStatus(ani, EventCacheVo.LEG_STATUS_ANSWER);
        } else if (LegUtils.isBLegByLegId(ani, eslEvent.getLegId())) {
            // 修改bLeg状态
            LegUtils.updateBLegStatus(ani, EventCacheVo.LEG_STATUS_ANSWER);
            LegUtils.updateAnswerTime(ani);
            // bLeg的Answer认为是通话已经接起
            iEventHandler.callAnswer(BeanUtil.copyProperties(LegUtils.getEventCacheByANI(ani), EventCacheVo.class));
        }

    }
}
