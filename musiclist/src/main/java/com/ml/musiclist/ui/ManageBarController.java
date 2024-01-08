package com.ml.musiclist.ui;

import com.ml.musiclist.entity.Album;
import com.ml.musiclist.enums.DbDataModeEnum;
import com.ml.musiclist.helper.MainScreenHolder;
import com.ml.musiclist.helper.TableDataHolder;
import com.ml.musiclist.service.AlbumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ManageBarController implements Initializable {
    @FXML
    private Button deletRowButton;
    @FXML
    private HBox root;
    @FXML
    private Button editRowButton;
    @FXML
    private Button setAsListenedButton;
    @FXML
    private Button setAsNotListenedButton;
    @FXML
    private Button showDetailsButton;
    @FXML
    private Button showHistoryButton;
    @FXML
    private Button showToListenButton;
    @Autowired
    private AlbumService albumService;
    private Long albumId;
    private MainTableController tableController;
    private MainScreenController mainController;
    private DbDataModeEnum currentTableMode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deletRowButton.setOnAction(this::handleDeleteRowButton);
        setAsListenedButton.setOnAction(this::handleSetAsListenedButton);
        setAsNotListenedButton.setOnAction(this::handleSetAsNotListenedButton);
        showDetailsButton.setOnAction(this::handleShowDetailsButton);
        showHistoryButton.setOnAction(this::handleShowHistoryButton);
        showToListenButton.setOnAction(this::handleShowToListenButton);
        editRowButton.setOnAction(this::handleEditRowButton);
        currentTableMode = DbDataModeEnum.NOT_LISTENED;
    }
    private void retrieveTableData() {
        TableDataHolder tableDataHolder = TableDataHolder.getInstance();
        albumId = tableDataHolder.getAlbumId();
    }
    private void retrieveTableController() {
        TableDataHolder tableDataHolder = TableDataHolder.getInstance();
        tableController = tableDataHolder.getTableController();
    }
    private void retrieveMainController() {
        MainScreenHolder mainScreenHolder = MainScreenHolder.getInstance();
        mainController = mainScreenHolder.getMainScreenController();
    }
    public void handleDeleteRowButton (ActionEvent event) {
        retrieveTableData();     //get ID of album selected in the table
        retrieveTableController();
        if (albumId != -1) {
            confirmDelete();
        } else {
            showNothingSelectedAlert();
        }
    }
    public void handleSetAsListenedButton (ActionEvent event) {
        retrieveTableData();     //get ID of album selected in the table
        retrieveTableController();
        if (albumId != -1) {
            albumService.changeAlbumListenedState(albumId, true);
            tableController.refreshTable(currentTableMode);
        } else {
            showNothingSelectedAlert();
        }
    }
    public void handleSetAsNotListenedButton (ActionEvent event) {
        retrieveTableData();     //get ID of album selected in the table
        retrieveTableController();
        if (albumId != -1) {
            albumService.changeAlbumListenedState(albumId, false);
            tableController.refreshTable(currentTableMode);
        } else {
            showNothingSelectedAlert();
        }
    }
    public void handleShowDetailsButton (ActionEvent event) {
        retrieveTableData();     //get ID of album selected in the table
        retrieveMainController();   //get main screen controller to show details screen without this screen visible
        if (albumId != -1) {
            mainController.setManageScreen("DetailsScreen", albumId);
        } else {
            showNothingSelectedAlert();
        }
    }
    public void handleEditRowButton (ActionEvent event) {
        retrieveTableData();     //get ID of album selected in the table
        retrieveMainController();   //get main screen controller to show details screen without this screen visible
        if (albumId != -1) {
            mainController.setManageScreen("EditScreen", albumId);
        } else {
            showNothingSelectedAlert();
        }
    }
    public void handleShowHistoryButton (ActionEvent event) {
        retrieveTableController();
        currentTableMode = DbDataModeEnum.LISTENED;
        tableController.refreshTable(currentTableMode);
    }
    public void handleShowToListenButton (ActionEvent event) {
        retrieveTableController();
        currentTableMode = DbDataModeEnum.NOT_LISTENED;
        tableController.refreshTable(currentTableMode);
    }
    private void showNothingSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No row selected");
        alert.setContentText("Can't perform action: no row selected");
        alert.show();
    }
    private void confirmDelete() {
        Album albumToDelete = albumService.selectById(albumId);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "" ,ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm deletion");
        if (albumToDelete.getTitle() != null) {
            alert.setContentText("Are you sure you want to delete selected album (" + albumToDelete.getTitle() + ")?");
        } else {
            alert.setContentText("Are you sure you want to delete selected album?");
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.YES) {
                albumService.deleteAlbum(albumId);
                tableController.refreshTable(currentTableMode);
            }
        }
    }
}

