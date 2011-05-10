package com.jdon.controller.context;

import java.security.Principal;

public interface RequestWrapper {
	
	ContextHolder getContextHolder();
		
	String getRemoteAddr();
	
	Principal getRegisteredPrincipal();

}
