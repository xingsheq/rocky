package rocky.com.constant;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by xingsheq on 2018/3/21.
 */
public class Constants {
   public static final ChannelGroup channelGroup = new DefaultChannelGroup(
           //单线程
           GlobalEventExecutor.INSTANCE);
   public static final ChannelGroup client = new DefaultChannelGroup(
            //单线程
            GlobalEventExecutor.INSTANCE);
}
