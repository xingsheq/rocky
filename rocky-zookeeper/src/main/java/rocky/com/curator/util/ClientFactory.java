package rocky.com.curator.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;


public class ClientFactory {

    private static String zkHosts="135.251.223.97:2181";

    public static CuratorFramework newClient(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
       return
                CuratorFrameworkFactory.builder()
                        .connectString(zkHosts)
//                        .sessionTimeoutMs(5000)
//                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
//                        .namespace("base")
                        .build();
    }

}
