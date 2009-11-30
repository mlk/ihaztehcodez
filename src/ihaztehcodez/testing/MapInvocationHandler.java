package ihaztehcodez.testing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.Map;

/** An Invocation handler that uses methods from a map to back it up.
 * 
 * @author Michael Lloyd Lee
 *
 */
public final class MapInvocationHandler implements InvocationHandler {
	/** The backing Map. */
	private final Map<String, Object> backing; 
	
	/** Ctor.
	 * 
	 * @param backing The map that backs the invocation handler.
	 */
	public MapInvocationHandler(final Map<String, Object> backing) {
		this.backing = backing;
	}
	
	/** The return value to use for this method call.
	 * 
	 * @param methodName The method call
	 * @param rtn The return value
	 */
	public void put(final String methodName, final Object rtn) {
		backing.put(methodName, rtn);
	}
	
	/** Raw map access.
	 * 
	 * @return Returns raw access to the map.
	 */
	public Map<String, Object> getMap() {
		return backing;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args)
			throws Throwable {
		if (method.getName().equals("toString") && !backing.containsKey("toString")) {
			return proxy.getClass().getName() + "@" + System.identityHashCode(proxy) 
			 	+ " PROXY backing: " + backing.toString();
		}
		if (method.getName().equals("equals")) {
			if (Proxy.isProxyClass(args[0].getClass())) {
				if (backing.containsKey("equals")) {
					return ((Comparator<Object>) backing.get("equals"))
						.compare(proxy, args[0]) == 0;
				} else {
					return (proxy == args[0]);
				}
			}
			return false;
		}
		validateArguments(args);
		
		if (backing.containsKey(method.getName())) { 
			return backing.get(method.getName());
		}
		String methodName = getMethodName(method);
		
		if (backing.containsKey(methodName)) {
			return backing.get(methodName);
		}

		throw new IllegalAccessException(methodName + " does not exist");
	}

	/** Returns the method name with the get or is stripped.
	 * 
	 * @param method The method to strip.
	 * @return The stripped method name.
	 */
	private String getMethodName(final Method method) {
		String methodName = method.getName();
		if (methodName.startsWith("get")) {
			methodName = methodName.substring(3);
			methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
		} else if (methodName.startsWith("is")) {
			methodName = methodName.substring(2);
			methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
		}
		return methodName;
	}

	/** This proxy only works on zero-arg methods (that is - gets).
	 * 
	 * @param args The arguments to validate.
	 */
	private void validateArguments(final Object[] args) {
		if (args != null) {
			if (args.length != 0) {
				throw new IllegalArgumentException("Can only be called on zero-arg methods");
			}
		}
	}

	/** Returns the map that backs this proxy.
	 * 
	 * @param o The proxy
	 * @return the map that backs this proxy.
	 */
	public static Map<String, Object> getMap(final Object o) {
		return ((MapInvocationHandler) Proxy
				.getInvocationHandler(o)).getMap();
	}
	
	/** Creates a proxy for a given type.
	 * 
	 * @param <T> The type to create a proxy for.
	 * @param data The data used to back this type.
	 * @param clazz The type (again, I know not DRY).
	 * @return A proxied object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T create(final Map<String, Object> data, final Class<T> clazz) {
		return (T) Proxy.newProxyInstance(MapInvocationHandler.class.getClassLoader(), 
				new Class[] { clazz }, new MapInvocationHandler(data));
	}
}
