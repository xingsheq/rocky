package rocky.com.curator.framework;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.List;

/**
 *  .namespace("base") 指定客户端工作目录，base必须先create
 * create delete的node路径必须带/ 否则异常Path must start with / character
 * create 如果node存在，异常NodeExists
 * delete 如果node不存在，异常NoNode
 * creatingParentContainersIfNeeded临时节点/parent/childPath，实际结果为：子节点为临时节点，父节点为永久节点
 * setData().withVersion(5).forPath 指定的Version必须是当前最新version，否则会异常 BadVersion
 */
public class CuratorNodeOps {
    private static String zkHosts="135.251.223.97:2181";
    static CuratorFramework client;
    public static void main(String[] args) {

        connect();
        createNode();
        isExists();
//        getAllChildPath();
//        deleteNode();
        updateNode();
        isExists();

        getNode();
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

    private static void createNode(){
                /* create node default value is ""; */
        try {
            client.create().forPath("/path");
//         create node default  value = init
            client.create().forPath("/pathWithValue","init".getBytes());
            //create temp node value = ""
            client.create().withMode(CreateMode.EPHEMERAL).forPath("/tempPath");
            //create temp node value = "int "
            client.create().withMode(CreateMode.EPHEMERAL).forPath("/tempPathWithValue","temp".getBytes());
            //auto create parent node if need,
            //实际生成结果：childPath为临时节点，parent为永久节点
            client.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath("/parent/childPath", "creatingParentContainersIfNeeded".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteNode(){

        try {
            client.delete().forPath("/path");
            //auto delete child if need
            client.delete().deletingChildrenIfNeeded().forPath("/parent");
            //delete path of version xx
            client.delete().withVersion(0).forPath("/pathWithValue");
            //保证删除，只要session有效，就持续删除，直到成功
            client.delete().guaranteed().forPath("/tempPath");
            //可自由组合
            client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(0).forPath("/tempPathWithValue");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void getNode(){

        try {
            byte[] result=  client.getData().forPath("/path");
            System.out.println(new String(result));
            Stat stat = new Stat();
            //同时获得状态
            result = client.getData().storingStatIn(stat).forPath("/path");
            System.out.println(stat.getVersion());
            System.out.println(new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateNode(){

        try {
//            Stat stat = client.setData().forPath("/path","data".getBytes());
            //指定版本更新
            Stat stat = client.setData().withVersion(5).forPath("/path","data".getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void  isExists(){
        try {
            Stat stat =  client.checkExists().forPath("/path");
            stat.getVersion();
            System.out.println(stat.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void  getAllChildPath(){
        try {
            List<String> paths=client.getChildren().forPath("/parent");
            System.out.println(Arrays.toString(paths.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
