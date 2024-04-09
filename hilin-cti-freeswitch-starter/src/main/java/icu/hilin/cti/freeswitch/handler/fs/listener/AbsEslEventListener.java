package icu.hilin.cti.freeswitch.handler.fs.listener;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.client.inbound.FsEventType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public abstract class AbsEslEventListener {


    /**
     * 事件锁，根据主叫号码进行锁定
     */
    private static final Cache<String, ReadWriteLock> EVENT_LOCKS = CacheUtil.newFIFOCache(5000, 3600 * 1000);

    /**
     * 事件是否需要处理
     *
     * @param eslEvent
     * @return
     */
    abstract public boolean shouldDeal(HilinEslEvent eslEvent);

    /**
     * 实力事件
     *
     * @param eslEvent
     */
    abstract public void deal(HilinEslEvent eslEvent);

    /**
     * starter内部调用的事件处理
     * 这个方法会自动判断是否需要进行事件处理
     *
     * @param eslEvent
     */
    public void doEvent(HilinEslEvent eslEvent) {
        log.info("处理事件{} legId:{} ani:{} dni:{}", eslEvent.getEventName(), eslEvent.getLegId(), eslEvent.getANI(), eslEvent.getDNI());
        String ani = eslEvent.getANI();

        // 如果是心跳，直接跳转到心跳处理类
        // 排除语音通知
        // 排除规则： 当主叫号码全是"0"，就认为是语音通知
        if (!FsEventType.HEARTBEAT.name().equalsIgnoreCase(eslEvent.getEventName()) && ObjectUtil.isEmpty(ani.replace("0", ""))) {
            return;
        }

        if (shouldDeal(eslEvent)) {
            ReadWriteLock lock;
            synchronized (EVENT_LOCKS) {
                if (!EVENT_LOCKS.containsKey(eslEvent.getANI())) {
                    EVENT_LOCKS.put(eslEvent.getANI(), new ReentrantReadWriteLock());
                }
                lock = EVENT_LOCKS.get(eslEvent.getANI());
            }
            lock.writeLock().lock();
            try {
                deal(eslEvent);
            } catch (Exception e) {
                log.warn("事件处理失败: " + JSONUtil.toJsonStr(eslEvent), e);
            }
            lock.writeLock().unlock();
        }
    }


}
