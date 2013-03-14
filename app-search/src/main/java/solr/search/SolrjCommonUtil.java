package solr.search;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SolrjCommonUtil {

    private static Log logger = LogFactory.getLog(SolrjCommonUtil.class);
    /**
     * 执行set方法
     * @param model 类实例
     * @param fieldName 属性名称
     * @param value 属性值
     */
    public static void invokSetMethod(Object model,String fieldName,Object value){
        String fieldName1 = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        try {
            Method setMethod= model.getClass().getMethod("set" + fieldName1,new Class[]{ model.getClass().getField(fieldName).getType() });
            setMethod.invoke(model, value);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 执行get方法
     * @param model
     * @param fieldName
     * @param value
     * @return
     */
    public static Object invokGetMethod(Object model,String fieldName,Object value){
        String fieldName1 = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        Object object = null;
        try {
            Method getMethod= model.getClass().getMethod("get" + fieldName1,new Class[]{ model.getClass().getField(fieldName).getType() });
            object = getMethod.invoke(model);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }

}
