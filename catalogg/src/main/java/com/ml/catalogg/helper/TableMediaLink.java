package com.ml.catalogg.helper;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Hyperlink;
import lombok.Setter;

@Setter
public class TableMediaLink {
    private final SimpleStringProperty siteName;
    private final Hyperlink url;

    public TableMediaLink(String siteName, String url) {
        this.siteName = new SimpleStringProperty(siteName);
        this.url = new Hyperlink(url);
    }

    public String getSiteName() {
        return siteName.get();
    }

    public SimpleStringProperty siteNameProperty() {
        return siteName;
    }

    public Hyperlink getUrl() {
        return url;
    }
}
