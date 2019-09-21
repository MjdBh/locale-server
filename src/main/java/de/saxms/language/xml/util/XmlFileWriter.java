package de.saxms.language.xml.util;

import de.saxms.language.exception.WriteFileException;
import de.saxms.language.service.dto.LanguageEntryDTO;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.List;

import static de.saxms.language.xml.util.XmlObjectFields.*;

public class XmlFileWriter {

    public static void write(String path, List<LanguageEntryDTO> languageEntryList) throws WriteFileException {
        try {

            OutputStream outputStream = new FileOutputStream(new File(path));

            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                new OutputStreamWriter(outputStream, "utf-8"));

            out.writeStartDocument("UTF-8", "");

            out.writeCharacters(System.lineSeparator());
            out.writeStartElement(langugeXmlFile);
            out.writeCharacters(System.lineSeparator());
            out.writeCharacters("\t");
            out.writeStartElement(languageField);
            out.writeCharacters(System.lineSeparator());
            for (LanguageEntryDTO languageEntryDTO : languageEntryList) {

                out.writeCharacters("\t\t");
                out.writeStartElement(languageField);
                out.writeCharacters(System.lineSeparator());
                out.writeCharacters("\t\t   ");
                out.writeStartElement(contentField);
                out.writeCharacters(languageEntryDTO.getContent());
                out.writeEndElement();

                out.writeCharacters(System.lineSeparator());
                out.writeCharacters("\t\t   ");
                out.writeStartElement(idField);
                out.writeCharacters(languageEntryDTO.getId());
                out.writeEndElement();

                if (!StringUtils.isEmpty(languageEntryDTO.getLastUpdate())) {
                    out.writeCharacters(System.lineSeparator());
                    out.writeCharacters("\t\t   ");
                    out.writeStartElement(lastUpdateField);
                    out.writeCharacters(languageEntryDTO.getLastUpdate());
                    out.writeEndElement();
                }
                out.writeCharacters(System.lineSeparator());
                out.writeCharacters("\t\t ");
                out.writeEndElement();
                out.writeCharacters(System.lineSeparator());


            }
            out.writeCharacters("\t");
            out.writeCharacters(System.lineSeparator());
            out.writeEndElement();
            out.writeCharacters(System.lineSeparator());
            out.writeEndElement();
            out.writeCharacters(System.lineSeparator());
            out.writeEndDocument();
            out.close();
        }catch ( XMLStreamException| FileNotFoundException| UnsupportedEncodingException e){
            throw new WriteFileException();
        }
    }
}
