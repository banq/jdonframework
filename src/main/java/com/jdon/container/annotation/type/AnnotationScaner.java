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
package com.jdon.container.annotation.type;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.jdon.container.annotation.AnnotationUtil;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.scanAnnotation.ScanAnnotationDB;

public class AnnotationScaner {

	private ScanAnnotationDB db;

	private FutureTask<ScanAnnotationDB> ft;

	public Map<String, Set<String>> getScannedAnnotations(AppContextWrapper context) {
		if (db != null)
			return db.getAnnotationIndex();

		try {
			db = ft.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return db.getAnnotationIndex();
	}

	public void startScan(final AppContextWrapper context) {

		this.ft = new FutureTask(new Callable<ScanAnnotationDB>() {
			public ScanAnnotationDB call() throws Exception {
				ScanAnnotationDB db = new ScanAnnotationDB();
				URL[] urls = AnnotationUtil.scanAnnotation(context);
				try {
					db.scanArchives(urls);
				} catch (Exception e) {
					System.err.print("[JdonFramework] scanAnnotation error:" + e);
				}
				return db;
			}
		});
		ft.run();

	}

}
