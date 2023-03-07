import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
public class Client1 {

    public static void main(String[] args) {
       //setup local cluster config
        Config config = new Config();
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(20);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("training").setBackupCount(2).setTimeToLiveSeconds(300);
        config.addMapConfig(mapConfig);
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);

        /**
         * Create a Hazelcast backed map
         */
        IMap<Integer, String> training = hazelcast.getMap("training");

        // Write the 1000 elements to the map
        for (int i = 0; i < 1000; i++) {
            int key = i;
            String value = "value-" + i;

            /**
             * Put the entry into the map
             */
            if(key == 42){
                value = training.get(42);
                String valu = value.clone
            }
            training.put(key, value);
        }
        System.out.println("Training map populated successfully!");
    }
}
