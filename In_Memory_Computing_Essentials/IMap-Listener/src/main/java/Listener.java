import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;

public class Listener {

    public static void main(String[] args) {
        // Setting up local cluster configuration
        Config config = new Config();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(20);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("someMap").setBackupCount(2).setTimeToLiveSeconds(300);
        config.addMapConfig(mapConfig);
        config.setClusterName("dev");

        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client;
        client = Hazelcast.newHazelcastInstance(config);

        // Create map and add listener
        IMap<String, String> map = client.getMap("someMap");
        map.addEntryListener(new MyEntryListener(), true);
        System.out.println("EntryListener registered");
    }

    // create listener - print event when entry added, updated, or deleted
    private static class MyEntryListener implements EntryAddedListener<String, String>,
            EntryRemovedListener<String, String>, EntryUpdatedListener<String, String> {
        @Override
        public void entryAdded(EntryEvent<String, String> event) {
            System.out.println("entryAdded: " + event);
        }

        @Override
        public void entryRemoved(EntryEvent<String, String> event) {
            System.out.println("entryRemoved: " + event);
        }

        @Override
        public void entryUpdated(EntryEvent<String, String> event) {
            System.out.println("entryUpdated: " + event);
        }
    }
}