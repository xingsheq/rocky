package rocky.com.service.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateEvent;
import rocky.com.constant.Constants;

import java.net.InetSocketAddress;
import java.util.Date;


public class ChildHandler extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new StringDecoder(),
                new StringEncoder(),
                new ServerHandler());

    }

    class ServerHandler extends SimpleChannelInboundHandler {
        private int UNCONNECT_NUM = 0;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            InetSocketAddress address =  (InetSocketAddress)ctx.channel().remoteAddress();
            System.out.println("receive new client : "+address.getHostName()+":"+address.getPort());
            Constants.channelGroup.add(ctx.channel());

        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            InetSocketAddress address =  (InetSocketAddress)ctx.channel().remoteAddress();
            System.out.println("receive message from client "+address.getHostName()+":"+address.getPort()+" : "+msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            System.out.println("exceptionCaught:"+cause.getMessage());
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                if(UNCONNECT_NUM >= 4) {
                    System.err.println("connect status is disconnect.");
                    ctx.close();
                    //此处当重启次数达到4次之后，关闭此链接后，并重新请求进行一次登录请求
                    return;
                }

                IdleStateEvent e = (IdleStateEvent) evt;
                switch (e.state()) {
                    case WRITER_IDLE:
                        System.out.println("send heartbeat to server---date=" + new Date());
                        String heartbeat="heartbeat";
                        ctx.writeAndFlush(heartbeat);
                        UNCONNECT_NUM++;
                        System.err.println("writer_idle over. and UNCONNECT_NUM=" + UNCONNECT_NUM);
                        break;
                    case READER_IDLE:
                        System.err.println("reader_idle over.");
                        UNCONNECT_NUM++;
                        //读取服务端消息超时时，直接断开该链接，并重新登录请求，建立通道
                    case ALL_IDLE:
                        System.err.println("all_idle over.");
                        UNCONNECT_NUM++;
                        //读取服务端消息超时时，直接断开该链接，并重新登录请求，建立通道
                    default:
                        break;
                }
            }
        }
    }
}
