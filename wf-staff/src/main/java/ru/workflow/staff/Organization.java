package ru.workflow.staff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collection;
import java.util.Objects;

/**
 *  Организация
 *  @author IShabalin
 */
@XmlRootElement(name = "Organization")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Organization extends Staff {

    /** Полное наименование */
    private String name;
    /** Краткое наименование */
    private String shortName;
    /** Начальник */
    private Integer headId;
    /** Контактные телефоны */
    private Collection<String> phones;

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

    @XmlElement(required = true, name = "head_id")
    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    @XmlElementWrapper(name="phones")
    @XmlElement(name="phone")
    public Collection<String> getPhones() {
        return phones;
    }

    public void setPhones(Collection<String> phones) {
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(shortName, that.shortName) &&
                Objects.equals(headId, that.headId) &&
                Objects.equals(phones, that.phones);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, shortName, headId, phones);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", headId=" + headId +
                ", phones=" + phones +
                ", id=" + id +
                '}';
    }
}