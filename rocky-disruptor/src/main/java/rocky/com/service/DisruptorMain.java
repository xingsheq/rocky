package rocky.com.service;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rocky.com.pojo.EventEntity;
import rocky.com.pojo.TrapEntity;
import rocky.com.service.hanlder.ConversionHandler;
import rocky.com.service.hanlder.EnrichHandler;
import rocky.com.service.hanlder.FlitHandler;
import rocky.com.service.hanlder.SendHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by xingsheq on 2018/1/16.
 */
public class DisruptorMain {
    static Logger log= LoggerFactory.getLogger(DisruptorMain.class);
    public static void main(String[] args) {


        ThreadFactory threadFactory=Executors.defaultThreadFactory();
        int bufferSize = 1024;
        Disruptor<EventEntity> disruptor=new Disruptor<EventEntity>(new TrapEventFactory(),bufferSize,threadFactory);
        Disruptor<EventEntity> disruptor2=new Disruptor<EventEntity>(new TrapEventFactory(),bufferSize,threadFactory);


        FlitHandler flitHandler=new FlitHandler(disruptor2);
        ConversionHandler conversionHandler=new ConversionHandler();
        EnrichHandler enrichHandler=new EnrichHandler();
        SendHandler sendHandler=new SendHandler();
        disruptor.handleEventsWith(flitHandler);
        disruptor2.handleEventsWith(conversionHandler,enrichHandler).then(sendHandler);
        disruptor.start();
        disruptor2.start();

        log.info("disruptor start");
        //生产者
        Executor executor = Executors.newFixedThreadPool(10);
        EventProducer producer = new EventProducer(disruptor);
        executor.execute(producer);
    }
}
