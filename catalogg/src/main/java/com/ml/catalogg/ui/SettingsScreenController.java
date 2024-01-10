package com.ml.catalogg.ui;

import com.ml.catalogg.entity.Album;
import com.ml.catalogg.enums.SortModeEnum;
import com.ml.catalogg.enums.SortOrderEnum;
import com.ml.catalogg.helper.PropertiesManager;
import com.ml.catalogg.service.AlbumService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@Component
public class SettingsScreenController implements Initializable {
    @FXML
    private CheckBox checkboxFullscreen;
    @FXML
    private Button openCoversFolderButton;
    @FXML
    private Button openDataFolderButton;
    @FXML
    private Button reloadCoversButton;
    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<SortModeEnum> sortPicker;
    @FXML
    private ChoiceBox<SortOrderEnum> sortModePicker;
    @FXML
    private Label versionLabel;
    private PropertiesManager propertiesManager;
    @Autowired
    private AlbumService albumService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reloadCoversButton.setOnAction(this::handleReloadCoversButton);
        saveButton.setOnAction(this::handleSaveButton);
        propertiesManager = new PropertiesManager();
        propertiesManager.loadProperties();
        sortPicker.setItems(FXCollections.observableArrayList(SortModeEnum.values()));
        sortPicker.setValue(propertiesManager.getSortMode());
        sortModePicker.setItems(FXCollections.observableArrayList(SortOrderEnum.values()));
        sortModePicker.setValue(propertiesManager.getSortOrder());
        checkboxFullscreen.setSelected(propertiesManager.isStartFullscreen());
        openCoversFolderButton.setOnAction(this::handleOpenCoversFolderButton);
        openDataFolderButton.setOnAction(this::handleOpenDataFolderButton);
    }
    public void handleSaveButton(ActionEvent event) {
        propertiesManager.setSortMode(sortPicker.getValue());
        propertiesManager.setStartFullscreen(checkboxFullscreen.isSelected());
        propertiesManager.setSortOrder(sortModePicker.getValue());
        propertiesManager.saveProperties();
    }
    public void handleReloadCoversButton(ActionEvent event) {
        List<Album> allAlbums = albumService.selectAll();
        for (Album album : allAlbums) {
            String oldPath = album.getCoverPath();
            String newPathStart = System.getProperty("user.dir") + "\\covers\\";
            List<String> oldPathSplit = List.of(oldPath.split(Pattern.quote("/")));
            String newPath = newPathStart + oldPathSplit.getLast();
            File newCover = new File(newPath);
            newPath = newCover.toURI().toString();
            album.setCoverPath(newPath);
            albumService.saveAlbum(album);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Done");
        alert.setContentText("Fixed file paths for imported album covers.");
        alert.show();
    }
    public void handleOpenCoversFolderButton(ActionEvent event) {
        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\covers"));
        } catch (IOException ignored) {}
    }
    public void handleOpenDataFolderButton(ActionEvent event) {
        try {
            Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\data"));
        } catch (IOException ignored) {}
    }
}
