package com.jdon.bussinessproxy.remote.hessian.io;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.logging.Level;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;

public class JdonJavaSerializer extends AbstractSerializer {
    
    private static final int INT_VALUE = 3;
    private final Method []localMethods;
    private final String []localNames;
    private final Method writeReplace;
	
    /**
     * Constructor
     * @param cl - Class cl
     */
    public JdonJavaSerializer(Class< ? > cl) {
        writeReplace = getWriteReplace(cl);

        ArrayList<Method> primitiveMethods = new ArrayList<Method>();
        ArrayList<Method> compoundMethods = new ArrayList<Method>();
	    
        for (; cl != null; cl = cl.getSuperclass()) {
            Method []methods = cl.getDeclaredMethods();
	      
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];

                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                if (method.getParameterTypes().length != 0) {
                    continue;
                }

                String name = method.getName();

                if (!name.startsWith("get")) {
                    continue;
                }

                Class< ? > type = method.getReturnType();

                if (type.equals(void.class)) {
                    continue;
                }

                if (findSetter(methods, name, type) == null) {
                    continue;
                }

                // XXX: could parameterize the handler to only deal with public
                method.setAccessible(true);

                if (type.isPrimitive() 
                    || type.getName().startsWith("java.lang.") 
                    && !type.equals(Object.class)) {
                    primitiveMethods.add(method);
                } else {
                    compoundMethods.add(method);      	
                }
            }
        }

        ArrayList<Method> methodList = new ArrayList<Method>();
        methodList.addAll(primitiveMethods);
        methodList.addAll(compoundMethods);

        localMethods = new Method[methodList.size()];
        methodList.toArray(localMethods);

        localNames = new String[localMethods.length];
	    
        for (int i = 0; i < localMethods.length; i++) {
            String name = localMethods[i].getName();

            name = name.substring(INT_VALUE);

            int j = 0;
            for (; j < name.length() && Character.isUpperCase(name.charAt(j)); j++) {
            }
            if (j == 1) {
                name = name.substring(0, j).toLowerCase() + name.substring(j);
            } else if (j > 1) {
            	name = name.substring(0, j - 1).toLowerCase() + name.substring(j - 1);  	
            }

            localNames[i] = name;
        }
    }

	/**
	 * Returns the writeReplace method
	 */
    private Method getWriteReplace(Class< ? > cl) {
        for (; cl != null; cl = cl.getSuperclass()) {
            Method []methods = cl.getDeclaredMethods();
	      
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];

                if (method.getName().equals("writeReplace") 
                    && method.getParameterTypes().length == 0) {
                    return method;
                }
            }
        }
        return null;
    }
    
    /**
     * Write object in output
     * @param obj - Object
     * @param out - AbstractHessianOutput
     * @throws IOException - occur if input/output error occur
     */
    public void writeObject(Object obj, AbstractHessianOutput out) 
        throws IOException {
    	
        if (out.addRef(obj)) {
            return;
        }
	    
        Class< ? > cl = obj.getClass();
	    
        try {
            if (writeReplace != null) {
                Object repl = writeReplace.invoke(obj, new Object[0]);

                out.removeRef(obj);

                out.writeObject(repl);

                out.replaceRef(repl, obj);

                return;
            }
        } catch (Exception e) {
            log.log(Level.FINE, e.toString(), e);
        }

        int ref = out.writeObjectBegin(cl.getName());

        if (ref < -1) {
	    // Hessian 1.1 uses event map
	      
            for (int i = 0; i < localMethods.length; i++) {
                Object value = null;

                try {
                    value = localMethods[i].invoke(obj, (Object []) null);
                } catch (Exception e) {
                    log.log(Level.FINE, e.toString(), e);
                }
		
                if (!LazyUtil.isPropertyInitialized(value)) {
                    continue;
                }
		
                out.writeString(localNames[i]);
		
                out.writeObject(value);
            }
	      
            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeInt(localNames.length);
		
                for (int i = 0; i < localNames.length; i++) {
                    out.writeString(localNames[i]);
                }
                out.writeObjectBegin(cl.getName());
            }

            for (int i = 0; i < localMethods.length; i++) {
                Object value = null;

                try {
                    value = localMethods[i].invoke(obj, (Object []) null);
                } catch (Exception e) {
                    log.log(Level.FINER, e.toString(), e);
                }
		
                if (!LazyUtil.isPropertyInitialized(value)) {
                    continue;
                }
		
                out.writeObject(value);
            }
        }
    }

	/**
	 * Finds any matching setter.
	 */
    private Method findSetter(Method []methods, String getterName, Class< ? > arg) {
        String setterName = "set" + getterName.substring(INT_VALUE);
	    
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            if (!method.getName().equals(setterName)) {
                continue;
            }
            
            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            Class< ? > []params = method.getParameterTypes();

            if (params.length == 1 && params[0].equals(arg)) {
                return method;
            }
        }
        return null;
    }
}
