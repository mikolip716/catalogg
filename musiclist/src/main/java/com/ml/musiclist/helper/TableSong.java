package com.ml.musiclist.helper;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
public class TableSong {
    private final SimpleStringProperty title;
    @Getter
    private final Duration length;
    private final SimpleStringProperty number;

    public TableSong (String title, Duration length, String number) {
        this.title = new SimpleStringProperty(title);
        this.length = length;
        this.number = new SimpleStringProperty(number);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getNumber() {
        return number.get();
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }
}
