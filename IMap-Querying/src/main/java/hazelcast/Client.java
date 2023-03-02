package hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.impl.predicates.SqlPredicate;

import java.util.Collection;
import java.util.Random;

public class Client {

    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        // Setting up cloud configuration
        Config config = new Config();
        config.setProperty("hazelcast.client.statistics.enabled","true");
        config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).setPortCount(20);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("training-queries").setBackupCount(2).setTimeToLiveSeconds(300);
        config.addMapConfig(mapConfig);
       // config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
       // config.setClusterName("YOUR_CLUSTER_NAME");

        //adding Employee factory to populate map
        config.getSerializationConfig().addPortableFactoryClass(Employee.FACTORY_ID, Employee.EmployeeFactory.class);
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = Hazelcast.newHazelcastInstance(config);

        // Create a Hazelcast backed map
        IMap<Integer, Employee> map = client.getMap("training-queries");

        // Write elements to the map
        System.out.print("Pushing data... ");

        long start1 = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Employee emp = new Employee(20 + new Random().nextInt(30), new Random().nextInt(5000));
            map.set(i, emp);
            System.out.println(map.get(i));
        }
        long delta1 = System.currentTimeMillis() - start1;

        System.out.println("done. " + delta1 + " ms");

        System.out.print("Querying data... ");
        long start2 = System.currentTimeMillis();

        // Use a predicate to retrieve the employees with a salary between 0 and 2000
        /**
         * SQL Predicate
         */
        SqlPredicate p1 = new SqlPredicate("salary >=0 AND salary <= 2000");

        /**
         * Boolean predicate
         */

        // Either predicate here should return same result
        Collection<Employee> matches = map.values(p1);
        System.out.println("done.");

        //  Print out the results
        for (Employee emp : matches) {
            System.out.println(emp);
        }

        System.out.println("Total matches: " + matches.size() + " out of " + map.size());
        System.out.println("Elapsed time for query " + (System.currentTimeMillis() - start2) + " ms");

        client.shutdown();
    }
}

