/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.util.scanAnnotation;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.scannotation.archiveiterator.DirectoryIteratorFactory;
import org.scannotation.archiveiterator.FileProtocolIteratorFactory;
import org.scannotation.archiveiterator.Filter;
import org.scannotation.archiveiterator.JarIterator;
import org.scannotation.archiveiterator.StreamIterator;

public class IteratorFactory {

	private static final ConcurrentHashMap<String, DirectoryIteratorFactory> registry = new ConcurrentHashMap<String, DirectoryIteratorFactory>();

	static {
		// http://groups.google.com/group/exadel-flamingo-users/browse_thread/thread/41d5a298bc8a4eb9
		registry.put("vfsfile", new FileProtocolIteratorFactory());// add
		registry.put("vfszip", new FileProtocolIteratorFactory());// add
		registry.put("file", new FileProtocolIteratorFactory());
	}

	public static StreamIterator create(URL url, Filter filter) throws IOException {
		String urlString = url.toString();
		if (urlString.endsWith("!/")) {
			urlString = urlString.substring(4);
			urlString = urlString.substring(0, urlString.length() - 2);
			url = new URL(urlString);
		}

		if (!urlString.endsWith("/")) {
			return new JarIterator(url.openStream(), filter);
		} else {
			DirectoryIteratorFactory factory = registry.get(url.getProtocol());
			if (factory == null)
				throw new IOException("Unable to scan directory of protocol: " + url.getProtocol());
			return factory.create(url, filter);
		}
	}

}
