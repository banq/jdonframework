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

import java.io.InputStream;
import java.net.URL;

import org.scannotation.AnnotationDB;
import org.scannotation.archiveiterator.Filter;
import com.jdon.util.scanAnnotation.IteratorFactory;
import org.scannotation.archiveiterator.StreamIterator;

public class ScanAnnotationDB extends AnnotationDB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void scanArchives(URL... urls) {
		for (URL url : urls) {
			Filter filter = new Filter() {
				public boolean accepts(String filename) {
					if (filename.endsWith(".class")) {
						if (filename.startsWith("/"))
							filename = filename.substring(1);
						if (!ignoreScan(filename.replace('/', '.')))
							return true;
						// System.out.println("IGNORED: " + filename);
					}
					return false;
				}
			};

			try {
				StreamIterator it = IteratorFactory.create(url, filter);

				InputStream stream;
				while ((stream = it.next()) != null)
					scanClass(stream);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private boolean ignoreScan(String intf) {
		for (String ignored : ignoredPackages) {
			if (intf.startsWith(ignored + ".")) {
				return true;
			} else {
				// System.out.println("NOT IGNORING: " + intf);
			}
		}
		return false;
	}

}
