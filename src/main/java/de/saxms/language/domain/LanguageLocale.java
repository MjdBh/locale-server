package de.saxms.language.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A LanguageLocale.
 */
@Entity
@Table(name = "language_local")
public class LanguageLocale implements Serializable {

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

    public LanguageLocale name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public LanguageLocale abbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
        return this;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Set<LanguageEntry> getLanguageEntries() {
        return languageEntries;
    }

    public LanguageLocale languageEntries(Set<LanguageEntry> languageEntries) {
        this.languageEntries = languageEntries;
        return this;
    }

    public LanguageLocale addLanguageEntry(LanguageEntry languageEntry) {
        this.languageEntries.add(languageEntry);
        languageEntry.setLanguageLocale(this);
        return this;
    }

    public LanguageLocale removeLanguageEntry(LanguageEntry languageEntry) {
        this.languageEntries.remove(languageEntry);
        languageEntry.setLanguageLocale(null);
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
        LanguageLocale languageLocale = (LanguageLocale) o;
        if (languageLocale.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languageLocale.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LanguageLocale{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abbreviation='" + getAbbreviation() + "'" +
            "}";
    }
}
