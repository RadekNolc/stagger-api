package cz.radeknolc.stagger.annotation;

import cz.radeknolc.stagger.model.BaseEntity;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {
    Class<? extends BaseEntity> entityClass();
    String targetColumn();
    String message() default "must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
