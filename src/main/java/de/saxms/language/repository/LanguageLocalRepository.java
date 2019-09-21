package de.saxms.language.repository;

import de.saxms.language.domain.LanguageLocal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LanguageLocal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageLocalRepository extends JpaRepository<LanguageLocal, Long> {

    LanguageLocal getByAbbreviation(String abbreviation);

}
