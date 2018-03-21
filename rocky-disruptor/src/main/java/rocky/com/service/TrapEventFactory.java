package rocky.com.service;

import com.lmax.disruptor.EventFactory;
import rocky.com.pojo.EventEntity;
import rocky.com.pojo.TrapEntity;

/**
 * Created by xingsheq on 2018/1/16.
 */
public class TrapEventFactory implements EventFactory<EventEntity> {
    @Override
    public EventEntity newInstance() {
        return new EventEntity();
    }
}
