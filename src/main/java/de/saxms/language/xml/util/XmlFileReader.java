package de.saxms.language.xml.util;

import de.saxms.language.exception.ReadFileException;
import de.saxms.language.service.dto.LanguageEntryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;

public class XmlFileReader {
    private final static Logger log = LoggerFactory.getLogger(XmlFileReader.class);

    public static List<LanguageEntryDTO> readEntries(String path) throws ReadFileException {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            LanguageHandler handler = new LanguageHandler();
            saxParser.parse(path, handler);
            return handler.getLanguageEntries();
        } catch (Exception e) {
           log.error("Error on read file ",e);
           throw new ReadFileException();
        }

    }
}
