package gri.engine.reflect;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodReflection {

	public MethodReflection() {
		// TODO Auto-generated constructor stub
	}
	
	public Object load(String className,String methodName,Object... objs) {
		Object result=null;
		try {
			Class<?> clazz=Class.forName(className);
			Object obj=clazz.newInstance();
			Class<?>[] paramTypes=this.getParamTypes(clazz, methodName);
			Method method=clazz.getDeclaredMethod(methodName, paramTypes);
			method.setAccessible(true);
			result=method.invoke(obj, objs);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	 public Class<?>[] getParamTypes(Class<?> clazz, String methodName) 
	 {//获取参数类型，返回值保存在Class[]中
	  Class<?>[] cs = null;
	  Method[] mtd = clazz.getDeclaredMethods();// 通过反射机制调用非public方法,使用了getDeclaredMethods()方法    
	  for (int i = 0; i < mtd.length; i++)
	  {
	   if (!mtd[i].getName().equals(methodName))
	       continue; // 不是所需要的参数，则进入下一次循环
	   cs = mtd[i].getParameterTypes();
	   break;
	  }
	  return cs;
	 }
	 
	 public static void main(String[] args) {
		// new MethodReflection().load("test2.test01","t3",1,true,new test02());
	 }
}
