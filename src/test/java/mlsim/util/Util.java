package mlsim.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Util {
	public static <T> T invokePrivateMethod(Object object, String name, List<Class<?>> argTypes, Object... args) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = object.getClass().getDeclaredMethod(name, (Class[])argTypes.toArray());
		method.setAccessible(true);
		return (T) method.invoke(object, args);
	}
}
