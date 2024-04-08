package icu.hilin.cti.freeswitch.handler.fs.listener.util;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.ObjectUtil;
import icu.hilin.cti.freeswitch.handler.fs.listener.EventCacheSwitchVo;
import icu.hilin.cti.freeswitch.handler.fs.listener.EventCacheVo;

import java.util.*;

public class LegUtils {
    private static final Cache<String, EventCacheVo> CACHE = CacheUtil.newFIFOCache(10000, 86400 * 1000);
    private static final Cache<String, EventCacheVo> HANGUP_CACHE = CacheUtil.newFIFOCache(10000, 3 * 1000);
    private static final Object LOCK = new HashMap<>();

    /**
     * 根据主叫号码判断是否是aleg
     * 这个仅仅在channel_create事件时使用
     */
    public static boolean isALegByNum(String num) {
        synchronized (LOCK) {
            return !CACHE.containsKey(num) || ObjectUtil.isEmpty(getEventCacheByANI(num).getALegId());
        }
    }

    /**
     * 根据legId判断是否是aleg
     */
    public static boolean isALegByLegId(String ani, String legId) {
        synchronized (LOCK) {
            return legId.equalsIgnoreCase(getEventCacheByANI(ani).getALegId());
        }
    }

    /**
     * 根据legId判断是否是bLeg
     */
    public static boolean isBLegByLegId(String ani, String legId) {
        synchronized (LOCK) {
            return legId.equalsIgnoreCase(getEventCacheByANI(ani).getBLegId());
        }
    }

    public static String getBLegId(String ani) {
        return getEventCacheByANI(ani).getBLegId();
    }

    public static String getALegId(String ani) {
        return getEventCacheByANI(ani).getALegId();
    }

    public static String getDNI(String ani) {
        return getEventCacheByANI(ani).getDni();
    }

    public static EventCacheVo getEventCacheByANI(String ani) {
        synchronized (LOCK) {
            return Optional.ofNullable(CACHE.get(ani)).orElse(new EventCacheVo());
        }
    }

    /**
     * 更改挂断方
     *
     * @param ani
     * @return
     */
    public static EventCacheVo updateHanguper(String ani, String hanguper) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setHanguper(hanguper);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    /**
     * 更改挂断方，leg
     *
     * @param ani
     * @return
     */
    public static EventCacheVo updateHanguperLeg(String ani, String hanguperLeg) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setHanguperLeg(hanguperLeg);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    /**
     * 获取挂断方
     *
     * @param ani
     * @param aLegId
     * @return
     */
    public static String getHanguper(String ani) {
        synchronized (LOCK) {
            return getEventCacheByANI(ani).getHanguper();
        }
    }

