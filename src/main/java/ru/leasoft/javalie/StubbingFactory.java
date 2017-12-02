package ru.leasoft.javalie;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import javax.validation.ConstraintViolation;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Set;

public class StubbingFactory {

    static ThreadLocal<InvocationProgress> invocationProgressThreadLocal = new ThreadLocal<>();

    public static Object stub(Class clazz, InvocationProgress details) {

        invocationProgressThreadLocal.set(details);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);

        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> stubbingInterceptor(method));

        return enhancer.create();
    }

    private static Object stubbingInterceptor(Method method) {

        String fieldName = introspectFieldName(method);
        if (fieldName == null) {
            throw new RuntimeException("Field not found for method: " + method.getName());
        }

        InvocationProgress details = invocationProgressThreadLocal.get();
        details = details.appendPathFragment(fieldName);
        invocationProgressThreadLocal.set(details);

        try {
            return stub(method.getReturnType(), details);
        } catch (Exception anyException) {
            return null;
        }
    }

    public static class InvocationProgress {

        public String path;
        public Set<ConstraintViolation<?>> violations;

        public InvocationProgress(String path, Set<ConstraintViolation<?>> violations) {
            this.path = path;
            this.violations = violations;
        }

        public InvocationProgress appendPathFragment(String fragment) {

            String nextPath = (this.path.isEmpty()) ? fragment : this.path + '.' + fragment;
            return new InvocationProgress(nextPath, this.violations);
        }
    }

    private static String introspectFieldName(Method method) {
        try {
            Class<?> clazz = method.getDeclaringClass();
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] properties = info.getPropertyDescriptors();
            for (PropertyDescriptor desc : properties) {
                if (method.equals(desc.getReadMethod())) {
                    return desc.getName();
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return null;
    }

}
