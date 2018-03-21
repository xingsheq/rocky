package rocky.com;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import rocky.com.curator.recipe.cache.PathCacheDemo;
import rocky.com.curator.util.ClientFactory;

/**
 * Created by xingsheq on 2018/1/24.
 */
public class Monitor {
    public static void main(String[] args) {
        CuratorFramework client=ClientFactory.newClient();
        client.start();
        PathChildrenCache cache=new PathChildrenCache(client,"/base/monitor",true);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("EventType : "+event.getData().toString());
                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                    System.out.println("new app started");
                    System.out.println(event.getData().getPath()+" : "+new String(event.getData().getData()));
                }

                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    System.out.println("app exit");
                    System.out.println(event.getData().getPath()+" : "+new String(event.getData().getData()));
                }
            }
        });
        try {
            cache.start();
            while (true){
                ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
