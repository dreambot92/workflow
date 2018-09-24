package ru.workflow.annotation;

import ru.workflow.enumeration.SetterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Аннотация для сеттеров класса.
 *  Позволяет рандомизировать значения согласно типу, указанному в value()
 */
@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface DocumentField {

    SetterType value();
}
