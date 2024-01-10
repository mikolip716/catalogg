package com.ml.catalogg.ui;

import com.ml.catalogg.entity.Album;
import com.ml.catalogg.enums.DbDataModeEnum;
import com.ml.catalogg.enums.SortModeEnum;
import com.ml.catalogg.enums.SortOrderEnum;
import com.ml.catalogg.helper.PropertiesManager;
import com.ml.catalogg.helper.TableDataHolder;
import com.ml.catalogg.helper.MainTableAlbum;
import com.ml.catalogg.service.AlbumService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class MainTableController implements Initializable {
    @FXML
    private HBox tableRoot;
    @FXML
    private TableColumn<MainTableAlbum, String> addDateCol;
    @FXML
    private TableColumn<MainTableAlbum, String> artistCol;
    @FXML
    private TableColumn<MainTableAlbum, String> formatCol;
    @FXML
    private TableColumn<MainTableAlbum, String> genreCol;
    @FXML
    private TableColumn<MainTableAlbum, String> idCol;
    @FXML
    private TableColumn<MainTableAlbum, String> languageCol;
    @FXML
    private TableColumn<MainTableAlbum, Duration> lengthCol;
    @FXML
    private TableColumn<MainTableAlbum, String> releaseCol;
    @FXML
    private TableView<MainTableAlbum> tableView;
    @FXML
    private TableColumn<MainTableAlbum, String> titleCol;
    @FXML
    private TableColumn<MainTableAlbum, String> tagsCol;
    @FXML
    private TableColumn<MainTableAlbum, String> typeCol;
    @FXML
    private Label tableModeLabel;
    @Autowired
    private AlbumService albumService;
    private List<MainTableAlbum> dbData;
    @Getter
    private DbDataModeEnum dataModeEnum;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbData = new ArrayList<MainTableAlbum>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setEditable(false);

        idCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MainTableAlbum, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(TableColumn.CellDataFeatures<MainTableAlbum, String> p) {
                return new ReadOnlyObjectWrapper(tableView.getItems().indexOf(p.getValue())+1 + "");
            }
        });
        idCol.setSortable(false);
        addDateCol.setCellValueFactory(new PropertyValueFactory<>("addDate"));
        artistCol.setCellValueFactory(( TableColumn.CellDataFeatures<MainTableAlbum, String> p ) -> {
            List<String> artists = p.getValue().getArtists();
            String val = String.join((", "), artists);
            return new ReadOnlyStringWrapper( val );
        });
        formatCol.setCellValueFactory(new PropertyValueFactory<>("format"));
        genreCol.setCellValueFactory(( TableColumn.CellDataFeatures<MainTableAlbum, String> p ) -> {
            List<String> genres = p.getValue().getGenres();
            String val = String.join((", "), genres);
            return new ReadOnlyStringWrapper( val );
        });
        languageCol.setCellValueFactory(( TableColumn.CellDataFeatures<MainTableAlbum, String> p ) -> {
            List<String> langs = p.getValue().getLangs();
            String val = String.join((", "), langs);
            return new ReadOnlyStringWrapper( val );
        });
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        releaseCol.setCellValueFactory(new PropertyValueFactory<>("relDate"));
        tagsCol.setCellValueFactory(( TableColumn.CellDataFeatures<MainTableAlbum, String> p ) -> {
            List<String> tags = p.getValue().getTags();
            String val = String.join((", "), tags);
            return new ReadOnlyStringWrapper( val );
        });
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        tableView.setRowFactory(table -> {
            TableRow<MainTableAlbum> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    MainTableAlbum clickedRow = row.getItem();
                    sendData(clickedRow.getDbId());
                }
            });
            return row ;
        });
        sendController();
        insertDataFromDb(DbDataModeEnum.NOT_LISTENED);
        tableModeLabel.setText("To Listen");
        setDefaultSort();
    }
    private void setDefaultSort() {
        PropertiesManager propertiesManager = new PropertiesManager();
        propertiesManager.loadProperties();
        SortModeEnum sortMode = propertiesManager.getSortMode();
        SortOrderEnum sortOrder = propertiesManager.getSortOrder();
        TableColumn.SortType sortType = TableColumn.SortType.ASCENDING;
        if (sortOrder != null) {
            if (sortOrder.equals(SortOrderEnum.DESC)) {
                sortType = TableColumn.SortType.DESCENDING;
            }
            switch (sortMode){
                case ARTIST: {
                    artistCol.setSortType(sortType);
                    tableView.getSortOrder().add(artistCol);
                    tableView.sort();
                    break;
                } case TITLE: {
                    titleCol.setSortType(sortType);
                    tableView.getSortOrder().add(titleCol);
                    tableView.sort();
                    break;
                } case TYPE: {
                    typeCol.setSortType(sortType);
                    tableView.getSortOrder().add(typeCol);
                    tableView.sort();
                    break;
                } case FORMAT: {
                    formatCol.setSortType(sortType);
                    tableView.getSortOrder().add(formatCol);
                    tableView.sort();
                    break;
                } case LENGTH: {
                    lengthCol.setSortType(sortType);
                    tableView.getSortOrder().add(lengthCol);
                    tableView.sort();
                    break;
                } case DATE_ADDED: {
                    addDateCol.setSortType(sortType);
                    tableView.getSortOrder().add(addDateCol);
                    tableView.sort();
                    break;
                } case RELEASE_DATE: {
                    releaseCol.setSortType(sortType);
                    tableView.getSortOrder().add(releaseCol);
                    tableView.sort();
                    break;
                } default:
                    break;
            }
        }
    }
    private void insertDataFromDb(DbDataModeEnum dbDataMode) {
        dataModeEnum = dbDataMode;
        List<Album> retrievedData = new ArrayList<>();
        switch (dbDataMode) {
            case LISTENED -> {
                retrievedData = albumService.selectByListenedTo(true);
            }
            case NOT_LISTENED -> {
                retrievedData = albumService.selectByListenedTo(false);
            }
            case ALL -> {
                retrievedData = albumService.selectAll();
            }
        }
        for (Album album : retrievedData) {
            MainTableAlbum temp = new MainTableAlbum(
                    album.getArtists(), album.getTitle(), album.getGenres(), Duration.ofSeconds(album.getLength()),
                    album.getReleaseDate(), album.getAdditionDate(), album.getFormat(), album.getLanguages(), album.getTags(),
                    album.getType(), album.getId()
            );
            dbData.add(temp);
        }
        final ObservableList<MainTableAlbum> data = FXCollections.observableArrayList(dbData);
        tableView.setItems(data);
    }
    private void sendData (long albumId) {
        TableDataHolder tableDataHolder = TableDataHolder.getInstance();
        tableDataHolder.setAlbumId(albumId);
    }
    private void sendController() {
        TableDataHolder tableDataHolder = TableDataHolder.getInstance();
        tableDataHolder.setTableController(this);
    }
    public void refreshTable (DbDataModeEnum dbDataMode) {
        tableView.getItems().clear();
        dbData.clear();
        switch (dbDataMode) {
            case LISTENED -> {
                insertDataFromDb(DbDataModeEnum.LISTENED);
                tableModeLabel.setText("History");
            }
            case NOT_LISTENED -> {
                insertDataFromDb(DbDataModeEnum.NOT_LISTENED);
                tableModeLabel.setText("To Listen");
            }
            case ALL -> {
                insertDataFromDb(DbDataModeEnum.ALL);
                tableModeLabel.setText("Everything");
            }
        }
        System.out.print("refreshed");
    }
    public void refreshTable() {
        tableView.getItems().clear();
        dbData.clear();
        insertDataFromDb(dataModeEnum);
    }
    public void setDataAndRefresh(List<Album> albums) {
        tableView.getItems().clear();
        dbData.clear();
        for (Album album : albums) {
            MainTableAlbum temp = new MainTableAlbum(
                    album.getArtists(), album.getTitle(), album.getGenres(), Duration.ofSeconds(album.getLength()),
                    album.getReleaseDate(), album.getAdditionDate(), album.getFormat(), album.getLanguages(), album.getTags(),
                    album.getType(), album.getId()
            );
            dbData.add(temp);
        }
        final ObservableList<MainTableAlbum> data = FXCollections.observableArrayList(dbData);
        tableView.setItems(data);
    }
}


