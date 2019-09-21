package de.saxms.language.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A LanguageKey.
 */
@Entity
@Table(name = "language_key")
public class LanguageKey implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_key")
    private String entryKey;

    public LanguageKey(String entryKey) {
        this.entryKey = entryKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntryKey() {
        return entryKey;
    }

    public LanguageKey entryKey(String entryKey) {
        this.entryKey = entryKey;
        return this;
    }

    public void setEntryKey(String entryKey) {
        this.entryKey = entryKey;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LanguageKey languageKey = (LanguageKey) o;
        if (languageKey.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languageKey.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LanguageKey{" +
            "id=" + getId() +
            ", entryKey='" + getEntryKey() + "'" +
            "}";
    }
}
