package com.xiaozhi.blog.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortList<T> {

	/**
	 * 通用排序方法
	 *
	 * @param list
	 * @param method
	 *            获取属性的方法
	 * @param sort
	 *            排序方式
	 * @param type
	 *            属性类型
	 */
	public void sort(List<T> list, final String method, final String type,final String sort) {
		Collections.sort(list, new Comparator() {
			public int compare(Object a, Object b) {
				int ret = 0;
				try {
					Method m1 = ((T) a).getClass().getMethod(method, null);
					Method m2 = ((T) b).getClass().getMethod(method, null);
					if (sort != null && "desc".equals(sort)) {// 倒序
						if ("Integer".equals(type) || "int".equals(type)) {
							ret = ((Integer)m2.invoke(((T) b), null)).compareTo((Integer)m1.invoke(((T) a), null));
						} else if("Float".equals(type) || "float".equals(type)){
							ret = ((Float)m2.invoke(((T) b), null)).compareTo((Float)m1.invoke(((T) a), null));
						} else if("Double".equals(type) || "double".equals(type)){
							ret = ((Double)m2.invoke(((T) b), null)).compareTo((Double)m1.invoke(((T) a), null));
						} else if("Long".equals(type) || "long".equals(type)){
							ret = ((Long)m2.invoke(((T) b), null)).compareTo((Long)m1.invoke(((T) a), null));
						}else {
							ret = m2.invoke(((T) b), null).toString().compareTo(m1.invoke(((T) a), null).toString());
						}

					} else {// 正序
						if ("Integer".equals(type) || "int".equals(type)) {
							ret = ((Integer)m1.invoke(((T) b), null)).compareTo((Integer)m2.invoke(((T) a), null));
						} else if("Float".equals(type) || "float".equals(type)){
							ret = ((Float)m1.invoke(((T) b), null)).compareTo((Float)m2.invoke(((T) a), null));
						} else if("Double".equals(type) || "double".equals(type)){
							ret = ((Double)m1.invoke(((T) b), null)).compareTo((Double)m2.invoke(((T) a), null));
						} else if("Long".equals(type) || "long".equals(type)){
							ret = ((Long)m1.invoke(((T) b), null)).compareTo((Long)m2.invoke(((T) a), null));
						}else {
							ret = m1.invoke(((T) b), null).toString().compareTo(m2.invoke(((T) a), null).toString());
						}
					}
				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalAccessException ie) {
					System.out.println(ie);
				} catch (InvocationTargetException it) {
					System.out.println(it);
				}
				return ret;
			}
		});
	}
}
