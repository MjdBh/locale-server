package de.saxms.language.repository;

import de.saxms.language.domain.LanguageEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the LanguageEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageEntryRepository extends JpaRepository<LanguageEntry, Long> {

    @Modifying
    void deleteByLanguageLocal_AbbreviationAndLanguageKey_EntryKey(String abbreviation,String entryKey);

    @Query(value = "select  le.language_key_id from langauge_entry le left join language_local lo on le.language_key=lo.id where lo.abbreviation=:local group by le.language_key_id having count(*)>1  ",nativeQuery =true)
    List<Long> getDuplicateKey(String Local);

    default void deleteEntry(String abbreviation, String entryKey){
        deleteByLanguageLocal_AbbreviationAndLanguageKey_EntryKey(abbreviation,entryKey);
    }

    @Modifying
    default void deleteEntry(String abbreviation, Long entryKeyId){
        deleteByLanguageLocal_AbbreviationAndLanguageKey_Id(abbreviation,entryKeyId);
    }

    @Modifying
    void deleteByLanguageLocal_AbbreviationAndLanguageKey_Id(String abbreviation,Long entryKeyId);




}
