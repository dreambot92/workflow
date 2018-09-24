package ru.workflow.rest;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.DocumentCollections;
import ru.workflow.ProjectProperties;
import ru.workflow.StaffCollections;
import ru.workflow.database.DatabaseService;
import ru.workflow.database.PersonDB;
import ru.workflow.documents.Document;
import ru.workflow.exception.DocumentExistsException;
import ru.workflow.staff.Person;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *  Рест запросы для получения информации по работникам
 *  @author IShabalin
 */
@Path("/employees")
public class EmployeeRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRest.class);
    private static Gson gson = new Gson();

    static {
        try {
            // загружаем данные из конфиг-файла
            ProjectProperties.load();
            // сохраняем в базу данных элементы организационной структуры
            DatabaseService.saveInDatabase();
            // создаем новые документы
            DocumentCollections.createNewDocuments();
        } catch (IOException e) {
            LOGGER.error("Не получилось считать данные с XML файлов", e);
        } catch (SQLException e) {
            LOGGER.error("Не получается сохранить объекты в базу данных", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | InterruptedException e) {
            LOGGER.error("Не получилось создать новые документы", e);
        } catch (DocumentExistsException e) {
            LOGGER.error("Произошла ошибка при попытке задать регистрационный номер. Такой номер уже есть в базе.", e);
        }
    }

    /**
     *  Достать список всех работников.
     *  @return список поручений, запрограммированы следующие http-коды:<br>
     *    <u>200</u> - работники найдены<br>
     *    <u>204</u> - сервер успешно отработал запрос, но вернул null<br>
     *    <u>503</u> - не получилось считать данные из базы данных
     */
    @GET
    @Path("/")
    public Response getAllEmployees() {

        // достаем колекцию с XML файла, указанного в Person.PATHNAME
        List<Person> list;
        try {
            list = PersonDB.findAll();
        } catch (SQLException e) {
            LOGGER.error("Не получилось считать данные из базы данных", e);
            return Response.status(HttpServletResponse.SC_SERVICE_UNAVAILABLE).build();
        }
        // если пусто, то возвращаем соответствующий ответ клиенту
        if (list.isEmpty()) {
            return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
        }
        // возвращаем ответ клиенту с данными, преобразованными в json и с кодировкой urf-8
        return Response.status(HttpServletResponse.SC_OK)
                .entity(gson.toJson(list))
                .build();
    }

    /**
     *  Достать список всех документов по id работника.
     *  @return список документов, запрограммированы следующие http-коды:<br>
     *    <u>200</u> - документы найдены<br>
     *    <u>204</u> - сервер успешно отработал запрос, но вернул null<br>
     *    <u>400</u> - ошибка конвертирования id в число
     */
    @GET
    @Path("/{id}")
    public Response getDocumentsByEmployeeId(@PathParam("id") String idString) {

        // делаем проверку id на возможность конвертирования
        Integer id;
        try {
            id = Integer.valueOf(idString);
        } catch (NumberFormatException e) {
            LOGGER.warn(String.format("Не получилось взять параметр id: %s", idString), e);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
        }
        // достаем множество документов с коллекции DocumentCollection.listDocument
        Set<Document> set = DocumentCollections.getDocumentByEmployeeId(id);
        // если пусто, то возвращаем соответствующий ответ клиенту
        if (set.isEmpty()) {
            return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
        }
        // создаем объект класса GSON и задаем ему параметры сериализаторов для документов
        // возвращаем ответ клиенту с данными, преобразованными в json и с кодировкой urf-8
        return Response.status(HttpServletResponse.SC_OK)
                .entity(gson.toJson(set))
                .build();
    }

    /**
     *  Создать нового работника
     *  @param newPerson работник, которого требуется создать
     *  @return http-код:<br>
     *      <u>200</u> - данные успешно изменены<br>
     *      <u>503</u> - произошла ошибка на уровне базы данных
     */
    @PUT
    @Path("/")
    public Response createEmployee(Person newPerson) {

        try {
            return Response.status(PersonDB.add(newPerson)).build();
        } catch (SQLException e) {
            LOGGER.error("Не получилось записать данные в базу данных", e);
            return Response.status(HttpServletResponse.SC_SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     *  Изменить данные по работнику
     *  @param idString идентификатор работника
     *  @param newPerson измененный работник
     *  @return http-код:<br>
     *      <u>200</u> - данные успешно изменены<br>
     *      <u>400</u> - работник не перезаписан<br>
     *      <u>503</u> - произошла ошибка на уровне базы данных
     */
    @PUT
    @Path("/{id}")
    public Response editEmployeeById(Person newPerson, @PathParam("id") String idString) {

        Integer id;
        try {
            id = Integer.valueOf(idString);
        } catch (NumberFormatException e) {
            LOGGER.warn(String.format("Не получилось взять параметр id: %s", idString), e);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
        }
        LOGGER.info(String.format("Попытка изменения рабочего с id %d. Рабочий: %s", id, newPerson.toString()));
        try {
            int httpCode = PersonDB.edit(newPerson);
            if (httpCode == HttpServletResponse.SC_OK) {
                for (Person p : StaffCollections.getPersonsList()) {
                    if (p.getId() == newPerson.getId()) {
                        p.setSurname(newPerson.getSurname());
                        p.setFirstName(newPerson.getFirstName());
                        p.setPatronymic(newPerson.getPatronymic());
                        p.setPost(newPerson.getPost());
                        break;
                    }
                }
            }
            return Response.status(httpCode).build();
        } catch (SQLException e) {
            LOGGER.error("Не получилось записать данные работника в базу", e);
            return Response.status(HttpServletResponse.SC_SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     *  Удалить работника по его идентификатору
     *  @param idString идентификатор работника
     *  @return http-код:<br>
     *      <u>200</u> - работник успешно удален<br>
     *      <u>400</u> - работник не перезаписан, либо id содержит некорректные символы или null<br>
     *      <u>503</u> - произошла ошибка на уровне базы данных
     */
    @DELETE
    @Path("/{id}")
    public Response removeEmployee(@PathParam("id") String idString) {

        try {
            return Response.status(PersonDB.removeById(Integer.valueOf(idString))).build();
        } catch (NumberFormatException e) {
            LOGGER.warn(String.format("Не получилось взять параметр id: %s", idString), e);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
        } catch (SQLException e) {
            LOGGER.error("Не получилось записать данные в базу данных", e);
            return Response.status(HttpServletResponse.SC_SERVICE_UNAVAILABLE).build();
        }
    }
}
