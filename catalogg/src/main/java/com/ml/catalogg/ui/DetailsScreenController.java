package com.ml.catalogg.ui;

import com.ml.catalogg.entity.*;
import com.ml.catalogg.helper.HyperlinkCell;
import com.ml.catalogg.helper.TableMediaLink;
import com.ml.catalogg.helper.TableSong;
import com.ml.catalogg.service.AlbumService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class DetailsScreenController implements Initializable {
    @FXML
    private VBox addRoot;
    @FXML
    private Label addDateLabel;
    @FXML
    private ImageView albumCover;
    @FXML
    private Label albumLengthLabel;
    @FXML
    private TextField artistField;
    @FXML
    private Label countryLabel;
    @FXML
    private Label formatLabel;
    @FXML
    private TextField genresField;
    @FXML
    private TextField languagesField;
    @FXML
    private TableColumn<TableSong, Duration> lengthCol;
    @FXML
    private TableColumn<TableMediaLink, Hyperlink> linkCol;
    @FXML
    private Label listenedToDateLabel;
    @FXML
    private Label listenedToStateLabel;
    @FXML
    private TableView<TableMediaLink> mediaTable;
    @FXML
    private TableColumn<TableMediaLink, String> nameCol;
    @FXML
    private TextArea noteInputField;
    @FXML
    private TableColumn<TableSong, Integer> numberCol;
    @FXML
    private Label ownedStateLabel;
    @FXML
    private Label releaseDateLabel;
    @FXML
    private TextField tagsField;
    @FXML
    private TableColumn<TableSong, String> titleCol;
    @FXML
    private TextField titleInputField;
    @FXML
    private TableView<TableSong> trackListingTable;
    @FXML
    private Label typeLabel;
    private ObservableList<TableMediaLink> mediaData;
    private ObservableList<TableSong> songsData;
    private Duration albumLength;
    @Autowired
    private AlbumService albumService;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mediaData = FXCollections.observableArrayList();
        songsData = FXCollections.observableArrayList();
        albumLength = Duration.ofMinutes(0);
    }

    public void setData(long albumId) {
        Album album = albumService.selectById(albumId);
        artistField.setText(album.getArtists().stream().map(Artist::getName)
                .collect(Collectors.joining("; ")));
        genresField.setText(album.getGenres().stream().map(Genre::getName)
                .collect(Collectors.joining("; ")));
        tagsField.setText(album.getTags().stream().map(Tags::getTag)
                .collect(Collectors.joining("; ")));
        languagesField.setText(album.getLanguages().stream().map(Language::getLanguage)
                .collect(Collectors.joining("; ")));
        countryLabel.setText(album.getCountry() == null ? "" : album.getCountry());
        formatLabel.setText(album.getFormat() == null ? "" : album.getFormat());
        listenedToStateLabel.setText(album.getListenedTo() == null ? "" : album.getListenedTo().toString());
        listenedToDateLabel.setText(album.getListenedDate() == null ? "" : album.getListenedDate().toString());
        noteInputField.setEditable(false);
        noteInputField.setText(album.getNotes() == null ? "" : album.getNotes());
        ownedStateLabel.setText(album.getOwned() == null ? "" : album.getOwned().toString());
        releaseDateLabel.setText(album.getReleaseDate() == null ? "" : album.getReleaseDate().toString());
        titleInputField.setText(album.getTitle() == null ? "" : album.getTitle());
        typeLabel.setText(album.getType() == null ? "" : album.getType());
        addDateLabel.setText(album.getAdditionDate() == null ? "" : album.getAdditionDate().toString());
        releaseDateLabel.setText(album.getReleaseDate() == null ? "" : album.getReleaseDate().toString());
        for (Songs song : album.getSongs()) {
            TableSong temp = new TableSong(song.getTitle(), Duration.ofSeconds(song.getLength()), song.getNumber());
            songsData.add(temp);
        }
        for (MediaLinks link : album.getMediaLinks()) {
            TableMediaLink temp = new TableMediaLink(link.getSiteName(), link.getLink());
            mediaData.add(temp);
        }
        setupMediaTable();
        setupTrackListingTable();
        for (TableSong song : trackListingTable.getItems()) {
            albumLength = albumLength.plus(song.getLength());
        }
        if (albumLength.toSecondsPart() < 10) {
            albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":0" + albumLength.toSecondsPart());
        } else {
            albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":" + albumLength.toSecondsPart());
        }
        System.out.println(album.getCoverPath());
        if (album.getCoverPath() != null) {
            File coverFile = new File (album.getCoverPath());
            if (album.getCoverPath().contains("file:\\")) {
                albumCover.setImage(new Image(coverFile.toString()));
            }
            else {
                albumCover.setImage(new Image(coverFile.toURI().toString()));
            }
        }
    }
    private void setupMediaTable() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("siteName"));
        linkCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        linkCol.setCellFactory(new HyperlinkCell());
        mediaTable.setItems(mediaData);
        mediaTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mediaTable.setFixedCellSize(25);
    }
    private void setupTrackListingTable() {
        trackListingTable.setFixedCellSize(25);
        //trackListingTable.prefHeightProperty()  //resize table based on number of rows (broken)
        //        .bind(Bindings.size(trackListingTable.getItems()).multiply(trackListingTable.getFixedCellSize()).add(30));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberCol.setSortable(false);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        numberCol.setSortable(false);
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        lengthCol.setSortable(false);
        trackListingTable.setItems(songsData);
        trackListingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
}
