package ru.leasoft.javalie;

import org.junit.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Javalie {

    @SuppressWarnings("unchecked")
    public static <T> T expectConstraintViolationExceptionWhen(Consumer<T> methodInvocation, T invalidValue) {

        Assert.assertNotNull(methodInvocation);
        Assert.assertNotNull(invalidValue);

        try {

            methodInvocation.accept(invalidValue);
            Assert.fail("ConstraintViolationException was expected");
        } catch (ConstraintViolationException cve) {
            StubbingFactory.InvocationProgress progress =
                    new StubbingFactory.InvocationProgress("", cve.getConstraintViolations());

            return (T) StubbingFactory.stub(invalidValue.getClass(), progress);
        }

        throw new RuntimeException(); //unreachable
    }

    private static void resetInvocationDetails(Set<ConstraintViolation<?>> violations) {

        StubbingFactory.InvocationProgress details =
                new StubbingFactory.InvocationProgress("", violations);
        StubbingFactory.invocationProgressThreadLocal.set(details);
    }

    public static <T> RunningStubbing assertThatField(T methodCall) {

        StubbingFactory.InvocationProgress invocationProgress = StubbingFactory.invocationProgressThreadLocal.get();
        resetInvocationDetails(invocationProgress.violations);

        return new RunningStubbing(invocationProgress.path, invocationProgress.violations);
    }

    public static class RunningStubbing {

        private List<Class> violatedConstraintsClasses = new ArrayList<>();

        public RunningStubbing(String path, Set<ConstraintViolation<?>> violations) {

            violatedConstraintsClasses =
            violations.stream().filter(v -> v.getPropertyPath().toString().equals(path))
                .map(v -> v.getConstraintDescriptor().getAnnotation().annotationType()).collect(Collectors.toList());
        }

        public void violatesConstraint(Class constraint) {

            boolean constraintViolated = violatedConstraintsClasses.contains(constraint);

            Assert.assertTrue("No constraint violation was generated", constraintViolated);
        }
    }

}
