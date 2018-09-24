package ru.workflow.enumeration;

import ru.workflow.documents.Incoming;
import ru.workflow.documents.Outgoing;
import ru.workflow.documents.Task;

/**
 *  Типы документов
 *  @author IShabalin
 */
public enum DocumentType {

    INCOMING(Incoming.class, "incoming"),
    OUTGOING(Outgoing.class, "outgoing"),
    TASK(Task.class, "task");

    /** Класс, за которым закреплен данный объект Enum-а */
    private final Class clazz;
    /** Отображаемое наименование */
    private final String rusLocale;

    DocumentType(Class clazz, String rusLocale) {
        this.rusLocale = rusLocale;
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getRusLocale() {
        return rusLocale;
    }
}