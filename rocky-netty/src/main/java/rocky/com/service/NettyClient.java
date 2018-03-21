package rocky.com.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import rocky.com.service.handler.ClientHandler;

/**
 * Created by xingsheq on 2018/2/8.
 */
public class NettyClient {

    public void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        try {
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder(),new StringEncoder(),new ClientHandler());
                        }
                    });

            //绑定端口，同步阻塞等待绑定成功
            ChannelFuture f = b.connect(host, port).sync();
            //阻塞等待服务端监听端口关闭后才退出main
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        client.connect("127.0.0.1", 12128);
    }
}


