package com.example.support;

import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.dynamic.DynamicType.Builder;

import javax.validation.Constraint;

import java.io.IOException;

import static net.bytebuddy.matcher.ElementMatchers.annotationType;
import static net.bytebuddy.matcher.ElementMatchers.hasAnnotation;

public class RecordValidationPlugin implements Plugin {

    @Override
    public boolean matches(TypeDescription target) {
        return target.getDeclaredMethods()
                .stream()
                .anyMatch(m -> m.isConstructor() && hasConstrainedParameter(m));
    }

    @Override
    public Builder<?> apply(Builder<?> builder,
                            TypeDescription typeDescription,
                            ClassFileLocator classFileLocator) {

        return builder.constructor(this::hasConstrainedParameter)
                .intercept(SuperMethodCall.INSTANCE.andThen(
                        MethodDelegation.to(RecordValidationInterceptor.class)));
    }

    private boolean hasConstrainedParameter(MethodDescription method) {
        return method.getParameters()
                .asDefined()
                .stream()
                .anyMatch(p -> isConstrained(p));
    }

    private boolean isConstrained(
            ParameterDescription.InDefinedShape parameter) {

        return !parameter.getDeclaredAnnotations()
                .asTypeList()
                .filter(hasAnnotation(annotationType(Constraint.class)))
                .isEmpty();
    }

    @Override
    public void close() throws IOException {
    }
}