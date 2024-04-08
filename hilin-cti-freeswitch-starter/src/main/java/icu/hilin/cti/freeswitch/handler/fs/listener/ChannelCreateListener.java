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

import java.util.Date;

/**
 * channel_create事件处理
 * 如果是呼入，bleg才能拿到被叫号码（也就是席位号码）
 * 这也不难理解，对于呼入，fs先处理aleg，这时还未分配席位，因此dni号码是中继号码（比如2003）
 * 对于外呼和内线拨打，fs从一开始就知道是哪个坐席打给哪个号码，因此外呼的时候，dni就是被叫号码
 */
@Slf4j
@AllArgsConstructor
public class ChannelCreateListener extends AbsEslEventListener {

    private final IEventHandler iEventHandler;
    private final FsClient fsClient;

    @Override
    public boolean shouldDeal(HilinEslEvent eslEvent) {
        return FsEventType.CHANNEL_CREATE.name().equalsIgnoreCase(eslEvent.getEventName());
    }

    @Override
    public void deal(HilinEslEvent eslEvent) {
        String ani = eslEvent.getANI();
        String dni = eslEvent.getDNI();

        // 如果是心跳，设置最后心跳时间，并不做事件处理
        if (FsEventType.HEARTBEAT.name().equalsIgnoreCase(eslEvent.getEventName())) {
            return;
        }

        // 排除语音通知
        // 排除规则： 当主叫号码全是"0"，就认为是语音通知
        if (ObjectUtil.isEmpty(ani.replace("0", ""))) {
            return;
        }

        // todo 判断呼叫类型
        LegUtils.setCallType(ani, true ? EventCacheVo.CALL_TYPE_OUT : EventCacheVo.CALL_TYPE_IN);

        if (LegUtils.isALegByNum(ani)) {
            // 如果是aLeg，保存leg信息，并且发送ready事件
            // 写入a脚信息
            LegUtils.updateALegId(ani, eslEvent.getLegId());
            LegUtils.updateReadyTime(ani);
            LegUtils.getEventCacheByANI(ani);
            LegUtils.updateALegInfo(ani, eslEvent.getEventHeaders());
            LegUtils.updateALegStatus(ani, EventCacheVo.LEG_STATUS_CREATE);
            LegUtils.updateRelayNum(ani,
                    (EventCacheVo.CALL_TYPE_IN.equalsIgnoreCase(LegUtils.getCallType(ani))) ? dni : null);
            // 回调ready事件
            iEventHandler.callReady(BeanUtil.copyProperties(LegUtils.getEventCacheByANI(ani), EventCacheVo.class));
        } else {
            // 如果是bLeg，则需要先判断是否已经有bLeg的信息，如果有，那么则认为是坐席切换
            boolean hasBLeg = ObjectUtil.isNotEmpty(LegUtils.getBLegId(ani));
            LegUtils.updateRingTime(ani, new Date());
            LegUtils.updateBLegInfo(ani, eslEvent.getEventHeaders());
            LegUtils.updateBLegStatus(ani, EventCacheVo.LEG_STATUS_CREATE);
            LegUtils.updateBLegId(ani, eslEvent.getLegId());
            // 判断bleg是否存在，如果存在，那么是坐席切换
            if (hasBLeg) {
                // 坐席切换处理
                LegUtils.addSwitchInfo(ani, new EventCacheSwitchVo().setSwitchTime(new Date()).setAgent(dni)
                        .setPrevAgent(LegUtils.getDNI(ani)));
            }
            // 响铃事件处理
            LegUtils.updateDNI(ani, dni);

            if (hasBLeg) {
                // 如果是坐席切换，那么需要把挂断者字段设置为空
                // 因为坐席切换会先挂断bleg在重新创建bleg，会造成挂断原因异常
                LegUtils.updateHanguper(ani, "");
                // 如果有bleg，那么就是席位切换事件
                iEventHandler.callSwitch(BeanUtil.copyProperties(LegUtils.getEventCacheByANI(ani), EventCacheVo.class));
                // fixme 这个需要明确一下
                // 1.是否需要发送callRing事件
                // 2.上一个bLeg的hangup事件是否需要发送
                // 现在处理是都不发送
            } else {
                // 如果没有，那么就是callRing事件
                iEventHandler.callRing(BeanUtil.copyProperties(LegUtils.getEventCacheByANI(ani), EventCacheVo.class));
            }
        }

    }

}
