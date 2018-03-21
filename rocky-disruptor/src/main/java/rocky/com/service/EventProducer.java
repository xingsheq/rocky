package rocky.com.service;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rocky.com.pojo.EventEntity;
import rocky.com.pojo.TrapEntity;


public class EventProducer implements Runnable{
    Logger log= LoggerFactory.getLogger(EventProducer.class);
    private final Disruptor<EventEntity> disruptor;

    public EventProducer(Disruptor<EventEntity> disruptor) {
        this.disruptor = disruptor;
    }



    @Override
    public void run() {
        int i=0;
        while (true){
            final int finalI = i;
            disruptor.publishEvent(new EventTranslator<EventEntity>() {
                @Override
                public void translateTo(EventEntity eventEntity, long l) {
                    TrapEntity entity=new TrapEntity();
                    entity.setAlarmName("alarmName" + finalI);
                    entity.setId(finalI);
                    eventEntity.setEntity(entity);

                }
            });
            i++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
