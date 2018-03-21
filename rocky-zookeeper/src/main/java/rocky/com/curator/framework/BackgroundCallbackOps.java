package rocky.com.curator.framework;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xingsheq on 2018/1/23.
 */
public class BackgroundCallbackOps {
    private static String zkHosts="135.251.223.97:2181";
    static CuratorFramework client;

    public static void main(String[] args) {
        connect();
        callBackOps();
    }
    private static void connect(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client =
                CuratorFrameworkFactory.builder()
                        .connectString(zkHosts)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .namespace("base")
                        .build();
        client.start();
    }

    /**
     * 响应码	意义
     0	OK，即调用成功
     -4	ConnectionLoss，即客户端与服务端断开连接
     -110	NodeExists，即节点已经存在
     -112	SessionExpired，即会话过期
     */
    private static void callBackOps(){
        Executor executor = Executors.newFixedThreadPool(2);
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .inBackground((curatorFramework, curatorEvent) ->
                    {
                        System.out.println(String.format("eventType:%s,resultCode:%s",
                                curatorEvent.getType(), curatorEvent.getResultCode()));
                    }, executor)
                    .forPath("/ephemeral");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
