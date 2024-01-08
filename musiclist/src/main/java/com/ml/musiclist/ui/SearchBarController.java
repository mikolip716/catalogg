package com.ml.musiclist.ui;

import com.ml.musiclist.entity.Album;
import com.ml.musiclist.enums.*;
import com.ml.musiclist.helper.MainScreenHolder;
import com.ml.musiclist.helper.TableDataHolder;
import com.ml.musiclist.service.AlbumService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

@Component
public class SearchBarController implements Initializable {
    @FXML
    private ChoiceBox<DateModeEnum> addDateModePicker;
    @FXML
    private DatePicker addDatePicker;
    @FXML
    private RadioButton addDateRadioButton;
    @FXML
    private Button applyFiltersButton;
    @FXML
    private TextField artistField;
    @FXML
    private RadioButton artistRadioButton;
    @FXML
    private Button chooseRandomButton;
    @FXML
    private ChoiceBox<FormatEnum> formatPicker;
    @FXML
    private RadioButton formatRadioButton;
    @FXML
    private TextField genreField;
    @FXML
    private RadioButton genreRadioButton;
    @FXML
    private TextField languageField;
    @FXML
    private RadioButton languageRadioButton;
    @FXML
    private TextField lengthField;
    @FXML
    private ChoiceBox<LengthModeEnum> lengthModePicker;
    @FXML
    private RadioButton lengthRadioButton;
    @FXML
    private RadioButton noFilterRadioButton;
    @FXML
    private CheckBox ownedCheckBox;
    @FXML
    private RadioButton ownedRadioButton;
    @FXML
    private ChoiceBox<DateModeEnum> relDateModePicker;
    @FXML
    private DatePicker relDatePicker;
    @FXML
    private RadioButton relDateRadioButton;
    @FXML
    private HBox root;
    @FXML
    private TextField tagField;
    @FXML
    private RadioButton tagRadioButton;
    @FXML
    private TextField titleField;
    @FXML
    private RadioButton titleRadioButton;
    @FXML
    private ChoiceBox<TypeEnum> typePicker;
    @FXML
    private RadioButton typeRadioButton;
    private ToggleGroup buttonGroup;
    @Autowired
    private AlbumService albumService;
    private MainTableController tableController;
    private MainScreenController mainController;
    private boolean listenedTo;
    private boolean filterApplied;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //choicebox initialization
        addDateModePicker.setItems(FXCollections.observableArrayList(DateModeEnum.values()));
        addDateModePicker.setValue(DateModeEnum.BEFORE);
        relDateModePicker.setItems(FXCollections.observableArrayList(DateModeEnum.values()));
        relDateModePicker.setValue(DateModeEnum.BEFORE);
        lengthModePicker.setItems(FXCollections.observableArrayList(LengthModeEnum.values()));
        lengthModePicker.setValue(LengthModeEnum.UNDER);
        formatPicker.setItems(FXCollections.observableArrayList(FormatEnum.values()));
        formatPicker.setValue(FormatEnum.DIGITAL_MEDIA);
        typePicker.setItems(FXCollections.observableArrayList(TypeEnum.values()));
        typePicker.setValue(TypeEnum.ALBUM);
        //toggle buttons initialization
        buttonGroup = new ToggleGroup();
        addDateRadioButton.setToggleGroup(buttonGroup);
        artistRadioButton.setToggleGroup(buttonGroup);
        formatRadioButton.setToggleGroup(buttonGroup);
        genreRadioButton.setToggleGroup(buttonGroup);
        lengthRadioButton.setToggleGroup(buttonGroup);
        languageRadioButton.setToggleGroup(buttonGroup);
        noFilterRadioButton.setToggleGroup(buttonGroup);
        ownedRadioButton.setToggleGroup(buttonGroup);
        relDateRadioButton.setToggleGroup(buttonGroup);
        tagRadioButton.setToggleGroup(buttonGroup);
        titleRadioButton.setToggleGroup(buttonGroup);
        typeRadioButton.setToggleGroup(buttonGroup);
        noFilterRadioButton.setSelected(true);
        //text field initialization
        lengthField.textProperty().addListener(new ChangeListener<String>() {       //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    lengthField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        //buttons initialization
        chooseRandomButton.setOnAction(this::handleChooseRandomButton);
        applyFiltersButton.setOnAction(this::handleApplyFiltersButton);

        retrieveTableController();
    }
    private void handleChooseRandomButton (ActionEvent event) {
        List<Album> albumTemp = setFilters();
        if (albumTemp != null) {
            Random rand = new Random();
            int index = 0;
            if (!albumTemp.isEmpty()) {
                if (albumTemp.size() > 1){
                    index = rand.nextInt(albumTemp.size()-1);
                }
                Album chosenAlbum = albumTemp.get(index);
                retrieveMainController();
                mainController.setManageScreen("DetailsScreen", chosenAlbum.getId());
            } else {
                showNoAlbumFound();
            }
        } else {
            showNoAlbumFound();
        }
    }
    private void handleApplyFiltersButton (ActionEvent event) {
        List<Album> albumTemp = setFilters();
        if (filterApplied && albumTemp != null) {
            tableController.setDataAndRefresh(albumTemp);
        }
        else {
            tableController.refreshTable();
        }
    }
    private List<Album> setFilters() {
        RadioButton selectedRadioButton = (RadioButton) buttonGroup.getSelectedToggle();
        String buttonId = selectedRadioButton.getId();
        listenedTo = false;
        if (tableController.getDataModeEnum() == DbDataModeEnum.LISTENED) {
            listenedTo = true;
        }
        List<Album> albumTemp = null;
        filterApplied = true;
        switch (buttonId){
            case "addDateRadioButton": {
                System.out.println("addDate chosen");
                albumTemp = new ArrayList<>(albumService.selectByAdditionDateAndListened(
                        addDatePicker.getValue(), addDateModePicker.getValue().toString(), listenedTo));
                break;
            }
            case "artistRadioButton": {
                System.out.println("Artist chosen");
                albumTemp = new ArrayList<>(albumService.selectByArtistAndListened(
                   artistField.getText(), listenedTo));
                break;
            }
            case "formatRadioButton": {
                System.out.println("Format chosen");
                albumTemp = new ArrayList<>(albumService.selectByFormatAndListened(
                        formatPicker.getSelectionModel().getSelectedItem().toString(), listenedTo));
                break;
            }
            case "genreRadioButton": {
                System.out.println("Genre chosen");
                albumTemp = new ArrayList<>(albumService.selectByGenreAndListened(
                        genreField.getText(), listenedTo));
                break;
            }
            case "lengthRadioButton": {
                System.out.println("Length chosen");
                albumTemp = new ArrayList<>(albumService.selectByLengthAndListened(
                        Integer.parseInt(lengthField.getText())*60, lengthModePicker.getValue().toString(), listenedTo));
                break;
            }
            case "languageRadioButton": {
                System.out.println("Language chosen");
                albumTemp = new ArrayList<>(albumService.selectByLanguageAndListened(
                        languageField.getText(), listenedTo));
                break;
            }
            case "ownedRadioButton": {
                System.out.println("Owned chosen");
                albumTemp = new ArrayList<>(albumService.selectByOwnedAndListened(
                        ownedCheckBox.isSelected(), listenedTo));
                break;
            }
            case "relDateRadioButton": {
                System.out.println("Release date chosen");
                albumTemp = new ArrayList<>(albumService.selectByReleaseDateAndListened(
                        relDatePicker.getValue(), relDateModePicker.getValue().toString(), listenedTo));
                break;
            }
            case "tagRadioButton": {
                System.out.println("Tag chosen");
                albumTemp = new ArrayList<>(albumService.selectByTagAndListened(
                        tagField.getText(), listenedTo));
                break;
            }
            case "titleRadioButton": {
                System.out.println("Title chosen");
                albumTemp = new ArrayList<>(albumService.selectByTitleAndListened(
                        titleField.getText(), listenedTo));
                break;
            }
            case "typeRadioButton": {
                System.out.println("Type chosen");
                albumTemp = new ArrayList<>(albumService.selectByTypeAndListened(
                        typePicker.getSelectionModel().getSelectedItem().toString(), listenedTo));
                break;
            }
            default: {
                System.out.println("No filter");
                filterApplied = false;
                break;
            }
        }
        return albumTemp;
    }
    private void retrieveTableController() {
        TableDataHolder tableDataHolder = TableDataHolder.getInstance();
        tableController = tableDataHolder.getTableController();
    }
    private void retrieveMainController() {
        MainScreenHolder mainScreenHolder = MainScreenHolder.getInstance();
        mainController = mainScreenHolder.getMainScreenController();
    }
    private void showNoAlbumFound() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No album found");
        alert.setContentText("No albums matching the given criteria were found. Please try setting different ones.");
        alert.show();
    }
}
