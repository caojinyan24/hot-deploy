package swa.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import swa.service.CallBackLoader;
import swa.service.DataStorer;
import swa.service.ListenerConfig;
import swa.service.DataUpdater;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by jinyan on 5/25/17.
 */
public class BeanInitProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization begin:" + beanName);
        Field[] fields = bean.getClass().getDeclaredFields();//只处理属性上的注解
        for (final Field field : fields) {
            if (field.getAnnotation(ValueSetter.class) != null) {
                String fileName = field.getAnnotation(ValueSetter.class).value();
                DataUpdater.addListener(new ListenerConfig(fileName, new CallBackLoader() {
                    public void loadData(Map<String, String> value) {
                        setField(field, bean, value);
                    }
                }));
                System.out.println("postProcessAfterInitialization" + DataStorer.getValue(fileName));
                setField(field, bean, DataStorer.getValue(fileName));
            }
        }
        return bean;
    }

    private void setField(Field field, Object bean, Map<String, String> map) {
        try {
            System.out.println("set field:" + field.getName() + "#value:" + map);
            field.setAccessible(true);
            field.set(bean, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
