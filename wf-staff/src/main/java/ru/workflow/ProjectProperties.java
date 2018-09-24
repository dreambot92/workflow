package ru.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  Класс для работы с конфигурационными данными
 *  @author IShabalin
 */
public class ProjectProperties {

    /** Путь к конфиг-файлу (берет начало с 'resources/') */
    private static final String CONFIG_PATH = "staff.properties";
    /** Путь, куда в дальнейшем будут сохраняться XML файлы */
    private static String rootPath = "";
    /** Путь к xml файлу с рабочими */
    private static String personXmlPath = "";
    /** Путь к xml файлу с подразделениями */
    private static String departmentXmlPath = "";
    /** Путь к xml файлу с организациями */
    private static String organizationXmlPath = "";

    private ProjectProperties() {  }

    /** Загрузить данные с файла staff.properties */
    public static void load() throws IOException {

        ClassLoader classLoader = ProjectProperties.class.getClassLoader();
        //обращаемся к конфигу и получаем данные
        try (InputStream inputStream = classLoader.getResourceAsStream(CONFIG_PATH)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            setRootPath(prop.getProperty("path_save"));
            setPersonXmlPath(prop.getProperty("person_xml"));
            setDepartmentXmlPath(prop.getProperty("department_xml"));
            setOrganizationXmlPath(prop.getProperty("organization_xml"));
        } catch (IOException e) {
            throw new IOException("Не могу прочесть файл по адресу " + CONFIG_PATH, e);
        }
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String getPersonXmlPath() {
        return personXmlPath;
    }

    public static String getDepartmentXmlPath() {
        return departmentXmlPath;
    }

    public static String getOrganizationXmlPath() {
        return organizationXmlPath;
    }

    private static void setRootPath(String rootPath) {
        ProjectProperties.rootPath = rootPath;
    }

    private static void setPersonXmlPath(String personXmlPath) {
        ProjectProperties.personXmlPath = personXmlPath;
    }

    public static void setDepartmentXmlPath(String departmentXmlPath) {
        ProjectProperties.departmentXmlPath = departmentXmlPath;
    }

    private static void setOrganizationXmlPath(String organizationXmlPath) {
        ProjectProperties.organizationXmlPath = organizationXmlPath;
    }
}