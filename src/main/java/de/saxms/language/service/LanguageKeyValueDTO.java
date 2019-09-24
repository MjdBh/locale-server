package de.saxms.language.service;

import de.saxms.language.domain.LanguageLocale;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class LanguageKeyValueDTO implements Serializable {

    private Long id;
    private String entryValue;
    private Instant lastUpdate;
    private LanguageLocale languageLocale;
    private String entryKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryValue() {
        return entryValue;
    }

    public void setEntryValue(String entryValue) {
        this.entryValue = entryValue;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LanguageLocale getLanguageLocale() {
        return languageLocale;
    }

    public void setLanguageLocale(LanguageLocale languageLocale) {
        this.languageLocale = languageLocale;
    }

    public String getEntryKey() {
        return entryKey;
    }

    public void setEntryKey(String entryKey) {
        this.entryKey = entryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageKeyValueDTO that = (LanguageKeyValueDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(entryValue, that.entryValue) &&
            Objects.equals(lastUpdate, that.lastUpdate) &&
            Objects.equals(languageLocale, that.languageLocale) &&
            Objects.equals(entryKey, that.entryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entryValue, lastUpdate, languageLocale, entryKey);
    }
}
