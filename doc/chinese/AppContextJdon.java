
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.RegistryDirectory;
import com.jdon.container.finder.ContainerFinder;
import com.jdon.container.finder.ContainerFinderImp;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.web.ServletContextWrapper;
import com.jdon.util.ClassUtil;

/**
 * <bean id="appContextJdon" class="com.jdon.spring.AppContextJdon"></bean>
 * 
 * @author banq
 * 
 */
public class AppContextJdon implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor, ApplicationListener {

	private ServletContext servletContext;
	private ApplicationContext applicationContext;
	private ConfigurableListableBeanFactory factory;
	private Map<String, Object> neededJdonComponents = new HashMap();
	private ContainerWrapper containerWrapper;

	/**
	 * ApplicationContextAware's method
	 * 
	 * at first run, startup Jdon Framework *
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

		if (servletContext == null)
			if (applicationContext instanceof WebApplicationContext) {
				servletContext = ((WebApplicationContext) applicationContext).getServletContext();
				if (servletContext == null) {
					System.err.print("this class only fit for Spring Web Application");
					return;
				}

			}

		// start up jdon
		AppContextWrapper acw = new ServletContextWrapper(servletContext);
		ContainerFinder containerFinder = new ContainerFinderImp();
		containerWrapper = containerFinder.findContainer(acw);

	}

	/**
	 * BeanDefinitionRegistryPostProcessor's method
	 * 
	 * second run: check which spring bean that need injected from Jdon.
	 * 
	 */
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

		for (String beanName : registry.getBeanDefinitionNames()) {
			BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
			String beanClassName = beanDefinition.getBeanClassName();
			try {
				Class beanClass = Class.forName(beanClassName);
				// large project need using Google Collection's lookup
				for (final Field field : ClassUtil.getAllDecaredFields(beanClass)) {
					if (field.isAnnotationPresent(Autowired.class)) {
						// inject jdon components into spring components with
						// AutoWire;
						Object o = findBeanClassInJdon(field.getType());
						if (o != null) {
							neededJdonComponents.put(field.getName(), o);
						}
					}
				}
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * BeanDefinitionRegistryPostProcessor's method
	 * 
	 * third run: injecting the jdon component into Spring bean;
	 * 
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
		this.factory = factory;
		injectJdonToSpring();
	}

	/**
	 * ApplicationListener's method
	 * 
	 * fouth run: after Spring all bean created and ApplicatioConext is ready .
	 * 
	 * injecting Spring bean instances into jdon component.
	 */
	public void onApplicationEvent(ApplicationEvent ae) {
		if (ae instanceof ContextRefreshedEvent)
			injectSpringToJdon();
	}

	public void injectJdonToSpring() {
		for (String name : neededJdonComponents.keySet()) {
			Object o = neededJdonComponents.get(name);
			if (!factory.containsBean(name))
				factory.registerSingleton(name, o);
		}
	}

	private Object findBeanClassInJdon(Class fClass) {
		List<Object> objects = containerWrapper.getComponentInstancesOfType(fClass);
		Object o = null;
		// List should be have only one.
		for (Object instance : objects) {
			o = instance;
			break;
		}
		return o;
	}

	public void injectSpringToJdon() {
		AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
		RegistryDirectory registryDirectory = (RegistryDirectory) containerWrapper.getRegistryDirectory();
		List<String> names = new ArrayList(registryDirectory.getComponentNames());
		for (String name : names) {
			Object o = containerWrapper.lookupOriginal(name);
			beanFactory.autowireBean(o);
			containerWrapper.register(name, o);

		}

	}

}
