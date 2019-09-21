package de.saxms.language.service;

import de.saxms.language.exception.ExistLanguageException;
import de.saxms.language.exception.ReadFileException;
import de.saxms.language.exception.WriteFileException;
import de.saxms.language.service.dto.LanguageEntryDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LanguageLocalAdminService {
    /*
      Add entry to a language
     */
    boolean saveEntry(String local, LanguageEntryDTO entryDTO);

    /*
    Delete a entry from a language

     */
    boolean deleteEntry(String local, String entryKey);

    /*
     Edit entry for a language

     */
    boolean updateEntry(String local, String entryKey, String entryValue);

    /*
     Add a new language
     */

    void addLanguage(String name, String abbreviation) throws ExistLanguageException;

    /*
    Get List of availabel languages
     */
    List<String> getAvailableLanguages();

    /*
    Write language files to Json file
    */
    void writeLanguagesToJsonFile();

    /*
    Remove duplicate entry from local
     */
    void removeDuplicates(String local);

    /*
    Get Conflict entry in local
    */
    List<String> getConflictEntry(String local);

    /*
     Get list of entry that not translated in any local
    */
    Map<String, List<String>> unTranslatedEntry();

}
