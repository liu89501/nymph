package com.nymph.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.nymph.utils.BasicUtils;

public class XmlComponent {
	
	@SuppressWarnings("unchecked")
	public static List<Object> readXml(String location) {
		try {
			SAXReader sax = new SAXReader();
			Document document = sax.read(
				BasicUtils.getDefaultClassLoad().getResourceAsStream(location));
			Element element = document.getRootElement();
			return component(element.elements("component"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 解析所有的component元素
	 * @param elements		component的元素集合
	 * @return				所有解析完成的对象集合
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> component(List<Element> elements) throws Exception {
		if (elements == null || elements.size() == 0)
			return null;
		
		Map<String, MTask> mTask = new HashMap<>();
		Map<String, CTask> cTask = new HashMap<>();
		Map<String, STask> sTask = new HashMap<>();

		Map<String, Object> component = new HashMap<>();

		for (Element element : elements) {
			String classValue = element.attribute("class").getValue();
			String idValue = element.attribute("id").getValue();

			Class<?> forName = Class.forName(classValue);
			Element conEle = element.element("constructor");
			
			boolean isref = false;

			if (conEle != null) {
				List<Element> params = conEle.elements("param");

				for (Constructor<?> construct : forName.getConstructors()) {
					Class<?>[] types = construct.getParameterTypes();
					if (types.length == params.size()) {
						Object[] args = new Object[types.length];

						for (int i = 0; i < args.length; i++) {
							Attribute attribute = params.get(i).attribute("value");

							if (attribute != null) {
								String value = attribute.getValue();
								args[i] = convert(types[i], value);
							} else {
								isref = true;
								args[i] = null;
								String ref = params.get(i).attribute("ref").getValue();
								cTask.put(idValue, new CTask(args, construct, ref));
							}
						}
						if (!isref) {
							component.put(idValue, construct.newInstance(args));
						}
						continue;
					}
				}

			} else {
				List<Element> methods = element.elements("method");
				List<Element> setters = element.elements("setter");
				Object instance = forName.newInstance();
				
				if (methods.size() <= 0 && setters.size() <= 0) {
					component.put(idValue, instance);
					continue;
				}
				
				for (Element setter : setters) {
					String name = setter.attribute("name").getValue();

					for (Method method : forName.getMethods()) {
						if (name.equals(method.getName())) {
							
							Class<?> type = method.getParameterTypes()[0];

							Attribute attribute = setter.attribute("value");
							
							if (attribute != null) {
								String value = attribute.getValue();
								Object result = convert(type, value);
								method.invoke(instance, result);
							} else {
								isref = true;
								String ref = setter.attribute("ref").getValue();
								sTask.put(idValue, new STask(instance, method, ref));
							}
							
							if (!isref) {
								component.put(idValue, instance);
							}
						}
					}
				}
				
				
				for (Element methodEle : methods) {
					String name = methodEle.attribute("name").getValue();

					for (Method method : forName.getMethods()) {
						if (name.equals(method.getName())) {
							Class<?>[] types = method.getParameterTypes();
							Object[] args = new Object[types.length];
							
							List<Element> values = methodEle.elements("property");
							
							Elements[] property = values.stream().map(ele -> {
								String value = ele.attribute("value") == null ? 
									null : ele.attribute("value").getValue();
								String ref = ele.attribute("ref") == null ? 
										null : ele.attribute("ref").getValue();
								return new Elements(value, ref);
							}).toArray(a -> new Elements[values.size()]);
							
							for (int i = 0; i < args.length; i++) {
								if (property[i].value != null) {
									args[i] = convert(types[i], property[i].value);
								} else {
									isref = true;
									args[i] = null;
									mTask.put(idValue, 
										new MTask(instance, method, property[i].ref, args));
								}
							}
							
							if (!isref) {
								method.invoke(instance, args);
								component.put(idValue, instance);
							}
						}
					}
				}
			}
		}
		return handlerTask(mTask, cTask, sTask, component);
	}
	
	/**
	 * 处理有引用的配置, 对应xml中component里setter或constructor的ref属性
	 * @param mTask			setter的ref任务
	 * @param cTask			constructor的ref任务
	 * @param component		所有的component
	 * @return
	 * @throws Exception
	 */
	public static List<Object> handlerTask(Map<String, MTask> mTask, Map<String, CTask> cTask, 
			Map<String, STask> sTask, Map<String, Object> component) throws Exception {
		
		for (Entry<String, MTask> task : mTask.entrySet()) {
			MTask value = task.getValue();
			String key = task.getKey();
			Object object = component.get(value.ref);
			
			for (int i = 0; i < value.args.length; i++) {
				if (value.args[i] == null) {
					value.args[i] = object;
				}
			}
			
			value.method.invoke(value.target, value.args);
			component.put(key, value.target);
		}
		
		for (Entry<String, STask> task : sTask.entrySet()) {
			STask value = task.getValue();
			String key = task.getKey();
			Object object = component.get(value.ref);
			
			value.method.invoke(value.target, object);
			component.put(key, value.target);
		}
		
		for (Entry<String, CTask> task : cTask.entrySet()) {
			CTask value = task.getValue();
			String key = task.getKey();
			Object object = component.get(value.ref);
			
			for (int i = 0; i < value.args.length; i++) {
				if (value.args[i] == null) {
					value.args[i] = object;
				}
			}
			component.put(key, value.constructor.newInstance(value.args));
		}
		return component.values().stream().collect(Collectors.toList());
	}
	
	/**
	 * 将字符串转换成指定类型
	 * @param clazz
	 * @param value
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static Object convert(Class<?> clazz, String value) throws ClassNotFoundException {
		if (clazz == Class.class)
			return Class.forName(value);
		return BasicUtils.convert(clazz, value);
	}
	
	private static class MTask {
		Object target;
		Method method;
		String ref;
		Object[] args;
		
		MTask(Object target, Method method, String ref, Object[] args) {
			this.target = target;
			this.method = method;
			this.ref = ref;
			this.args = args;
		}
	}
	
	private static class STask {
		Object target;
		Method method;
		String ref;
		
		STask(Object target, Method method, String ref) {
			this.target = target;
			this.method = method;
			this.ref = ref;
		}
	}
	
	private static class CTask {
		Constructor<?> constructor;
		Object[] args;
		String ref;
		
		CTask(Object[] args, Constructor<?> constructor, String ref) {
			this.args = args;
			this.constructor = constructor;
			this.ref = ref;
		}
	}
	
	private static class Elements {
		String value;
		String ref;
		
		Elements(String value, String ref) {
			super();
			this.value = value;
			this.ref = ref;
		}
		
	}

}
