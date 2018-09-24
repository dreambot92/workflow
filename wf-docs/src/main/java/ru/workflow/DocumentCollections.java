package ru.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.workflow.documents.Document;
import ru.workflow.enumeration.DocumentType;
import ru.workflow.exception.DocumentExistsException;
import ru.workflow.staff.Person;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.util.TreeMap;

/**
 *  Статические коллекции документов
 *  @author IShabalin
 */
public final class DocumentCollections {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentCollections.class);

    /** Количество создаваемых документов */
    private static final Integer NUMBER_OF_DOCUMENTS = 40;

    private DocumentCollections() {
    }

    /** Отсортированный связанный список множества документов, закрепленных за конкретными авторами */
    private static Map<Person, Set<Document>> listDocuments = new TreeMap<>();
    /** Список занятых регистрационных номеров */
    private static List<Integer> registrationNumbers = new ArrayList<>();

    /** Создаем новые документы */
    public static void createNewDocuments() throws IllegalAccessException, DocumentExistsException, InstantiationException, InvocationTargetException, InterruptedException {

        Random r = new Random();
        // создаем документы
        for (int i = 0; i < NUMBER_OF_DOCUMENTS; i++) {
            Factory.createDocument(DocumentType.values()[r.nextInt(DocumentType.values().length)]);
        }
        LOGGER.info("Были созданы рандомные документы");
    }

    /**
     *  Получаем множество документов конкретного работника по id
     *  @return множество документов или пустое множество, если документов у этого работника нету
     */
    public static Set<Document> getDocumentByEmployeeId(int id) {

        Map<Person, Set<Document>> map = DocumentCollections.getListDocuments();
        for (Map.Entry<Person, Set<Document>> entry : map.entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getValue();
            }
        }
        LOGGER.debug(String.format("По указанному id: %d никаких документов найдено не было", id));
        return Collections.emptySet();
    }

    public static Map<Person, Set<Document>> getListDocuments() {
        return listDocuments;
    }

    public static List<Integer> getRegistrationNumbers() {
        return registrationNumbers;
    }
}
