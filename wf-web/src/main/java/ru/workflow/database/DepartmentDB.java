package ru.workflow.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.staff.Department;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Класс для работы с базой данных с объектами класса Department
 *  @author IShabalin
 */
public class DepartmentDB {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDB.class);

    private static final String FIND_ALL = "select * from department";
    private static final String FIND = "select * from department where id = ?";
    private static final String INSERT = "insert into department(id, name, short_name, phones, organization_id) values(?,?,?,?,?)";
    private static final String UPDATE_ID = "update department set head_id = ? where id = ?";
    private static final String DELETE_ALL = "delete from department";

    private DepartmentDB() {   }

    /**
     *  Достать список подразделений из базы
     *  @return этот список
     */
    public static List<Department> findAll() throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(FIND_ALL);
            ResultSet result = ps.executeQuery()
        ) {
            List<Department> list = new ArrayList<>();
            while (result.next()) {
                list.add(collectEntity(result));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Не получается выполнить запрос на чтение пдоразделений из базы данных", e);
        }
    }

    /**
     *  Достать конкретное подразделение из базы по id
     *  @value id идентификатор подразделения
     *  @return то самое подразделение
     */
    public static Department find(int id) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(FIND);
            ResultSet result = ps.executeQuery()
        ) {
            ps.setInt(1, id);
            result.next();
            return collectEntity(result);
        } catch (SQLException e) {
            throw new SQLException(String.format("Не получается считать подразделение с id: %s с базы данных", id), e);
        }
    }

    /**
     *  Собирает подразделение из результата запроса
     *  @param result результат запроса
     *  @return собранное подразделение
     */
    private static Department collectEntity(ResultSet result) throws SQLException {

        Department department = new Department();
        department.setId(result.getInt("id"));
        department.setName(result.getString("name"));
        department.setShortName(result.getString("short_name"));
        return department;
    }

    /**
     *  Запись подразделении в базу данных
     *  @param department объект класса Department для записи
     *  @return http-код состояния:<br>
     *      <u>200</u> - подразделение записано<br>
     *      <u>400</u> - подразделение не записано
     */
    public static Integer add(Department department) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(INSERT)
        ) {
            ps.setInt(1, department.getId());
            ps.setString(2, department.getName());
            ps.setString(3, department.getShortName());
            ps.setString(4, String.join(";", department.getPhones()));
            if (department.getOrganizationId() != null) {
                ps.setInt(5, department.getOrganizationId());
            } else {
                ps.setString(5, null);
            }
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Подразделение '%s' с id='%d' вставлено успешно", department.getName(), department.getId()));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.error(String.format("Подразделение не записано. %s", department.toString()));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("Не получается выполнить запрос на добавление подразделения в базу данных", e);
        }
    }

    /**
     *  Добавить начальника подразделения
     *  @param id идентификатор подразделения
     *  @param value идентификатор начальника подразделения
     *  @return http-код состояния:<br>
     *      <u>200</u> - id начальников и руководителей записаны успешно<br>
     *      <u>400</u> - id начальников и руководителей не записаны
     */
    public static Integer addHeadId(int id, Integer value) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(UPDATE_ID)
        ) {
            if (value != null){
                ps.setInt(1, value);
            } else {
                ps.setString(1, null);
            }
            ps.setInt(2, id);
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Head_id для подразделения с id: %d успешно задан", id));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.error(String.format("Что-то произошло. Head_id для подразделения с id: %d не записано.", id));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("Не удается задать head_id для подразделения с id: " + id, e);
        }
    }

    /**
     *  Удалить все подразделения из базы данных
     *  @return http-код состояния:<br>
     *      <u>200</u> - подразделения успешно удалены<br>
     *      <u>400</u> - подразделения не удалены
     */
    public static Integer remove() throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(DELETE_ALL)
        ) {
            if(ps.executeUpdate() > 0) {
                LOGGER.debug("Данные из таблицы department удалены");
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.debug("Таблица department уже пуста");
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("При удалении данных из таблицы department произошла ошибка", e);
        }
    }
}
