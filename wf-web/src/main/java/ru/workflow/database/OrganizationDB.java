package ru.workflow.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.staff.Organization;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Класс для работы с базой данных с объектами класса Organization
 *  @author IShabalin
 */
public class OrganizationDB {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationDB.class);

    private static final String FIND_ALL = "select * from organization";
    private static final String FIND = "select * from organization where id = ?";
    private static final String INSERT = "insert into organization(id, name, short_name, phones) values(?,?,?,?)";
    private static final String UPDATE_ID = "update organization set head_id = ? where id = ?";
    private static final String DELETE_ALL = "delete from organization";

    private OrganizationDB() { }

    /**
     *  Достать список организаций из базы
     *  @return этот список
     */
    public static List<Organization> findAll() throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(FIND_ALL);
            ResultSet result = ps.executeQuery()
        ) {
            List<Organization> list = new ArrayList<>();
            while (result.next()) {
                list.add(collectEntity(result));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Не получается выполнить запрос на чтение организациии из базы данных", e);
        }
    }

    /**
     *  Достать конкретную организацию из базы по id
     *  @value id идентификатор организацию
     *  @return та самая организация
     */
    public static Organization find(int id) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(FIND);
            ResultSet result = ps.executeQuery()
        ) {
            ps.setInt(1, id);
            result.next();
            return collectEntity(result);
        } catch (SQLException e) {
            throw new SQLException(String.format("Не получается считать организацию с id: %s с базы данных", id), e);
        }
    }

    /**
     *  Собирает организацию из результата запроса
     *  @param result результат запроса
     *  @return собранная организация
     */
    private static Organization collectEntity(ResultSet result) throws SQLException {

        Organization organization = new Organization();
        organization.setId(result.getInt("id"));
        organization.setName(result.getString("name"));
        organization.setShortName(result.getString("short_name"));
        return organization;
    }

    /**
     *  Запись организации в базу данных
     *  @param organization объект класса Organization для записи
     *  @return http-код состояния:<br>
     *      <u>200</u> - организация записана<br>
     *      <u>400</u> - организация не записана
     */
    public static Integer add(Organization organization) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(INSERT)
        ) {
            ps.setInt(1, organization.getId());
            ps.setString(2, organization.getName());
            ps.setString(3, organization.getShortName());
            ps.setString(4, String.join(";", organization.getPhones()));
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Организация '%s' с id='%d' вставлена успешна", organization.getName(), organization.getId()));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.error(String.format("Что-то произошло. Организация не записана.", organization.toString()));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("Не получается выполнить запрос на добавление организации в базу данных", e);
        }
    }

    /**
     *  Добавить руководителя организации
     *  @param id идентификатор организации
     *  @param value идентификатор руководителя
     *  @return http-код состояния:<br>
     *      <u>200</u> - id начальников и руководителей записаны успешно<br>
     *      <u>400</u> - id начальников и руководителей не записаны
     */
    public static Integer addHeadId(int id, Integer value) throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(UPDATE_ID)
        ) {
            if (value != null) {
                ps.setInt(1, value);
            } else {
                ps.setString(1, null);
            }
            ps.setInt(2, id);
            if(ps.executeUpdate() > 0) {
                LOGGER.info(String.format("Head_id для организации с id: %d успешно задан", id));
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.error(String.format("Что-то произошло. Head_id для организации с id: %d не записано.", id));
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("Не удается задать head_id для организации с id: " + id, e);
        }
    }

    /**
     *  Удалить все организации из базы данных
     *  @return http-код состояния:<br>
     *      <u>200</u> - организации успешно удалены<br>
     *      <u>400</u> - организации не удалены
     */
    public static Integer remove() throws SQLException {

        try(
            Connection conn = DatabaseService.getDs().getConnection(DatabaseService.getUSER(), DatabaseService.getPASS());
            PreparedStatement ps = conn.prepareStatement(DELETE_ALL)
        ) {
            if(ps.executeUpdate() > 0) {
                LOGGER.debug("Данные из таблицы organization удалены");
                return HttpServletResponse.SC_OK;
            } else {
                LOGGER.debug("Таблица organization уже пуста");
                return HttpServletResponse.SC_BAD_REQUEST;
            }
        } catch (SQLException e) {
            throw new SQLException("При удалении данных из таблицы organization произошла ошибка", e);
        }
    }
}
