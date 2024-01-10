package com.ml.catalogg.helper;

import com.ml.catalogg.enums.SortModeEnum;
import com.ml.catalogg.enums.SortOrderEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Properties;

public class PropertiesManager {
    private final String rootPath = System.getProperty("user.dir");
    private final String appConfigPath = rootPath + "userPreferences.properties";
    @Setter
    @Getter
    private boolean startFullscreen;
    private final String startFullscreenPropName = "fullscreen";
    @Setter
    @Getter
    private SortModeEnum sortMode;
    private final String sortModePropName = "defaultSort";
    @Setter
    @Getter
    private SortOrderEnum sortOrder;
    private final String sortOrderPropName = "defaultSortOrder";
    public void saveProperties () {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
            appProps.setProperty(startFullscreenPropName, String.valueOf(startFullscreen));
            appProps.setProperty(sortModePropName, sortMode.toString());
            appProps.setProperty(sortOrderPropName, sortOrder.toString());
            appProps.store(new FileWriter(appConfigPath), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadProperties() {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
            startFullscreen = Boolean.parseBoolean(appProps.getProperty(startFullscreenPropName));
            sortMode = SortModeEnum.valueOf(appProps.getProperty(sortModePropName));
            sortOrder = SortOrderEnum.valueOf(appProps.getProperty(sortOrderPropName));
        } catch (IOException e) {
            createWithDefaultValues();
        }
    }
    private void createWithDefaultValues() {
        Properties appProps = new Properties();
        appProps.setProperty(startFullscreenPropName, String.valueOf(false));
        appProps.setProperty(sortModePropName, String.valueOf(SortModeEnum.DEFAULT));
        appProps.setProperty(sortOrderPropName, String.valueOf(SortOrderEnum.ASC));
        try {
            appProps.store(new FileWriter(appConfigPath), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
