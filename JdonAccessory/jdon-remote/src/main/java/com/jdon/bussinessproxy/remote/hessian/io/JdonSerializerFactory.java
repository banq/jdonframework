package com.jdon.bussinessproxy.remote.hessian.io;

import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;


public class JdonSerializerFactory extends SerializerFactory {

	  /**
	   * Returns the default serializer for event class that isn't matched
	   * directly.  Application can override this method to produce
	   * bean-style serialization instead of field serialization.
	   *
	   * @param cl the class of the object that needs to be serialized.
	   *
	   * @return event serializer object for the serialization.
	   */
  protected Serializer getDefaultSerializer(Class cl) {
      return new JdonJavaSerializer(cl);
  }
}
