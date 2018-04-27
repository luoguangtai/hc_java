package com.hc.model;

import java.lang.reflect.Method;

/**
 * 基础的MODEL类
 * 实现model类的对象属性值获取
 */
public class BaseModel {

	/**
	 * 返回对象的属性值
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(this.getClass().getName()).append("(");
		Method[] m = this.getClass().getMethods();		
		try {
			for (int i = 0; i < m.length; i++) {
				String methodName = m[i].getName();
				if (methodName.startsWith("get") && !methodName.endsWith("Class")) {
					Object o = m[i].invoke(this, new Object[] {});
					if (o != null) {
						buff.append(methodName.substring(3)).append("=[").append(o).append("] ");
					}
					else{
						buff.append(methodName.substring(3)).append("=[").append("null").append("] ");
					}
				}
			}
			buff.append(")");
		}
		catch (Exception iae) {
			;
		}
		return buff.toString();
	}

}
