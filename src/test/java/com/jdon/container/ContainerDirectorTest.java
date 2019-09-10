/*
 * Copyright 2003-2005 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.container;

import junit.framework.TestCase;

import com.jdon.container.builder.ContainerRegistryBuilder;
import com.jdon.container.builder.ContainerDirector;
import com.jdon.container.builder.StartupException;
import com.jdon.container.factory.ContainerBuilderFactory;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class ContainerDirectorTest extends TestCase {

    ContainerDirector containerDirector;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ContainerBuilderFactory containerBuilderFactory = new ContainerBuilderFactory();

        ContainerRegistryBuilder containerBuilder = containerBuilderFactory
                .createContainerBuilderForTest("container.xml", "aspect.xml");
        containerDirector = new ContainerDirector(
                containerBuilder);
        
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        containerDirector = null;
    }

    public void testStartup() throws StartupException,
                                     Exception{
            containerDirector.prepareAppRoot("");
            containerDirector.startup();

    }

}
