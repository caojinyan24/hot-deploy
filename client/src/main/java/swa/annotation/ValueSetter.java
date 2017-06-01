package swa.annotation;

import com.sun.istack.internal.NotNull;

import java.lang.annotation.*;

/**
 * Created by jinyan on 5/25/17.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ValueSetter {
    public String value();

}