    /**
     * 更新中继号码
     *
     * @param ani
     * @param aLegId
     * @return
     */
    public static EventCacheVo updateRelayNum(String ani, String relayNum) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setRelayNum(relayNum);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateALegId(String ani, String aLegId) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setALegId(aLegId);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateALegInfo(String ani, Map<String, String> aLegInfo) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setALegInfo(aLegInfo);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateALegStatus(String ani, String aLegStatus) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            switch (aLegStatus) {
                case EventCacheVo.LEG_STATUS_CREATE:
                    cacheVo.setALegPrevStatus(null);
                    cacheVo.setALegCreateTime(new Date());
                    break;
                case EventCacheVo.LEG_STATUS_ANSWER:
                    cacheVo.setALegPrevStatus(cacheVo.getALegStatus());
                    cacheVo.setALegAnswerTime(new Date());
                    break;
                case EventCacheVo.LEG_STATUS_HANGUP:
                    cacheVo.setALegPrevStatus(cacheVo.getALegStatus());
                    cacheVo.setALegHangupTime(new Date());
                    break;
                default:
                    break;
            }
            cacheVo.setALegStatus(aLegStatus);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static String getALegStatus(String ani) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            return cacheVo.getALegStatus();
        }
    }

    public static String getALegPrevStatus(String ani) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            return cacheVo.getALegPrevStatus();
        }
    }

    public static EventCacheVo updateBLegId(String ani, String bLegId) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setBLegId(bLegId);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateBLegStatus(String ani, String bLegStatus) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            switch (bLegStatus) {
                case EventCacheVo.LEG_STATUS_CREATE:
                    cacheVo.setBLegPrevStatus(null);
                    cacheVo.setBLegCreateTime(new Date());
                    break;
                case EventCacheVo.LEG_STATUS_ANSWER:
                    cacheVo.setBLegPrevStatus(cacheVo.getBLegStatus());
                    cacheVo.setBLegAnswerTime(new Date());
                    break;
                case EventCacheVo.LEG_STATUS_HANGUP:
                    cacheVo.setBLegPrevStatus(cacheVo.getBLegStatus());
                    cacheVo.setBLegHangupTime(new Date());
                    break;
                default:
                    break;
            }
            cacheVo.setBLegStatus(bLegStatus);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static String getBLegStatus(String ani) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            return cacheVo.getBLegStatus();
        }
    }

    public static String getBLegPrevStatus(String ani) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            return cacheVo.getBLegPrevStatus();
        }
    }

    public static EventCacheVo updateBLegInfo(String ani, Map<String, String> bLegInfo) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setBLegInfo(bLegInfo);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateDNI(String ani, String DNI) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setDni(DNI);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateReadyTime(String ani) {
        return updateReadyTime(ani, new Date());
    }

    public static EventCacheVo updateReadyTime(String ani, Date readyTime) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setReadyTime(readyTime);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateRingTime(String ani) {
        return updateRingTime(ani, new Date());
    }

    public static EventCacheVo updateRingTime(String ani, Date ringTime) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setRingTime(ringTime);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateAnswerTime(String ani) {
        return updateAnswerTime(ani, new Date());
    }

    public static EventCacheVo updateAnswerTime(String ani, Date answerTime) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setAnswerTime(answerTime);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo updateHangupTime(String ani) {
        return updateHangupTime(ani, new Date());
    }

    public static EventCacheVo updateHangupTime(String ani, Date hangupTIme) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setHangupTime(hangupTIme);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static EventCacheVo setCallType(String ani, String callType) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            cacheVo.setAni(ani);
            cacheVo.setCallType(callType);
            updateEventCache(cacheVo);
            return cacheVo;
        }
    }

    public static String getCallType(String ani) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            return cacheVo.getCallType();
        }
    }

    public static EventCacheVo addSwitchInfo(String ani, EventCacheSwitchVo switchVo) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            if (ObjectUtil.isNull(cacheVo.getSwitchVos())) {
                cacheVo.setSwitchVos(new ArrayList<>());
            }
            cacheVo.getSwitchVos().add(switchVo);
            return cacheVo;
        }
    }

    private static void updateEventCache(EventCacheVo cacheVo) {
        synchronized (LOCK) {
            if (ObjectUtil.isNotEmpty(cacheVo) && ObjectUtil.isNotEmpty(cacheVo.getAni())) {
                CACHE.put(cacheVo.getAni(), cacheVo);
            }
        }
    }

    public static EventCacheVo removeCache(String ani) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            CACHE.remove(ani);
            HANGUP_CACHE.put(ani, cacheVo, 3000);
            return cacheVo;
        }
    }

    public static EventCacheVo updateRecordPath(String ani, String legId, String recordPath) {
        synchronized (LOCK) {
            EventCacheVo cacheVo = getEventCacheByANI(ani);
            // 先去缓存找，判断legid，如果相等，那么直接修改然后返回
            if (legId.equalsIgnoreCase(cacheVo.getALegId()) || legId.equalsIgnoreCase(cacheVo.getBLegId())) {
                cacheVo.setRecordPath(recordPath);
                CACHE.put(ani, cacheVo);
                return cacheVo;
            } else {
                // 否则从HANGUP_CACHE中找
                cacheVo = HANGUP_CACHE.get(ani);
                return (ObjectUtil.isEmpty(cacheVo) || ObjectUtil.isEmpty(cacheVo.getALegId())) ? null : cacheVo;
            }
        }
    }
}
