package swa.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import swa.obj.ConfigFile;
import swa.service.CallBackLoader;
import swa.service.DataSetterService;
import swa.service.ListenerConfig;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 程序启动时对相关域或方法做注入
 * Created by jinyan on 5/25/17.
 */
public class BeanInitProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BeanInitProcessor.class);

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();//只处理属性上的注解
        for (final Field field : fields) {
            if (field.getAnnotation(ValueSetter.class) != null) {
                String fileName = field.getAnnotation(ValueSetter.class).value();
                DataSetterService.addListener(new ListenerConfig(fileName, new CallBackLoader() {
                    public void loadData(ConfigFile file) {
                        setField(field, bean, file.getContent());
                    }
                }, null));
            }
        }

        return bean;
    }

    private void setField(Field field, Object bean, Map<String, String> map) {
        try {
            field.setAccessible(true);
            field.set(bean, map);
        } catch (IllegalAccessException e) {
            logger.error("setField error:", e);
        }

    }
}
