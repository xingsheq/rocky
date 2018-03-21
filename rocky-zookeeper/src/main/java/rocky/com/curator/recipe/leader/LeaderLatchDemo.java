package rocky.com.curator.recipe.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;

/**
 * Created by xingsheq on 2018/1/23.
 */
public class LeaderLatchDemo {
    protected static String PATH = "/francis/leader";
    private static final int CLIENT_QTY = 10;


    public static void main(String[] args) throws Exception {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderLatch> examples = Lists.newArrayList();
        TestingServer server=new TestingServer();
        try {
            for (int i = 0; i < CLIENT_QTY; i++) {
                CuratorFramework client
                        = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(20000, 3));
                clients.add(client);
                LeaderLatch latch = new LeaderLatch(client, PATH, "Client #" + i);
                latch.addListener(new LeaderLatchListener() {

                    @Override
                    public void isLeader() {
                        System.out.println("I am Leader : "+latch.getId());

                    }

                    @Override
                    public void notLeader() {
                        System.out.println("I am not Leader : "+latch.getId());
                    }
                });
                examples.add(latch);
                client.start();
                latch.start();
            }
            Thread.sleep(10000);
            LeaderLatch currentLeader = null;
            for (LeaderLatch latch : examples) {
                if (latch.hasLeadership()) {
                    currentLeader = latch;
                }
            }
            System.out.println("current leader is " + currentLeader.getId());
            System.out.println("release the leader " + currentLeader.getId());
            currentLeader.close();

            Thread.sleep(5000);

            for (LeaderLatch latch : examples) {
                if (latch.hasLeadership()) {
                    currentLeader = latch;
                }
            }
            System.out.println("current leader is " + currentLeader.getId());
            System.out.println("release the leader " + currentLeader.getId());
        } finally {
            for (LeaderLatch latch : examples) {
                if (null != latch.getState())
                    CloseableUtils.closeQuietly(latch);
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }

}
