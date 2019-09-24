package de.saxms.language.repository;

import de.saxms.language.domain.LanguageLocale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LanguageLocale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageLocalRepository extends JpaRepository<LanguageLocale, Long> {

    LanguageLocale getByAbbreviation(String abbreviation);

}
