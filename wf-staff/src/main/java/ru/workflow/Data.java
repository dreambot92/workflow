package ru.workflow;

import ru.workflow.staff.Department;
import ru.workflow.staff.Organization;
import ru.workflow.staff.Person;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *  Root-корень для xml элементов
 *  @author IShabalin
 */
@XmlRootElement(name="data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {

    /** Коллекция работников, взятая из XML файла */
    @XmlElement(name="Person")
    private List<Person> persons;
    /** Коллекция подразделений, взятая из XML файла */
    @XmlElement(name="Department")
    private List<Department> departments;
    /** Коллекция организаций, взятая из XML файла */
    @XmlElement(name="Organization")
    private List<Organization> organizations;

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public String toString() {
        return "Data{" +
                (persons == null ? "" : "persons = " + persons) +
                (departments == null ? "" : "departments = " + departments) +
                (organizations == null ? "" : "organizations = " + organizations) +
                '}';
    }
}