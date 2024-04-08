package icu.hilin.cti.freeswitch.handler.fs.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.client.inbound.FsClient;
import icu.hilin.cti.freeswitch.client.inbound.FsEventType;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import icu.hilin.cti.freeswitch.handler.fs.listener.util.LegUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ChannelHangupListener extends AbsEslEventListener {
    private final IEventHandler iEventHandler;
    private final FsClient fsClient;

    @Override
    public boolean shouldDeal(HilinEslEvent eslEvent) {
        return FsEventType.CHANNEL_HANGUP.name().equalsIgnoreCase(eslEvent.getEventName());
    }

    @Override
    public void deal(HilinEslEvent eslEvent) {
        String ani = eslEvent.getANI();
        changeLegStatus(eslEvent);
        if (ObjectUtil.isNotEmpty(LegUtils.getALegId(ani)) && ObjectUtil.isNotEmpty(LegUtils.getBLegId(ani))) {
            // 如果有两只脚，则需要判断另一只脚
            // 另一只脚也挂断了，那么整个通话就挂断了，认为通话结束了
            if (EventCacheVo.LEG_STATUS_HANGUP.equalsIgnoreCase(LegUtils.getALegStatus(ani))
                    && EventCacheVo.LEG_STATUS_HANGUP.equalsIgnoreCase(LegUtils.getBLegStatus(ani))) {
                // 两只脚都挂断了
                // 这里有两种可能
                // 1. 漏接：当bLeg状态是create，那么就是挂断
                // 2. 正常挂断: 两只脚都是从answer转变为hangup
                LegUtils.updateHangupTime(ani);
                EventCacheVo cacheVo = LegUtils.getEventCacheByANI(ani);
                iEventHandler.callHangup(BeanUtil.copyProperties(cacheVo, EventCacheVo.class));
                if (EventCacheVo.LEG_STATUS_CREATE.equalsIgnoreCase(LegUtils.getBLegPrevStatus(ani))
                        || EventCacheVo.LEG_STATUS_CREATE.equalsIgnoreCase(LegUtils.getALegPrevStatus(ani))) {
                    iEventHandler.callLeak(BeanUtil.copyProperties(cacheVo, EventCacheVo.class));
                }
                LegUtils.removeCache(ani);
            } else {
                // 只有一只脚挂断
                // todo 是否需要发起通知
            }
        } else {
            // 如果只有一只脚，那么整个通话就挂断了，进入漏接
            LegUtils.updateHangupTime(ani);
            iEventHandler.callLeak(BeanUtil.copyProperties(LegUtils.getEventCacheByANI(ani), EventCacheVo.class));
            LegUtils.removeCache(ani);
        }
    }

    private void changeLegStatus(HilinEslEvent eslEvent) {
        String ani = eslEvent.getANI();

        // 修改对应脚的状态
        if (eslEvent.getLegId().equalsIgnoreCase(LegUtils.getALegId(ani))) {
            LegUtils.updateALegStatus(ani, EventCacheVo.LEG_STATUS_HANGUP);
            // 如果是aleg先挂断，则是主叫方挂断
            // 判断是否是aleg先挂断就是判断bleg状态，如果bleg不存在或者bleg不属于hangup状态，那么就是主叫挂断
            // 这个判断仅用在第一只脚挂断的情况，也就是hanguper为空的时候
            if (ObjectUtil.isEmpty(LegUtils.getHanguper(ani))) {
                if (ObjectUtil.isEmpty(LegUtils.getBLegStatus(ani))
                        || !LegUtils.getBLegStatus(ani).equalsIgnoreCase(EventCacheVo.LEG_STATUS_HANGUP)) {
                    LegUtils.updateHanguper(ani,
                            EventCacheVo.CALL_TYPE_IN.equalsIgnoreCase(LegUtils.getCallType(ani)) ?
                                    EventCacheVo.HANGUPER_USER : EventCacheVo.HANGUPER_AGENT);
                    LegUtils.updateHanguperLeg(ani, EventCacheVo.HANGUPER_ALEG);
                }
            }
        } else if (eslEvent.getLegId().equalsIgnoreCase(LegUtils.getBLegId(ani))) {
            LegUtils.updateBLegStatus(ani, EventCacheVo.LEG_STATUS_HANGUP);
            // 如果是bleg先挂断，则是被叫方挂断
            // 判断是否是bleg先挂断就是判断aleg状态，如果bleg不存在或者bleg不属于hangup状态，那么就是主叫挂断
            // 这个判断仅用在第一只脚挂断的情况，也就是hanguper为空的时候
            if (ObjectUtil.isEmpty(LegUtils.getHanguper(ani))) {
                if (ObjectUtil.isEmpty(LegUtils.getALegStatus(ani))
                        || !LegUtils.getALegStatus(ani).equalsIgnoreCase(EventCacheVo.LEG_STATUS_HANGUP)) {
                    LegUtils.updateHanguper(ani,
                            EventCacheVo.CALL_TYPE_IN.equalsIgnoreCase(LegUtils.getCallType(ani)) ?
                                    EventCacheVo.HANGUPER_AGENT : EventCacheVo.HANGUPER_USER);
                    LegUtils.updateHanguperLeg(ani,
                            EventCacheVo.HANGUPER_BLEG);
                }
            }
        }
    }

}
