package ru.workflow.enumeration;

import ru.workflow.StaffCollections;

import java.util.Date;
import java.util.Random;
import java.util.function.Supplier;

/**
 *  Тип переменной в сеттере. Метод getRandomElement() генерирует рандомный элемент согласно типу
 *  @author IShabalin
 */
public enum SetterType {

    INTEGER(() -> getRandom().nextInt(10000000)),
    BOOLEAN(() -> getRandom().nextBoolean()),
    STRING(() -> java.lang.String.valueOf(getRandom().nextInt(10000000))),
    DATE(() -> new Date(System.currentTimeMillis() - getRandom().nextInt(1000*60*60*24*365))),
    DELIVERY_METHOD(() -> {
        int randomDeliveryMethods = getRandom().nextInt(DeliveryMethods.values().length);
        return DeliveryMethods.values()[randomDeliveryMethods];
    }),
    PERSON(() -> {
        int randomPerson = getRandom().nextInt(StaffCollections.getPersonsList().size());
        return StaffCollections.getPersonsList().get(randomPerson);
    });

    /** Статический объект класса Random для рандомизации */
    private static Random random = new Random();

    private Supplier<?> randomElement;

    SetterType(Supplier<?> randomElement) {
        this.randomElement = randomElement;
    }

    /** Получить рандомный элемент согласно типу */
    public Object getRandomElement() {
        return randomElement.get();
    }

    public static Random getRandom() {
        return random;
    }
}