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

import java.util.Set;

import com.jdon.annotation.Introduce;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaDefHolder;
import com.jdon.container.annotation.AnnotationHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.interceptor.IntroduceInfoHolder;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;

public class InroduceLoader {

	public final static String module = InroduceLoader.class.getName();

	private AnnotationScaner annotationScaner;

	private IntroduceInfoHolder introduceInfoHolder;

	public InroduceLoader(AnnotationScaner annotationScaner, IntroduceInfoHolder introduceInfoHolder) {
		super();
		this.annotationScaner = annotationScaner;
		this.introduceInfoHolder = introduceInfoHolder;
	}

	public void loadAnnotationIntroduceInfos(AnnotationHolder annotationHolder, AppContextWrapper context, ContainerWrapper containerWrapper) {
		Set<String> classes = annotationScaner.getScannedAnnotations(context).get(Introduce.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation IntroduceInfo size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationIntroduceInfoClass((String) className, annotationHolder, containerWrapper);
		}
	}

	public void createAnnotationIntroduceInfoClass(String className, AnnotationHolder annotationHolder, ContainerWrapper containerWrapper) {
		try {
			Class targetclass = Utils.createClass(className);
			Introduce cp = (Introduce) targetclass.getAnnotation(Introduce.class);

			String[] adviceName = cp.value();
			introduceInfoHolder.addIntroduceInfo(adviceName, targetclass);
			String targetName = annotationHolder.getComponentName(targetclass);
			if (targetName == null) {// iterate xml component
				TargetMetaDefHolder targetMetaDefHolder = (TargetMetaDefHolder) containerWrapper.lookup(ComponentKeys.SERVICE_METAHOLDER_NAME);
				targetName = targetMetaDefHolder.lookupForName(targetclass.getName());
			}
			introduceInfoHolder.addTargetClassNames(targetclass, targetName);
			Debug.logVerbose("[JdonFramework] load Annotation IntroduceInfo name:" + adviceName + " target class:" + className, module);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationIntroduceInfoClass error:" + e + className, module);
		}
	}
}
