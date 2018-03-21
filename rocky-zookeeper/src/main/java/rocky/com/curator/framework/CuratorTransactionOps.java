package rocky.com.curator.framework;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 未验证成功
 */
public class CuratorTransactionOps {
    private static String zkHosts="135.251.223.97:2181";
    static CuratorFramework client;
    public static void main(String[] args) {
       connect();
        doSthInTransaction();

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

    private static void doSthInTransaction(){

        try {
             client.inTransaction().check().forPath("/path")
                     .and()
                    .create().forPath("/path","newData".getBytes())
                    .and()
                    .setData().forPath("/parent", "data6".getBytes())
                    .and()
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
