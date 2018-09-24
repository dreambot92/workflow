package ru.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.annotation.DocumentField;
import ru.workflow.documents.Document;
import ru.workflow.enumeration.DocumentType;
import ru.workflow.enumeration.SetterType;
import ru.workflow.exception.DocumentExistsException;
import ru.workflow.staff.Person;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  Фабрика.
 *  Запрещаем создавать потомков класса Factory
 *  @author IShabalin
 */
public final class Factory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Factory.class);

    /** Блокировка коллекции registrationNumber на чтение или запись */
    private static ReentrantReadWriteLock rwlRegistrationNumber = new ReentrantReadWriteLock();
    /** Блокировка коллекции документов только на чтение */
    private static Lock lockListDocument = new ReentrantReadWriteLock().readLock();

    private Factory() {
    }

    /**
     *  Создание объекта с рандомными значениями полей. Используется reflection.
     *  @param documentType тип документа, определяемый enum DocumentType. Incoming(входящий),
     *                      Outgoing(исходящий), Task(поручение)
     */
    public static void createDocument(DocumentType documentType) throws IllegalAccessException,
            InstantiationException, InvocationTargetException, DocumentExistsException, InterruptedException {

        // достаем класс данного типа документов и создаем его экземляр
        Class clazz = documentType.getClazz();
        Document document = (Document) clazz.newInstance();
        // перебираем методы класса
        for (Method method : clazz.getMethods()) {
            // проверяем наличие аннотации в сеттерах документа
            if (method.isAnnotationPresent(DocumentField.class)) {
                try {
                    // пытаемся задать значение документу с помощью сеттера
                    SetterType setterType = method.getAnnotation(DocumentField.class).value();
                    method.invoke(document, setterType.getRandomElement());
                    LOGGER.debug(String.format("Сеттер %s успешно заменил значение поля объекта", method.getName()));
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetException(e, String.format("Во время работы сеттера %s произошла ошибка", method));
                }
            }
        }
        // задаем регистрационный номер документу
        setRegistrationNumber(document);
        Person author = document.getAuthor();
        // запрашиваем блокировку
        lockListDocument.lockInterruptibly();
        try {
            // добавляем документ в listDocuments (TreeMap<Person, TreeSet<Document>>)
            // берем множество у author-а или создаем новое и добавляем в это множество текущий документ
            DocumentCollections.getListDocuments().computeIfAbsent(author, k -> new TreeSet<>()).add(document);
            LOGGER.debug(String.format("Объект класса %s успешно создан", clazz.getName()));
        } finally {
            lockListDocument.unlock();
        }
    }

    /**
     *  Создание регистрационного номера для документа. Номер рандомизируется.
     *  Если рандомный номер совпадает с уже имеющимся в базе, то выбрасывает исключение.
     *  @param document объект класса Document
     *  @exception DocumentExistsException выбрасывается, если есть совпадение регистрационных номеров
     */
    private static void setRegistrationNumber(Document document) throws DocumentExistsException, InterruptedException {

        // создаем рандомное значение типа Integer
        Integer newRegistrationNumber = (Integer) SetterType.INTEGER.getRandomElement();
        // проверяем, есть ли такой регистрационный номер в базе
        rwlRegistrationNumber.writeLock().lockInterruptibly();
        try {
            if (DocumentCollections.getRegistrationNumbers().contains(newRegistrationNumber))
                throw new DocumentExistsException(String.format("Такой номер уже есть в базе. %d",
                        newRegistrationNumber));
        } finally {
            rwlRegistrationNumber.writeLock().unlock();
        }
        // после этого присваиваем документу регистрационный номер и сохраняем этот номер в базу
        rwlRegistrationNumber.readLock().lockInterruptibly();
        try {
            document.setRegistrationNumber(newRegistrationNumber);
            DocumentCollections.getRegistrationNumbers().add(newRegistrationNumber);
        } finally {
            rwlRegistrationNumber.readLock().unlock();
        }
    }
}
