import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Client2 {

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

        /**
         * Get key 42 from the map and store the value
         */
        String entry42 = training.get(42);

        /**
         * Print the result to the console
         */
        System.out.println("Entry 42 is: " + entry42);

        /**
         * Convert entry 42
         */
        training.put(42, "Transformed entry");

        /**
         * Print converted entry
         */
        System.out.println("Modified entry 42 is: " + training.get(42));
        for (int i = 0; i < 1000; i++) {
            int key = i;
            String value = training.get(key);;
            value = value + "transform " + key;

            /**
             * Put the entry into the map
             */
            if(key == 42){
                value = "overwrite-test";
            }
            training.put(key, value);
            System.out.println(training.get(key));
        }
    }
}
