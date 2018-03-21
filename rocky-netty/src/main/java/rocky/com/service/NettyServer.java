package rocky.com.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import rocky.com.constant.Constants;
import rocky.com.service.handler.ChildHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyServer {

    public void bind(int port){
        EventLoopGroup masterGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        ServerBootstrap b=new ServerBootstrap();
        try {
        b.group(masterGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler())
                .childHandler(new ChildHandler());
            //绑定端口，同步阻塞等待绑定成功
            ChannelFuture f=b.bind(port).sync();
            //阻塞等待服务端监听端口关闭后才退出main
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static void send(String msg){
        System.out.println("send msg : "+msg);
        Constants.channelGroup.writeAndFlush(msg);
    }

    public static void  heartBeatCmd(){
        ScheduledExecutorService executorService= Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("heartBeatCmd::channels.size = "+Constants.channelGroup.size());
                if (Constants.channelGroup.size() > 0) {
                    send("heartbeat");
                }
            }
        }, 1000L, 5000L, TimeUnit.MILLISECONDS);

    }


    public static void main(String[] args) {
        NettyServer server=new NettyServer();
        heartBeatCmd();
        server.bind(12128);

    }
}


