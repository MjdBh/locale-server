package de.saxms.language.repository;

import de.saxms.language.domain.LanguageKey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the LanguageKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageKeyRepository extends JpaRepository<LanguageKey, Long> {

    Optional<LanguageKey> getByEntryKey(String entryKey);

}
