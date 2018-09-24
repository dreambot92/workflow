package ru.workflow.exception;

/**
 *  Исключение, которое вызывается при совпадении регистрационных номеров
 *  @author IShabalin
 */
public class DocumentExistsException extends Exception {

    /** @param message сообщение об ошибке */
    public DocumentExistsException(String message) {
        super(message);
    }
}
