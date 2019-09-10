/*
 * Copyright 2007 the original author or jdon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.jdon.persistence.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class AnnotationConfFactory extends ConfFactory {
	private final static Logger logger = LogManager.getLogger(AnnotationConfFactory.class);

	public AnnotationConfFactory(String hibernate_cfg_xml) {
		super(hibernate_cfg_xml);
	}

	public void createSessionFactory() {
		try {
			Configuration configuration = null;
			if ((hibernate_cfg_xml != null) && (hibernate_cfg_xml.length() != 0)) {
				configuration = new AnnotationConfiguration().configure(hibernate_cfg_xml);
			} else {
				configuration = new AnnotationConfiguration().configure();
			}
			this.sessionFactory = configuration.buildSessionFactory();
		} catch (HibernateException e) {
			logger.error("Hibernate Annotation start error: " + e);
		}

	}

}
