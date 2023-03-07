/*

 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.pipeline.*;
import com.hazelcast.jet.pipeline.file.FileSources;
import com.hazelcast.jet.pipeline.test.TestSources;
import com.hazelcast.jet.pipeline.test.SimpleEvent;
import com.hazelcast.map.IMap;
import com.hazelcast.map.impl.KeyValueConsumingEntryProcessor;

public class Lab1 {

    public static void main (String[] args) {
        Pipeline p = buildPipeline();
        HazelcastInstance hz = Hazelcast.bootstrappedInstance();

        hz.getJet().newJob(p).join();
    }

    private static Pipeline buildPipeline() {
        Pipeline p = Pipeline.create();

        StreamSource<Long> source = TestSources.itemStream(1, (ts, seq) -> seq);

        //BatchSource<String> presource = Sources.files("/home/gerard/IdeaProjects/Hazelcast developer essentials/hazelcast-jet-training-master/test.txt").build();
        String directory = "/home/gerard/IdeaProjects/Hazelcast developer essentials/hazelcast-jet-training-master/test";

        p.readFrom(Sources.fileWatcher(directory))
                .withoutTimestamps()
                .map( event -> Long.parseLong(event))
                .filter( event -> event % 2 == 0)
                .setName("Filter out odd numbers")
         .writeTo(Sinks.logger());

        // Run the code to see the results in the console
        // Stop it before leaving the lab

        // STEP 2: Filter out odd numbers from the stream
        //BatchStage<String> odds = odds.filter( number -> !(number % 2 = 0));

        return p;
    }
}
