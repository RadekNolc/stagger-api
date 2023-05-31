package cz.radeknolc.stagger.annotation;

import jakarta.persistence.Table;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExistsValidator implements ConstraintValidator<Exists, Object> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String tableName;
    private String targetColumn;

    @Override
    public void initialize(Exists constraintAnnotation) {
        tableName = constraintAnnotation.entityClass().getAnnotation(Table.class).name();
        targetColumn = constraintAnnotation.targetColumn();
    }

    @Override
    public boolean isValid(Object charSequence, ConstraintValidatorContext constraintValidatorContext) {
        Integer result = jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", tableName, targetColumn), Integer.class, charSequence.toString());
        return result != null && result > 0;
    }
}
