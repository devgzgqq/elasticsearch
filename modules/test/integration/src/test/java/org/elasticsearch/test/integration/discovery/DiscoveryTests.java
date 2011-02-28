/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.test.integration.discovery;

import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Test
public class DiscoveryTests extends AbstractNodesTests {

    @AfterTest public void closeNodes() {
        closeAllNodes();
    }

    @Test public void testUnicastDiscovery() {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("discovery.zen.multicast.enabled", false)
                .put("discovery.zen.unicast.hosts", "localhost")
                .build();

        startNode("node1", settings);
        startNode("node2", settings);

        ClusterState state = client("node1").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(2));

        state = client("node2").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(2));
    }
}