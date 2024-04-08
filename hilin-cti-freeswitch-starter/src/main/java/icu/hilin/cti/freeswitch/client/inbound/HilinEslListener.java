package icu.hilin.cti.freeswitch.client.inbound;

import icu.hilin.cti.core.entity.HilinEslEvent;
import icu.hilin.cti.freeswitch.handler.biz.listener.IEventHandler;
import icu.hilin.cti.freeswitch.handler.fs.listener.AbsEslEventListener;
import lombok.AllArgsConstructor;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.springframework.context.ApplicationContext;

@AllArgsConstructor
public class HilinEslListener implements IEslEventListener {
    private final ApplicationContext context;
    private final IEventHandler iEventHandler;

    @Override
    public void eventReceived(EslEvent eslEvent) {
        HilinEslEvent hilinEslEvent = new HilinEslEvent(eslEvent);
        context.getBeansOfType(AbsEslEventListener.class)
                .forEach((eventListenerName, absEslEventListener) -> {
                    if (absEslEventListener.shouldDeal(hilinEslEvent)){
                        absEslEventListener.doEvent(hilinEslEvent);
                    }
                });
    }

    @Override
    public void backgroundJobResultReceived(EslEvent eslEvent) {

    }
}
