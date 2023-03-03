import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class MapWriter {

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

        //create map, add entry, update entry, delete entry
        IMap<String, String> map = client.getMap("someMap");
        String key = "" + System.nanoTime();
        String value = "1";
        map.put(key, value);
        map.put(key, "2");
        map.delete(key);

        System.exit(0);
    }
}