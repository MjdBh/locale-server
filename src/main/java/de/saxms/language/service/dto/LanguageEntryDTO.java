package de.saxms.language.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

public class LanguageEntryDTO implements Serializable {
   private static DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
       .withLocale( Locale.GERMANY )
       .withZone( ZoneId.systemDefault() );

    private String content;
    private String id;
    private String lastUpdate;

    public LanguageEntryDTO(String content, String id) {
        this.content = content;
        this.id = id;
    }

    public LanguageEntryDTO(String content, String id, String lastUpdate) {
        this.content = content;
        this.id = id;
        this.lastUpdate = lastUpdate;
    }
    public LanguageEntryDTO(String content, String id, Instant lastUpdate) {
        this.content = content;
        this.id = id;
        this.lastUpdate = format.format(lastUpdate);
    }

    public LanguageEntryDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = format.format(lastUpdate) ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageEntryDTO that = (LanguageEntryDTO) o;
        return Objects.equals(content, that.content) &&
            Objects.equals(id, that.id) &&
            Objects.equals(lastUpdate, that.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, id, lastUpdate);
    }
}
