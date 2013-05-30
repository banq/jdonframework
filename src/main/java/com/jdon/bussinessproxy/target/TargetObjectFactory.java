package com.jdon.bussinessproxy.target;

import com.jdon.container.finder.ContainerCallback;


public interface TargetObjectFactory {

	Object create(ContainerCallback containerCallback) throws Exception;

}