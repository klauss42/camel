/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.file;

import java.io.File;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;

/**
 * Unit test for the FileRenameStrategy using move options with absolute paths
 */
public class FileConsumerCommitRenameAbsolutePathStrategyTest extends ContextTestSupport {

    private String base;

    @Override
    protected void setUp() throws Exception {
        deleteDirectory("target/done");
        deleteDirectory("target/reports");
        // use current dir as base as aboslute path
        base = new File("").getAbsolutePath() + "/target";
        super.setUp();
    }

    public void testRenameSuccess() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:report");
        mock.expectedBodiesReceived("Hello Paris");
        mock.expectedFileExists("target/done/paris.txt", "Hello Paris");

        template.sendBodyAndHeader("file:target/reports", "Hello Paris", Exchange.FILE_NAME, "paris.txt");

        mock.assertIsSatisfied();
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("file://target/reports?move=" + base + "/done/${file:name}&consumer.delay=5000")
                        .convertBodyTo(String.class).to("mock:report");
            }
        };
    }
}