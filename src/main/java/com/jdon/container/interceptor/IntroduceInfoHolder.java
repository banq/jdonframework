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
package com.jdon.container.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IntroduceInfoHolder {
	public final static String NAME = IntroduceInfoHolder.class.getSimpleName();

	// key is acceptor/target class
	private Map<Class, IntroduceInfo> introduceInfos;

	// key is advice Name
	private Map<String, List<IntroduceInfo>> adviceNameIntroduceInfos;

	// key is acceptor/target Class, value is target name
	private Map<Class, String> introducerClassNames;

	public IntroduceInfoHolder() {
		this.introduceInfos = new LinkedHashMap();
		this.introducerClassNames = new LinkedHashMap();
		this.adviceNameIntroduceInfos = new HashMap();
	}

	public Map<Class, String> getAcceptorClassNames() {
		return introducerClassNames;
	}

	public Map<Class, IntroduceInfo> getIntroduceInfos() {
		return introduceInfos;
	}

	public IntroduceInfo getIntroduceInfoByIntroducer(Class introducerClass) {
		return introduceInfos.get(introducerClass);
	}

	public void addIntroduceInfo(String[] adviceNames, Class introducerClass) {
		if (adviceNames == null)
			return;
		for (int i = 0; i < adviceNames.length; i++) {
			IntroduceInfo introduceInfo = new IntroduceInfo(adviceNames[i], introducerClass);
			introduceInfos.put(introducerClass, introduceInfo);
			addAdviceNameIntroduceInfos(adviceNames[i], introduceInfo);
		}
	}

	private void addAdviceNameIntroduceInfos(String adviceName, IntroduceInfo introduceInfo) {
		List<IntroduceInfo> infos = adviceNameIntroduceInfos.get(adviceName);
		if (infos == null) {
			infos = new ArrayList();
			adviceNameIntroduceInfos.put(adviceName, infos);
		}
		infos.add(introduceInfo);

	}

	public boolean containsThisClass(Class introducerClass) {
		return introduceInfos.containsKey(introducerClass);
	}

	public List<String> getIntroducerNameByIntroducedName(String introducedName) {
		List<String> names = new ArrayList();

		List<IntroduceInfo> introduceInfos = adviceNameIntroduceInfos.get(introducedName);
		if (introduceInfos == null)
			return names;
		for (IntroduceInfo info : introduceInfos) {
			names.add(getTargetName(info.getTarget()));
		}
		return names;
	}

	public Set<String> getIntroduceNames() {
		return adviceNameIntroduceInfos.keySet();
	}

	public void addTargetClassNames(Class targetClass, String targetName) {
		introducerClassNames.put(targetClass, targetName);
	}

	public String getTargetName(Class targetClass) {
		return introducerClassNames.get(targetClass);
	}

}
