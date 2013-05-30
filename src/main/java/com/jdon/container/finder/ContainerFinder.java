package com.jdon.container.finder;

import com.jdon.container.ContainerWrapper;
import com.jdon.controller.context.AppContextWrapper;

/**
 * difference with ContainerCallback:
 * ContainerCallback client is in the container.
 * this client is outside the container
 * @author banq
 *
 */
public interface ContainerFinder {

	public abstract ContainerWrapper findContainer(AppContextWrapper sc);

}