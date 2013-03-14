package mina.remote;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 
 * @author xiaozhi
 * 
 */

public class Reflection {

	/**
	 * 取得参数对象中的公共属性
	 * 
	 * @param obj
	 * @param fieldname
	 * @return
	 * @throws Exception
	 */
	public static Object getProperty(Object obj, String fieldname) throws Exception {
		Object result = null;
		Class objClass = obj.getClass();
		Field field = objClass.getField(fieldname);
		result = field.get(obj);
		return result;
	}

	/**
	 * 获得某类的静态属性
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static Object getStaticProperty(String className, String fieldName)
			throws Exception {
		Class cls = Class.forName(className);
		Field field = cls.getField(fieldName);
		Object provalue = field.get(cls);
		return provalue;
	}

	/**
	 * 获取参数对象的属性值
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	public static Object getPrivatePropertyValue(Object obj, String propertyName)
			throws Exception {
		Class cls = obj.getClass();
		Field field = cls.getDeclaredField(propertyName);
		field.setAccessible(true);
		Object retvalue = field.get(obj);
		return retvalue;
	}

	/**
	 * 执行某对象的方法
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Object invokeMethod(Object owner, String methodName, Object[] args)
			throws Exception {
		Class cls = owner.getClass();
		Class[] argclass = new Class[args.length];
		for (int i = 0, j = argclass.length; i < j; i++) {
			argclass[i] = args[i].getClass();
		}
		Method method = cls.getMethod(methodName, argclass);
		return method.invoke(owner, args);
	}

	/**
	 * 执行静态类的方法
	 * 
	 * @param className
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Object invokeStaticMethod(String className, String methodName,Object[] args) throws Exception {
		Class cls = Class.forName(className);
		Class[] argclass = new Class[args.length];
		for (int i = 0, j = argclass.length; i < j; i++) {
			argclass[i] = args[i].getClass();
		}
		Method method = cls.getMethod(methodName, argclass);
		return method.invoke(null, args);
	}

	
	public static Object newInstance(String className, Object[] args) throws Exception {
		Class clss = Class.forName(className);

		Class[] argclass = new Class[args.length];
		for (int i = 0, j = argclass.length; i < j; i++) {
			argclass[i] = args[i].getClass();
		}
		java.lang.reflect.Constructor cons = clss.getConstructor(argclass);
		return cons.newInstance();
	}

}