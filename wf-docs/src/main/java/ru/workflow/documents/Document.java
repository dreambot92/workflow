package ru.workflow.documents;

import ru.workflow.annotation.DocumentField;
import ru.workflow.enumeration.SetterType;
import ru.workflow.interfaces.Storable;
import ru.workflow.staff.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *  Базовый абстрактный класс
 *  @author IShabalin
 */
public abstract class Document implements Comparable<Document>, Storable {

    /** Идентификатор документа */
    private int id;
    /** Название документа */
    private String name;
    /** Текст документа */
    private String text;
    /** Регистрационный номер документа */
    private Integer registrationNumber;
    /** Дата регистрации документа */
    private Date registrationDate;
    /** Автор документа */
    private Person author;
    /** Тип документа */
    protected String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @DocumentField(SetterType.STRING)
    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    @DocumentField(SetterType.STRING)
    public void setText(String text) {
        this.text = text;
    }

    public Integer getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    @DocumentField(SetterType.DATE)
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Person getAuthor() {
        return author;
    }

    @DocumentField(SetterType.PERSON)
    public void setAuthor(Person author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    /** Представление документа в отчете */
    public String outputDocumentParam() {

        return String.format("%s №%d от %s. %s", type, registrationNumber, new SimpleDateFormat("dd.MM.yyyy").format(registrationDate), name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id == document.id &&
                Objects.equals(name, document.name) &&
                Objects.equals(text, document.text) &&
                Objects.equals(registrationNumber, document.registrationNumber) &&
                Objects.equals(registrationDate, document.registrationDate) &&
                Objects.equals(author, document.author) &&
                Objects.equals(type, document.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, text, registrationNumber, registrationDate, author, type);
    }

    /** Реализуем метод compareTo для сравнения по дате, потом по регистрационному номеру */
    @Override
    public int compareTo(Document document) {

        int result = -this.registrationDate.compareTo(document.registrationDate);
        if (result == 0) {
            result = this.registrationNumber.compareTo(document.registrationNumber);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", registrationNumber=" + registrationNumber +
                ", registrationDate=" + registrationDate +
                ", author=" + author +
                '}';
    }
}
