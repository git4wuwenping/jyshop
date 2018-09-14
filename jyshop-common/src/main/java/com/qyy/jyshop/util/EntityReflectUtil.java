package com.qyy.jyshop.util;

import java.beans.BeanInfo;  
import java.beans.IntrospectionException;  
import java.beans.Introspector;  
import java.beans.PropertyDescriptor;  
import java.lang.reflect.InvocationTargetException;  
import java.lang.reflect.Method;  
import java.math.BigDecimal;
import java.util.HashMap;  
import java.util.Map;  

import org.apache.commons.beanutils.ConvertUtils;

public class EntityReflectUtil {

    

    /** 
     * 将一个 Map 对象转化为一个 JavaBean 
     * @param clazz 要转化的类型 
     * @param map 包含属性值的 map 
     * @return 转化出来的 JavaBean 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InstantiationException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */  
    @SuppressWarnings("rawtypes")  
    public static <T> T toBean(Class<T> clazz, Map map) {  
        T obj = null;  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);  
            obj = clazz.newInstance(); // 创建 JavaBean 对象  
  
            // 给 JavaBean 对象的属性赋值  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (int i = 0; i < propertyDescriptors.length; i++) {  
                PropertyDescriptor descriptor = propertyDescriptors[i];  
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {  
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。  
                    Object value = map.get(propertyName);
                    Class<?> fieldTypeClass = descriptor.getPropertyType();
                    if(value != null && !"".equals(value)){
                        //value = convertValType(value, fieldTypeClass);
                        value = ConvertUtils.convert(value, fieldTypeClass);
                        Object[] args = new Object[1];
                        args[0] = value;
                        try {
                            descriptor.getWriteMethod().invoke(obj, args);
                        } catch (InvocationTargetException e) {
                            System.out.println("字段映射失败");
                        }
                    }
                }
            }  
        } catch (IllegalAccessException e) {  
            System.out.println("实例化 JavaBean 失败");  
        } catch (IntrospectionException e) {  
            System.out.println("分析类属性失败");  
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("映射错误");  
        } catch (InstantiationException e) {  
            System.out.println("实例化 JavaBean 失败");  
        }  
        return (T) obj;  
    }
    
     
  
    /** 
     * 将一个 JavaBean 对象转化为一个 Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的 Map 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */  
    @SuppressWarnings("rawtypes")  
    public static Map toMap(Object bean) {  
        Class<? extends Object> clazz = bean.getClass();  
        Map<Object, Object> returnMap = new HashMap<Object, Object>();  
        BeanInfo beanInfo = null;  
        try {  
            beanInfo = Introspector.getBeanInfo(clazz);  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (int i = 0; i < propertyDescriptors.length; i++) {  
                PropertyDescriptor descriptor = propertyDescriptors[i];  
                String propertyName = descriptor.getName();  
                if (!propertyName.equals("class")) {  
                    Method readMethod = descriptor.getReadMethod();  
                    Object result = null;  
                    result = readMethod.invoke(bean, new Object[0]);  
                    if (null != propertyName) {  
                        propertyName = propertyName.toString();  
                    }  
                    if (null != result) {  
                        result = result.toString();  
                    }  
                    returnMap.put(propertyName, result);  
                }  
            }  
        } catch (IntrospectionException e) {  
            System.out.println("分析类属性失败");  
        } catch (IllegalAccessException e) {  
            System.out.println("实例化 JavaBean 失败");  
        } catch (IllegalArgumentException e) {  
            System.out.println("映射错误");  
        } catch (InvocationTargetException e) {  
            System.out.println("调用属性的 setter 方法失败");  
        }  
        return returnMap;  
    }
    
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {    
        Object retVal = null;    
        if(Long.class.getName().equals(fieldTypeClass.getName())    
                || long.class.getName().equals(fieldTypeClass.getName())) {    
            retVal = Long.parseLong(value.toString());    
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())    
                || int.class.getName().equals(fieldTypeClass.getName())) {    
            retVal = Integer.parseInt(value.toString());    
        } else if(Float.class.getName().equals(fieldTypeClass.getName())    
                || float.class.getName().equals(fieldTypeClass.getName())) {    
            retVal = Float.parseFloat(value.toString());    
        } else if(Double.class.getName().equals(fieldTypeClass.getName())    
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());    
        } else if(BigDecimal.class.getName().equals(fieldTypeClass.getName())) {
            retVal = new BigDecimal(value.toString());
        } else if(Short.class.getName().equals(fieldTypeClass.getName())
                || short.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Short.parseShort(value.toString());
        } else {
            retVal = value;    
        }    
        return retVal;    
    }
  
    public static void main(String[] args) {  

  
    }   
}
