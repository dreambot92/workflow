package ru.workflow.documents;

import ru.workflow.annotation.DocumentField;
import ru.workflow.enumeration.DocumentType;
import ru.workflow.enumeration.SetterType;
import ru.workflow.staff.Person;
import java.util.Date;
import java.util.Objects;

/**
 *  Поручение
 *  @author IShabalin
 */
public class Task extends Document {

    /** Дата выдачи поручения */
    private Date dateBegin;
    /** Срок исполнения поручения */
    private Date dateEnd;
    /** Ответственный исполнитель */
    private Person executor;
    /** Признак контрольности */
    private Boolean control;
    /** Контроллер */
    private Person controller;

    public Task() {
        super.type = DocumentType.TASK.getRusLocale();
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    @DocumentField(SetterType.DATE)
    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    @DocumentField(SetterType.DATE)
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Person getExecutor() {
        return executor;
    }

    @DocumentField(SetterType.PERSON)
    public void setExecutor(Person executor) {
        this.executor = executor;
    }

    public Boolean getControl() {
        return control;
    }

    @DocumentField(SetterType.BOOLEAN)
    public void setControl(Boolean control) {
        this.control = control;
    }

    public Person getController() {
        return controller;
    }

    @DocumentField(SetterType.PERSON)
    public void setController(Person controller) {
        this.controller = controller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(dateBegin, task.dateBegin) &&
                Objects.equals(dateEnd, task.dateEnd) &&
                Objects.equals(executor, task.executor) &&
                Objects.equals(control, task.control) &&
                Objects.equals(controller, task.controller);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), dateBegin, dateEnd, executor, control, controller);
    }

    @Override
    public String toString() {
        return "Task{" +
                super.toString() +
                "dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", executor=" + executor +
                ", control=" + control +
                ", controller=" + controller +
                '}';
    }
}
