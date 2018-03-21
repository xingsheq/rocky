package rocky.com;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.joda.time.DateTime;
import rocky.com.curator.util.ClientFactory;

import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CuratorFramework client= ClientFactory.newClient();
        client.start();
//        System.out.println( "Hello World!" );
        try {
//            client.create().forPath("/base/test");
            client.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath("/base/monitor/hwt2000",new DateTime(new Date()).toString("yyyy-MM-dd HH:mm:ss").getBytes());
            while (true){
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }
}
