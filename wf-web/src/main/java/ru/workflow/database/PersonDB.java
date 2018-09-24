package ru.workflow.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.staff.Person;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Класс для работы с базой данных с объектами класса Person
 *  @author IShabalin
 */
public class PersonDB {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDB.class);

    private static final String FIND_ALL = "select * from person";
    private static final String FIND_BY_ID = "select * from person where id = ?";
    private static final String INSERT = "insert into person(id, surname, first_name, patronymic, post, department_id) values(?,?,?,?,?,?)";
    private static final String UPDATE = "update person set surname = ?, first_name = ?, patronymic = ?, post = ? where id = ?";
    private static final String DELETE_ALL = "delete from person";
    private static final String DELETE = "delete from person where id = ?";

    private PersonDB() {   }

    /**
     *  Достать список рабоников из базы
     *  @return список из всех работников в базе
     */
    public static List<Person> findAll() throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(FIND_ALL);
            ResultSet result = ps.executeQuery()
        ) {
            List<Person> list = new ArrayList<>();
            while (result.next()) {
                list.add(collectEntity(result));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Не получается выполнить запрос на чтение рабочего из базы данных", e);
        }
    }

    /**
     *  Достать конкретного работника из базы по id
     *  @value id идентификатор рабочего
     *  @return тот самый работник
     */
    public static Person find(int id) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(FIND_BY_ID);
            ResultSet result = ps.executeQuery()
        ) {
            ps.setInt(1, id);
            result.next();
            return collectEntity(result);
        } catch (SQLException e) {
            throw new SQLException(String.format("Не получается считать рабочего с id: %s с базы данных", id), e);
        }
    }

    /**
     *  Собирает рабочего из результата запроса
     *  @param result результат запроса
     *  @return собранный рабочий
     */
    private static Person collectEntity(ResultSet result) throws SQLException {

        Person person = new Person();
        person.setId(result.getInt("id"));
        person.setSurname(result.getString("surname"));
        person.setFirstName(result.getString("first_name"));
        person.setPatronymic(result.getString("patronymic"));
        person.setPost(result.getString("post"));
        return person;
    }

    /**
     *  Запись работника в базу данных
     *  @param person объект класса Department для записи
     *  @return http-код состояния:<br>
     *      <u>200</u> - данные записаны<br>
     *      <u>400</u> - данные не записаны
     */
    public static Integer add(Person person) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(INSERT)
        ) {
            ps.setInt(1, person.getId());
            ps.setString(2, person.getSurname());
            ps.setString(3, person.getFirstName());
            ps.setString(4, person.getPatronymic());
            ps.setString(5, person.getPost());
            if (person.getDepartmentId() != null) {
                ps.setInt(6, person.getDepartmentId());
            } else {
                ps.setString(6, null);
            }
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Работник '%s' вставлен успешно", person.toString()));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.warn(String.format("Что-то произошло. Работник не записан. %s", person.toString()));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("Не получается выполнить запрос на добавление рабочего в базу данных", e);
        }
    }

    /**
     *  Изменение записи работника в базе данных
     *  @param person сущность работника
     *  @return http-код состояния:<br>
     *      <u>200</u> - данные перезаписаны<br>
     *      <u>400</u> - данные не перезаписаны
     */
    public static Integer edit(Person person) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, person.getSurname());
            ps.setString(2, person.getFirstName());
            ps.setString(3, person.getPatronymic());
            ps.setString(4, person.getPost());
            ps.setInt(5, person.getId());
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Данные по рабочему с id %d изменены", person.getId()));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.info(String.format("Данные по рабочему с id %d не изменены", person.getId()));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("При удалении данных из таблицы person произошла ошибка", e);
        }
    }

    /**
     *  Удалить всех рабочих из базы данных
     *  @return http-код состояния:<br>
     *      <u>200</u> - работники успешно удалены<br>
     *      <u>400</u> - работники не удалены
     */
    public static Integer remove() throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(DELETE_ALL)
        ) {
            if(ps.executeUpdate() > 0) {
                LOGGER.info("Данные из таблицы person удалены");
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.info("Таблица person уже пуста");
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("При удалении данных из таблицы person произошла ошибка", e);
        }
    }

    /**
     *  Удалить конкретного рабочего из базы данных
     *  @return http-код состояния:<br>
     *      <u>200</u> - работник успешно удалены<br>
     *      <u>400</u> - работник не удалены
     */
    public static Integer removeById(int id) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(DELETE)
        ) {
            ps.setInt(1, id);
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Данные рабочего с id = %d удалены", id));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.info(String.format("Данные рабочего с id = %d не удалены.", id));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("При удалении данных из таблицы person произошла ошибка", e);
        }
    }
}
