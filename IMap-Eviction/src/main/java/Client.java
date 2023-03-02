import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.eviction.impl.comparator.LFUEvictionPolicyComparator;
import com.hazelcast.map.IMap;
import static com.hazelcast.client.impl.spi.impl.discovery.HazelcastCloudDiscovery.CLOUD_URL_BASE_PROPERTY;
import static com.hazelcast.client.properties.ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN;
import static com.hazelcast.client.properties.ClientProperty.STATISTICS_ENABLED;

public class Client {
    public static void main(String[] args) {
        //setup local cluster config
        Config config = new Config();
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(20);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("training-eviction").setBackupCount(2).setTimeToLiveSeconds(300);
        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(EvictionPolicy.LFU);
        evictionConfig.setMaxSizePolicy(MaxSizePolicy.USED_HEAP_SIZE);
        evictionConfig.setSize(1);
        mapConfig.setEvictionConfig(evictionConfig);
        config.addMapConfig(mapConfig);
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);

        /**
         * Create a Hazelcast backed map
         */
        IMap<Integer, String> map = hazelcast.getMap("training-eviction");

        final int NUMBER_OF_ITEMS_TO_LOAD = 25000;
        // Write elements to the map
        for (int i = 0; i < NUMBER_OF_ITEMS_TO_LOAD; i++) {
            int key = i;
            String value = String.valueOf(i);

            // Put the entry into the map
            map.put(key, value);

            if (i % 100 == 0){
                System.out.println("Number of loaded items: " + i + " out of " + NUMBER_OF_ITEMS_TO_LOAD);
            }
        }

        // Checking map size to observe eviction
        System.out.println("Map size after eviction: " + map.size());
    }
}