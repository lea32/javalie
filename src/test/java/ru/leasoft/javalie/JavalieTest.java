package ru.leasoft.javalie;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.leasoft.javalie.samples.SampleEntity;

import javax.validation.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static ru.leasoft.javalie.Javalie.*;

public class JavalieTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidation() {

        SampleEntity se = new SampleEntity();
        Set<ConstraintViolation<SampleEntity>> violations = validator.validate(se);

        Assert.assertFalse(violations.isEmpty());
    }

    public void runValidation(SampleEntity entity) {
        Set<ConstraintViolation<SampleEntity>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Test
    public void testJavalie() {

        SampleEntity se = expectConstraintViolationExceptionWhen(this::runValidation, invalidValue());

        assertThatField(se.isSomethingTrue()).violatesConstraint(AssertTrue.class);

        assertThatField(se.getStringField1()).violatesConstraint(NotNull.class);
        assertThatField(se.getStringField2()).violatesConstraint(Size.class);
        assertThatField(se.getGetNestedEntityField1()).violatesConstraint(NotNull.class);

        assertThatField(se.getNestedEntityField2().getNestedEntityStringField()).violatesConstraint(NotNull.class);
    }

    @Test(expected = AssertionError.class)
    public void testJavalieExpectationFails1() {

        SampleEntity se = expectConstraintViolationExceptionWhen(this::runValidation, invalidValue());

        assertThatField(se.getStringField1()).violatesConstraint(Size.class);
    }

    @Test(expected = AssertionError.class)
    public void testJavalieExpectationFails2() {

        SampleEntity se = expectConstraintViolationExceptionWhen(this::runValidation, invalidValue());

        assertThatField(se.getNestedEntityField2().getNestedIntegerField()).violatesConstraint(NotNull.class);
    }

    @Test(expected = RuntimeException.class)
    public void testNonGetterFieldAccess() {

        SampleEntity se = expectConstraintViolationExceptionWhen(this::runValidation, invalidValue());

        assertThatField(se.getGetNestedEntityField1().nonGetter());
    }

    private SampleEntity invalidValue() {
        return new SampleEntity();
    }

}
