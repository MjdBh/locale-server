package de.saxms.language.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A LanguageLocal.
 */
@Entity
@Table(name = "language_local")
public class LanguageLocal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "abbreviation")
    private String abbreviation;

    @OneToMany(mappedBy = "languageLocal")
    private Set<LanguageEntry> languageEntries = new HashSet<>();
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LanguageLocal name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public LanguageLocal abbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Set<LanguageEntry> getLanguageEntries() {
        return languageEntries;
    }

    public LanguageLocal languageEntries(Set<LanguageEntry> languageEntries) {
        this.languageEntries = languageEntries;
        return this;
    }

    public LanguageLocal addLanguageEntry(LanguageEntry languageEntry) {
        this.languageEntries.add(languageEntry);
        languageEntry.setLanguageLocal(this);
        return this;
    }

    public LanguageLocal removeLanguageEntry(LanguageEntry languageEntry) {
        this.languageEntries.remove(languageEntry);
        languageEntry.setLanguageLocal(null);
        return this;
    }

    public void setLanguageEntries(Set<LanguageEntry> languageEntries) {
        this.languageEntries = languageEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LanguageLocal languageLocal = (LanguageLocal) o;
        if (languageLocal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languageLocal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LanguageLocal{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abbreviation='" + getAbbreviation() + "'" +
            "}";
    }
}
