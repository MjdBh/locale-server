package de.saxms.language.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.saxms.language.exception.*;

import de.saxms.language.service.LanguageLocalAdminService;
import de.saxms.language.service.dto.LanguageEntryDTO;
import de.saxms.language.xml.util.XmlFileReader;
import de.saxms.language.xml.util.XmlFileWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@Qualifier("FILE")
public class LanguageLocalFileBaseAdminServiceImpl implements LanguageLocalAdminService {

    public static final String LANGUAGE_PREFIX = "language_";
    public static final String LANGUAGE_FILE_EXT = ".xml";
    public static final String JSON_FILE_EXT = ".json";

    private final Logger log = LoggerFactory.getLogger(LanguageLocalFileBaseAdminServiceImpl.class);
    @Value("${spring.application.localsPath}")
    private String localsPath;

    @Autowired
    private ObjectMapper mapper;

    private String getLocalFilePath(String local) {
        return localsPath + LANGUAGE_PREFIX + local.toUpperCase() + LANGUAGE_FILE_EXT;

    }

    private String getJsonFilePath(String local) {
        return localsPath + LANGUAGE_PREFIX + local.toUpperCase() + "_" + System.currentTimeMillis() + JSON_FILE_EXT;

    }



    @Override
    public boolean saveEntry(String local, LanguageEntryDTO entryDTO) {
        try {
            List<LanguageEntryDTO> allEntry = XmlFileReader.readEntries(getLocalFilePath(local));
            allEntry.add(entryDTO);
            XmlFileWriter.write(getLocalFilePath(local), allEntry);
            return true;
        } catch (Exception e) {
            log.error("error on save  entry to file", e);
        }

        return false;
    }

    @Override
    public boolean deleteEntry(String local, String entryKey) {

        try {
            List<LanguageEntryDTO> allEntry = XmlFileReader.readEntries(getLocalFilePath(local));

            XmlFileWriter.write(getLocalFilePath(local),
                allEntry.stream().
                    filter(e -> !e.getId().equalsIgnoreCase(entryKey)).
                    collect(Collectors.toList()));
            return true;
        } catch (Exception e) {
            log.error("error on delete entry to file", e);
        }

        return false;
    }

    @Override
    public boolean updateEntry(String local, String entryKey, String entryValue) {

        try {
            //todo duplicate entry updated
            List<LanguageEntryDTO> allEntry = XmlFileReader.readEntries(getLocalFilePath(local));
            XmlFileWriter.write(getLocalFilePath(local), allEntry.stream().parallel().map(en -> {
                if (en.getId().equalsIgnoreCase(entryKey))
                    en.setContent(entryValue);
                return en;
            }).collect(Collectors.toList()));

            return true;
        } catch (Exception e) {
            log.error("error on delete entry to file", e);
        }

        return false;
    }

    public void addLanguage(String name, String abbreviation) throws ExistLanguageException {
        File[] files = new File(localsPath).listFiles();
        long numberOfSameName = Arrays.stream(Objects.requireNonNull(files))
            .filter(File::isFile)
            .filter(f -> f.getName().toLowerCase().startsWith(LANGUAGE_PREFIX) &&
                f.getName().toLowerCase().endsWith(LANGUAGE_FILE_EXT) && f.getName().toUpperCase().contains(abbreviation.toUpperCase())).count();

        if (numberOfSameName > 0)
            throw new ExistLanguageException();

        try {
            XmlFileWriter.write(getLocalFilePath(abbreviation), new ArrayList<>());
        } catch (WriteFileException e) {
            log.error("error on create new Language", e);
        }


    }

    public List<String> getAvailableLanguages() {
        File[] files = new File(localsPath).listFiles();
        return Arrays.stream(Objects.requireNonNull(files))
            .filter(File::isFile)
            .filter(f -> f.getName().toLowerCase().startsWith(LANGUAGE_PREFIX) &&
                f.getName().toLowerCase().endsWith(LANGUAGE_FILE_EXT)).map(f -> f.getName().replace(LANGUAGE_PREFIX, "").replace(LANGUAGE_FILE_EXT, "")).collect(Collectors.toList());
    }

    public void writeLanguagesToJsonFile() {

        getAvailableLanguages().stream().parallel().forEachOrdered(s -> {
            List<LanguageEntryDTO> allEntry = null;

            try {
                allEntry = XmlFileReader.readEntries(getLocalFilePath(s));
            } catch (ReadFileException e) {
                log.error("error on read file of language= {}", s, e);
            }
            try {
                mapper.writeValue(new File(getJsonFilePath(s)), allEntry);
            } catch (IOException e) {
                log.error("Error on write json file for language {}", s, e);
            }

        });
    }

    //todo remove both of duplicate or single entry?
    public void removeDuplicates(String local) {
        List<LanguageEntryDTO> allEntry = null;
        try {
            allEntry = XmlFileReader.readEntries(getLocalFilePath(local));
        } catch (ReadFileException e) {
            log.error("error read language {} for remove duplicate", local);
        }
        try {
            XmlFileWriter.write(getLocalFilePath(local),
                allEntry.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(LanguageEntryDTO::getId))),
                        ArrayList::new)));
        } catch (WriteFileException e) {
            log.error("Error on write unique keys for language= {}", local, e);
        }

    }

    @Override
    public List<String> getConflictEntry(String local) {
        List<LanguageEntryDTO> allEntry;
        try {
            allEntry = XmlFileReader.readEntries(getLocalFilePath(local));
        } catch (ReadFileException e) {
            log.error("error read language {} for resolve  conflicts", local);
            return null;
        }

        return allEntry.stream().collect(Collectors.groupingBy(
            LanguageEntryDTO::getId, Collectors.counting()))
            .entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public Map<String, List<String>> unTranslatedEntry() {
        List<String> availableLocal = getAvailableLanguages();
        /*Map key= id of entry key=(Key =language local, key=available this key in language) */
        Map<String, Map<String, Boolean>> entryAvailability = new ConcurrentHashMap<>();
        availableLocal.stream().filter(Objects::nonNull).forEach(local -> {
            try {
                XmlFileReader.readEntries(getLocalFilePath(local)).stream().parallel().forEach(entryKey -> {
                    entryAvailability.computeIfAbsent(entryKey.getId(), localKey -> {
                        Map<String, Boolean> availableEntry = new HashMap<>();
                        availableEntry.put(local, true);
                        return availableEntry;
                    });
                    entryAvailability.computeIfPresent(entryKey.getId(), (localKey, availableKeys) -> {
                        availableKeys.put(local, true);
                        return availableKeys;
                    });
                });
            } catch (ReadFileException e) {
                log.error("Error on read local for resole untranslated language.");
            }
        });


        Map<String, List<String>> result = entryAvailability.entrySet().parallelStream().
            filter(e -> e.getValue().size() != availableLocal.size()).
            collect(Collectors.toMap(k -> k.getKey(), v -> new ArrayList<>(CollectionUtils.subtract(availableLocal, new ArrayList<>(v.getValue().keySet())))));
        return result;
    }
}
