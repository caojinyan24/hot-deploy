package swa.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import swa.annotation.ValueSetter;
import swa.service.CallBackLoader;
import swa.service.DataStorer;
import swa.service.DataUpdater;
import swa.service.ListenerConfig;

import java.lang.reflect.Field;

/**
 * Created by jinyan on 5/25/17.
 */
public class BeanInitProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Field[] fields = bean.getClass().getFields();//只处理属性上的注解
        for (final Field field : fields) {
            if (field.getAnnotation(ValueSetter.class) != null) {
                DataUpdater.addListener(new ListenerConfig("filename.properties", new CallBackLoader() {
                    public void loadData() {
                        try {
                            System.out.println("postProcessAfterInitialization" + beanName);
                            field.set(bean, DataStorer.getValue(field.getAnnotation(ValueSetter.class).value()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            }
        }
        return bean;
    }
}
