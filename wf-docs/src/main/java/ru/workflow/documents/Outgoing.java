package ru.workflow.documents;

import ru.workflow.annotation.DocumentField;
import ru.workflow.enumeration.DeliveryMethods;
import ru.workflow.enumeration.DocumentType;
import ru.workflow.enumeration.SetterType;
import ru.workflow.staff.Person;

import java.util.Objects;

/**
 *  Исходящий документ
 *  @author IShabalin
 */
public class Outgoing extends Document {

    /** Адресат */
    private Person acceptor;
    /** Способ доставки */
    private DeliveryMethods deliveryMethods;

    public Outgoing() {
        super.type = DocumentType.OUTGOING.getRusLocale();
    }

    public Person getAcceptor() {
        return this.acceptor;
    }

    @DocumentField(SetterType.PERSON)
    public void setAcceptor(Person acceptor) {
        this.acceptor = acceptor;
    }

    public DeliveryMethods getDeliveryMethods() {
        return deliveryMethods;
    }

    @DocumentField(SetterType.DELIVERY_METHOD)
    public void setDeliveryMethods(DeliveryMethods deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Outgoing outgoing = (Outgoing) o;
        return Objects.equals(acceptor, outgoing.acceptor) &&
                deliveryMethods == outgoing.deliveryMethods;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), acceptor, deliveryMethods);
    }

    @Override
    public String toString() {
        return "Outgoing{" +
                super.toString() +
                "acceptor=" + acceptor +
                ", deliveryMethods=" + deliveryMethods +
                '}';
    }
}
