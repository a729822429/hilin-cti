package icu.hilin.cti.freeswitch.handler.biz.listener;

import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.handler.fs.listener.EventCacheVo;

public interface IEventHandler {

    /**
     * 呼叫ready事件
     */
    void callReady(EventCacheVo dto);

    /**
     * 响铃事件
     * 这个是bLeg创建时触发
     */
    void callRing(EventCacheVo dto);

    /**
     * 接听事件
     *
     * @param dto
     */
    void callAnswer(EventCacheVo dto);

    /**
     * 通话切换事件，这个事件仅当呼入才有可能发生
     * 当席位超过一定时间没有接听时，就会把通话切换到另一个空闲席位，这时就会触发这个事件
     *
     * @param dto
     */
    void callSwitch(EventCacheVo dto);

    /**
     * 挂断事件
     * 这个挂断事件是整个通话挂断
     *
     * @param dto
     */
    void callHangup(EventCacheVo dto);

    /**
     * 漏接事件
     *
     * @param dto
     */
    void callLeak(EventCacheVo dto);

    /**
     * 录音事件
     * <p>
     * 推送录音文件
     *
     * @param dto
     */
    void callRecord(EventCacheVo dto);

}
