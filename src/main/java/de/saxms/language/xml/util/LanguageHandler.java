package de.saxms.language.xml.util;


import de.saxms.language.service.dto.LanguageEntryDTO;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Stack;

import static de.saxms.language.xml.util.XmlObjectFields.*;

public class LanguageHandler extends DefaultHandler {
    private ArrayList<LanguageEntryDTO> entryList = new ArrayList<>();
    private Stack<String> elementStack = new Stack<>();
    private Stack<LanguageEntryDTO> objectStack = new Stack<>();


    public void startDocument() {

    }

    public void endDocument() {

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        this.elementStack.push(qName);


        if (languageField.equalsIgnoreCase(qName)) {

            LanguageEntryDTO entry = new LanguageEntryDTO();
            this.objectStack.push(entry);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        this.elementStack.pop();


        if (languageField.equalsIgnoreCase(qName)) {
            LanguageEntryDTO entryDTO = this.objectStack.pop();
            if (entryDTO.getId() != null)
                this.entryList.add(entryDTO);
        }
    }


    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();

        if (StringUtils.isEmpty(value)) {
            return;
        }
        if (contentField.equalsIgnoreCase(currentElement())) {
            LanguageEntryDTO entry = this.objectStack.peek();
            entry.setContent(value);
        } else if (idField.equalsIgnoreCase(currentElement())) {
            LanguageEntryDTO entry = this.objectStack.peek();
            entry.setId(value);
        } else if (lastUpdateField.equalsIgnoreCase(currentElement())) {
            LanguageEntryDTO entry = this.objectStack.peek();
            entry.setLastUpdate(value);
        }
    }


    private String currentElement() {
        return this.elementStack.peek();
    }

    public ArrayList<LanguageEntryDTO> getLanguageEntries() {
        return entryList;
    }


}
