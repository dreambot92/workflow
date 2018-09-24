package ru.workflow.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.StaffCollections;
import ru.workflow.staff.Department;
import ru.workflow.staff.Organization;
import ru.workflow.staff.Person;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 *  Main-class для работы с базой данных. В статическом блоке настраивается соединение с JNDI
 *  @author IShabalin
 */
public class DatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);

    /** JNDI для соединения к БД */
    private static final String JDBC_RESOURCES = "jdbc/wfBase";
    /** Логин для доступа к БД */
    private static final String USER = "root";
    /** Пароль для доступа к БД */
    private static final String PASS = "password";

    private static DataSource ds;

    private DatabaseService() {   }

    static {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource)ctx.lookup(JDBC_RESOURCES);
        } catch (NamingException e) {
            LOGGER.error("Нет соединения с базой данных через JNDI");
        }
    }

    /** Сохранение объектов из XML файлов в базу данных */
    public static void saveInDatabase() throws SQLException {

        // очищаем базу данных
        OrganizationDB.remove();
        DepartmentDB.remove();
        PersonDB.remove();
        // чтобы не было проблемы с foreign keys:
        // записываем все организации и подразделения без указателей на руководителей и начальников
        for (Organization organization : StaffCollections.getOrganizationList()) {
            OrganizationDB.add(organization);
        }
        for (Department department : StaffCollections.getDepartmentList()) {
            DepartmentDB.add(department);
        }
        // записываем всех работников
        for (Person person : StaffCollections.getPersonsList()) {
            PersonDB.add(person);
        }
        // добавляем начальников и руководителей
        for (Organization organization : StaffCollections.getOrganizationList()) {
            OrganizationDB.addHeadId(organization.getId(), organization.getHeadId());
        }
        for (Department department : StaffCollections.getDepartmentList()) {
            DepartmentDB.addHeadId(department.getId(), department.getHeadId());
        }
    }

    public static DataSource getDs() {
        return ds;
    }

    public static String getUSER() {
        return USER;
    }

    public static String getPASS() {
        return PASS;
    }
}
