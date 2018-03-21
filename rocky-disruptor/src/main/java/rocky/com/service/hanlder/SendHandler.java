package rocky.com.service.hanlder;


import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rocky.com.pojo.EventEntity;
import rocky.com.pojo.TrapEntity;


public class SendHandler implements EventHandler<EventEntity> {
    Logger log= LoggerFactory.getLogger(SendHandler.class);
    @Override
    public void onEvent(EventEntity entity, long seq, boolean endOfBatch) throws Exception {
        log.info("发送"+entity.getEntity().getAlarmName());
    }
}
