package ru.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.interfaces.ConvertFromXML;
import ru.workflow.staff.Department;
import ru.workflow.staff.Organization;
import ru.workflow.staff.Person;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  Коллекции работников, подразделений и организаций, которые берутся из XML файла
 *  @author IShabalin
 */
public final class StaffCollections {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffCollections.class);

    /** Список работников */
    private static List<Person> personList = new ArrayList<>();
    /** Список подразделений */
    private static List<Department> departmentList = new ArrayList<>();
    /** Список организаций */
    private static List<Organization> organizationList = new ArrayList<>();

    private StaffCollections() {   }

    // лениво подгружаем объекты в коллекции при первом обращении к ним
    static {
        try {
            getPersonFromXML();
            getDepartmentFromXML();
            getOrganizationFromXML();
        } catch (JAXBException e) {
            LOGGER.error("Не получилось считать данные организационных единиц из XML файла");
        }
    }

    /** Достать список из объектов класса Person с XML файла и записать в коллекцию personList */
    private static void getPersonFromXML() throws JAXBException {

        ConvertFromXML convert = () -> {
            ClassLoader classLoader = Person.class.getClassLoader();
            return ConvertFromXML.convert(classLoader.getResourceAsStream(ProjectProperties.getPersonXmlPath()));
        };
        Data data = convert.convertFromXml();
        if (data != null) {
            personList = data.getPersons();
        }
    }

    /** Достать список из объектов класса Department с XML файла и записать в коллекцию departmentList */
    private static void getDepartmentFromXML() throws JAXBException {

        ConvertFromXML convert = () -> {
            ClassLoader classLoader = Department.class.getClassLoader();
            return ConvertFromXML.convert(classLoader.getResourceAsStream(ProjectProperties.getDepartmentXmlPath()));
        };
        Data data = convert.convertFromXml();
        if (data != null) {
            departmentList = data.getDepartments();
        }
    }

    /** Достать список из объектов класса Organization с XML файла и записать в коллекцию organizationList */
    private static void getOrganizationFromXML() throws JAXBException {

        ConvertFromXML convert = () -> {
            ClassLoader classLoader = Organization.class.getClassLoader();
            return ConvertFromXML.convert(classLoader.getResourceAsStream(ProjectProperties.getOrganizationXmlPath()));
        };
        Data data = convert.convertFromXml();
        if (data != null) {
            organizationList = data.getOrganizations();
        }
    }

    /**
     *  Получаем список всех работников с XML по пути, указанному в Person.PATH_NAME
     *  @return коллекция работников или пустая коллекция, если в XML-ке ничего нет
     */
    public static List<Person> getPersonsList() {

        if (personList == null) {
            LOGGER.debug(String.format("В файле '%s' никаких пользователей найдено не было", ProjectProperties.getPersonXmlPath()));
            return Collections.emptyList();
        }
        return personList;
    }

    /**
     *  Получаем список всех подразделений с XML по пути, указанному в Department.PATH_NAME
     *  @return коллекция подразделений или пустая коллекция, если в XML-ке ничего нет
     */
    public static List<Department> getDepartmentList() {

        if (personList == null) {
            LOGGER.debug(String.format("В файле '%s' никаких подразделений найдено не было", ProjectProperties.getDepartmentXmlPath()));
            return Collections.emptyList();
        }
        return departmentList;
    }

    /**
     *  Получаем список всех подразделений с XML по пути, указанному в Department.PATH_NAME
     *  @return коллекция подразделений или пустая коллекция, если в XML-ке ничего нет
     */
    public static List<Organization> getOrganizationList() {

        if (organizationList == null) {
            LOGGER.debug(String.format("В файле '%s' никаких организаций найдено не было", ProjectProperties.getOrganizationXmlPath()));
            return Collections.emptyList();
        }
        return organizationList;
    }
}