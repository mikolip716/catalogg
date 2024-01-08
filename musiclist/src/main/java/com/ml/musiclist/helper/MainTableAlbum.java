package com.ml.musiclist.helper;

import com.ml.musiclist.entity.*;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
public class MainTableAlbum {
    @Getter
    private final List<String> artists;
    private final SimpleStringProperty title;
    @Getter
    private final List<String> genres;
    @Getter
    private final Duration length;
    private final SimpleStringProperty relDate;
    private final SimpleStringProperty addDate;
    private final SimpleStringProperty format;
    private final SimpleStringProperty type;
    @Getter
    private final List<String> langs;
    @Getter
    private final List<String> tags;
    @Getter
    private final Long dbId;

    public MainTableAlbum(List<Artist> artists, String title, List<Genre> genres, Duration length,
                          LocalDate relDate, LocalDate addDate, String format, List<Language> langs,
                          List<Tags> tags, String type, Long dbId) {

        List<String> tempArtists = new ArrayList<String>();
        List<String> tempGenres = new ArrayList<String>();
        List<String> tempLangs = new ArrayList<String>();
        List<String> tempTags = new ArrayList<>();

        for (Artist artist : artists){
            tempArtists.add(artist.getName());
        }
        for (Genre genre : genres){
            tempGenres.add(genre.getName());
        }
        for (Language lang : langs){
            tempLangs.add(lang.getLanguage());
        }
        for (Tags tag : tags){
            tempTags.add(tag.getTag());
        }

        this.artists = tempArtists;
        this.title = new SimpleStringProperty(title);
        this.genres = tempGenres;
        this.length = length;
        this.relDate = relDate != null ? new SimpleStringProperty(relDate.toString()) : new SimpleStringProperty("");
        this.addDate = addDate != null ? new SimpleStringProperty(addDate.toString()) : new SimpleStringProperty("");
        this.format = new SimpleStringProperty(format);
        this.langs = tempLangs;
        this.tags = tempTags;
        this.type = new SimpleStringProperty(type);
        this.dbId = dbId;
    }

    public String getTitle() {
        return title.get();
    }
    public SimpleStringProperty titleProperty() {
        return title;
    }
    public String getRelDate() {
        return relDate.get();
    }
    public SimpleStringProperty relDateProperty() {
        return relDate;
    }
    public String getAddDate() {
        return addDate.get();
    }
    public SimpleStringProperty addDateProperty() {
        return addDate;
    }
    public String getFormat() {
        return format.get();
    }
    public SimpleStringProperty formatProperty() {
        return format;
    }
    public String getType() {
        return type.get();
    }
    public SimpleStringProperty typeProperty() {
        return type;
    }
}
