package ru.workflow.staff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Objects;

/**
 *  Сотрудник
 *  @author IShabalin
 */
@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Person extends Staff implements Comparable<Person> {

    /** Фамилия */
    private String surname;
    /** Имя */
    private String firstName;
    /** Отчество */
    private String patronymic;
    /** Должность */
    private String post;
    /** Идентификатор подкрепления, за которым закреплен данный сотрудник */
    private Integer departmentId;

    @XmlElement(required = true, name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @XmlElement(required = true, name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlElement(required = true, name = "patronymic")
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @XmlElement(required = true, name = "post")
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @XmlAttribute(required = true, name = "department_id")
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(surname, person.surname) &&
                Objects.equals(firstName, person.firstName) &&
                Objects.equals(patronymic, person.patronymic) &&
                Objects.equals(post, person.post) &&
                Objects.equals(departmentId, person.departmentId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(surname, firstName, patronymic, post, departmentId);
    }

    /** Реализуем метод compareTo для сравнения по фамилии, имени, отчеству. */
    @Override
    public int compareTo(Person p) {

        int result = this.surname.compareTo(p.surname);
        if (result == 0) {
            result = this.firstName.compareTo(p.firstName);
            if (result == 0) {
                result = this.patronymic.compareTo(p.patronymic);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.join(" ", surname, firstName, patronymic);
    }
}