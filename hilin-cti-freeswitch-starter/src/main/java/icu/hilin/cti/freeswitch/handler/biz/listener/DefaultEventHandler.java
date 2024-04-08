package icu.hilin.cti.freeswitch.handler.biz.listener;

import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.handler.fs.listener.EventCacheVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultEventHandler implements IEventHandler {

    @Override
    public void callReady(EventCacheVo dto) {
        log.info("callReady {}", dto);
    }

    @Override
    public void callRing(EventCacheVo dto) {
        log.info("callRing {}", dto);
    }

    @Override
    public void callAnswer(EventCacheVo dto) {
        log.info("callAnswer {}", dto);
    }

    @Override
    public void callSwitch(EventCacheVo dto) {
        log.info("callSwitch {}", dto);
    }

    @Override
    public void callHangup(EventCacheVo dto) {
        log.info("callHangup {}", dto);
    }

    @Override
    public void callLeak(EventCacheVo dto) {
        log.info("callLeak {}", dto);
    }

    @Override
    public void callRecord(EventCacheVo dto) {
        log.info("callRecord {}", dto);
    }
}
