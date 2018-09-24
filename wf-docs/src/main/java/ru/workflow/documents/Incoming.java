package ru.workflow.documents;

import ru.workflow.annotation.DocumentField;
import ru.workflow.enumeration.DocumentType;
import ru.workflow.enumeration.SetterType;
import ru.workflow.staff.Person;
import java.util.Date;
import java.util.Objects;

/**
 *  Входящий документ
 *  @author IShabalin
 */
public class Incoming extends Document {

    /** Отправитель */
    private Person sender;
    /** Адресат */
    private Person acceptor;
    /** Исходящий номер */
    private Integer outgoingNumber;
    /** Исходящая дата регистрации */
    private Date outgoingRegistrationDate;

    public Incoming() {
        super.type = DocumentType.INCOMING.getRusLocale();
    }

    public Person getSender() {
        return sender;
    }

    @DocumentField(SetterType.PERSON)
    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getAcceptor() {
        return acceptor;
    }

    @DocumentField(SetterType.PERSON)
    public void setAcceptor(Person acceptor) {
        this.acceptor = acceptor;
    }

    public Integer getOutgoingNumber() {
        return outgoingNumber;
    }

    @DocumentField(SetterType.INTEGER)
    public void setOutgoingNumber(Integer outgoingNumber) {
        this.outgoingNumber = outgoingNumber;
    }

    public Date getOutgoingRegistrationDate() {
        return outgoingRegistrationDate;
    }

    @DocumentField(SetterType.DATE)
    public void setOutgoingRegistrationDate(Date outgoingRegistrationDate) {
        this.outgoingRegistrationDate = outgoingRegistrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Incoming incoming = (Incoming) o;
        return Objects.equals(sender, incoming.sender) &&
                Objects.equals(acceptor, incoming.acceptor) &&
                Objects.equals(outgoingNumber, incoming.outgoingNumber) &&
                Objects.equals(outgoingRegistrationDate, incoming.outgoingRegistrationDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), sender, acceptor, outgoingNumber, outgoingRegistrationDate);
    }

    @Override
    public String toString() {
        return "Incoming{" +
                super.toString() +
                "sender=" + sender +
                ", acceptor=" + acceptor +
                ", outgoingNumber=" + outgoingNumber +
                ", outgoingRegistrationDate=" + outgoingRegistrationDate +
                '}';
    }
}
