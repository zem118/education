package com.education.common.utils;

import com.education.common.model.PageInfo;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 字符串处理工具类
 *   
 *   
 */
public class ObjectUtils {

	/**
	 * 判断数组是否为空
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(Object[] target) {
		return target == null || target.length == 0 ;
	}

	public static boolean isNotEmpty(Object[] target) {
		return target != null && target.length > 0;
	}

	/**
	 * 判断对象是否为空
	 *
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(Object target) {
		if (target instanceof Collection) {
			Collection collection = (Collection)target;
			return collection == null || collection.size() == 0;
		} else if (target instanceof Map) {
			Map map = (Map)target;
			return map == null || map.size() == 0;
		}
		return target == null || target.toString().trim().length() == 0;
	}

	public static boolean isNotEmpty(Object target) {
		if (target instanceof Collection) {
			Collection collection = (Collection) target;
			return collection != null && collection.size() > 0;
		} else if (target instanceof Map) {
			Map map = (Map) target;
			return map != null || map.size() > 0;
		}
		return target != null && target.toString().trim().length() > 0;
	}



	/**
	 * 将字符串转换成数组
	 * @param str
	 * @return
	 */
	public static String[] spilt(String str) {
		if (str == null)
			throw new NullPointerException("str can not be null");
		return str.split(",");
	}

	public static List<String> spiltToList(String str) {
		if (str == null)
			throw new NullPointerException("str can not be null");
		String[] array = str.split(",");
		return Arrays.asList(array);
	}

	public static String[] spilt(String str, String separator) {
		if (str == null)
			throw new NullPointerException("str can not be null");
		return str.split(separator);
	}

	/**
	 * 将首字母变成小写
	 * @param keyName
	 * @return
	 */
	public static String toLowerCaseFirst(String keyName){
		return keyName.substring(0, 1).toLowerCase();
	}

	/**
	 * 将首字母变成大写
	 * @param keyName
	 * @return
	 */
	public static String totoUpperCaseFirst(String keyName) {
		return keyName.substring(0, 1).toUpperCase() + keyName.substring(1, keyName.length());
	}

	/**
	 * 生成年月日字符串
	 * @return
	 */
	public static String generateFileByTime() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date()).replaceAll("-", "/");
	}

	/**
	 * 生成时分秒字符串
	 * @return
	 */
	public static String generateFileBySecond() {
		DateFormat format = new SimpleDateFormat("hh-mm-ss");
		return format.format(new Date()).replaceAll("-", "");
	}

	public static String generateUuId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 通过反射获取对象属性值
	 * @param target
	 * @param fieldName
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public static <T> T getValue(Object target, String fieldName) throws Exception {
		List<Field> fieldList = new ArrayList();
		addField(fieldList, target.getClass());
		for (Field field : fieldList) {
			if (field.getName().equals(fieldName)) {
				field.setAccessible(true);
				return (T) field.get(target);
			}
		}
		return null;
	}

	private static void addField(List<Field> fieldList, Class<?> clazz) {
		Field[] field = clazz.getDeclaredFields();
		fieldList.addAll(Arrays.asList(field));
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			addField(fieldList, superClass);
		}
	}

	/**
	 * list 集合内存分页
	 * @param pageNumber
	 * @param pageSize
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> PageInfo<T> selectPageList(int pageNumber, int pageSize, List<T> data) {
		int offset = (pageNumber - 1) * pageSize;
		int totalRow = data.size();
		int totalPage = totalRow / pageSize;
		boolean flag = true; // 分页是否刚好整除
		if (totalRow % pageSize != 0) {
			totalPage++;
			flag = false;
		}

		if (pageNumber > totalPage) {
			return new PageInfo<>(new ArrayList<>(), totalPage, totalRow);
		}

		int limit = offset + pageSize;
	    if (limit >= totalRow) {
			if (offset < totalRow && !flag) {
				 // 取最后一页所有元素
				int index = (totalPage - 1) * pageSize; // 最后一页之前的下标
				return new PageInfo<>(data.subList(index, totalRow), totalPage, totalRow);
			}
			return new PageInfo<>(new ArrayList<>(), totalPage, totalRow);
		}
		return new PageInfo(data.subList(offset, limit), totalPage, totalRow);
	}

	public static String charSort(String value) {
		char[] c = value.toCharArray(); //将字符串转换成char数组
		Arrays.sort(c);//对数组进行排序
		return new String(c); //返回数组。注
	}
}
