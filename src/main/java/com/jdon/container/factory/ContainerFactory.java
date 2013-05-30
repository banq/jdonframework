package com.jdon.container.factory;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.pico.ConfigInfo;
import com.jdon.container.pico.PicoContainerWrapper;

public class ContainerFactory {

	public synchronized ContainerWrapper create(ConfigInfo configInfo) {
		PicoContainerWrapper pico = new PicoContainerWrapper(configInfo);
		configInfo.setContainerWrapper(pico);
		pico.registerContainerCallback();
		return pico;
	}
}
