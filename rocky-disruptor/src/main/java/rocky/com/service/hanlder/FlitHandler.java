package rocky.com.service.hanlder;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rocky.com.pojo.EventEntity;
import rocky.com.pojo.TrapEntity;


public class FlitHandler implements EventHandler<EventEntity> {
    Logger log= LoggerFactory.getLogger(FlitHandler.class);

    Disruptor<EventEntity> disruptor;
    public FlitHandler(Disruptor<EventEntity> disruptor) {
        this.disruptor=disruptor;
    }

    @Override
    public void onEvent(final EventEntity entity, long seq, boolean endOfBatch) throws Exception {
        if(entity.getEntity().getId()%2==0){
            log.info("屏蔽"+entity.getEntity().getAlarmName());
        }else {

            disruptor.publishEvent(new EventTranslator<EventEntity>() {
                @Override
                public void translateTo(EventEntity eventEntity, long l) {
                    eventEntity.setEntity(entity.getEntity());
                }
            });
        }
    }
}
