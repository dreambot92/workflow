package ru.workflow.staff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Collection;
import java.util.Objects;

/**
 *  Подразделение
 *  @author IShabalin
 */
@XmlRootElement(name = "Department")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Department extends Staff {

    /** Полное наименование */
    private String name;
    /** Краткое наименование */
    private String shortName;
    /** Начальник подразделения */
    private Person head;
    /** Контактные телефоны */
    private Collection<String> phones;
    /** Организация, за которым закреплено данное подразделение */
    private Integer organizationId;

    @XmlElement(required = true, name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(required = true, name = "short_name")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @XmlTransient
    public Person getHead() {
        return head;
    }

    public void setHead(Person head) {
        this.head = head;
    }

    @XmlElement(required = true, name = "head_id")
    public Integer getHeadId() {
        if (head == null) {
            return null;
        }
        return head.getId();
    }

    @XmlElementWrapper(name="phones")
    @XmlElement(name="phone")
    public Collection<String> getPhones() {
        return phones;
    }

    public void setPhones(Collection<String> phones) {
        this.phones = phones;
    }

    @XmlAttribute(required = true, name = "organization_id")
    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(shortName, that.shortName) &&
                Objects.equals(head, that.head) &&
                Objects.equals(phones, that.phones) &&
                Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, shortName, head, phones, organizationId);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", head=" + head +
                ", phones=" + phones +
                ", organizationId=" + organizationId +
                ", id=" + id +
                '}';
    }
}