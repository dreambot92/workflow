package ru.workflow.interfaces;

import ru.workflow.Data;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 *  Функциональный интерфейс для конвертации в XML.
 *  @author IShabalin
 */
@FunctionalInterface
public interface ConvertFromXML {

    /**
     *  Метод конвертирования в XML из файла
     *  Data.class является по сути root-корнем XML файла и содержит в себе коллекции из типов документов
     *  @param inputStream инпут-стрим файла, который нужно конвернуть
     *  @return десериализованные объекты
     */
    static <T> T convert(InputStream inputStream) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        Unmarshaller un = jaxbContext.createUnmarshaller();
        return (T) un.unmarshal(inputStream);
    }

    Data convertFromXml() throws JAXBException;
}