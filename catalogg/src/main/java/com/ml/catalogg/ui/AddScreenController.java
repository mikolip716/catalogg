package com.ml.catalogg.ui;
import com.ml.catalogg.entity.*;
import com.ml.catalogg.enums.ApiCallModeEnum;
import com.ml.catalogg.enums.FormatEnum;
import com.ml.catalogg.enums.TypeEnum;
import com.ml.catalogg.helper.*;
import com.ml.catalogg.service.AlbumService;
import com.ml.catalogg.service.ApiService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AddScreenController implements Initializable {
    @FXML
    private Button addToDb;
    @FXML
    private ImageView albumCover;
    @FXML
    private Label albumLengthLabel;
    @FXML
    private TextField apiArtistField;
    @FXML
    private Button apiFindButton;
    @FXML
    private Button apiFindCoverButton;
    @FXML
    private TextField apiIdField;
    @FXML
    private TextField apiTitleField;
    @FXML
    private Button artistAddButton;
    @FXML
    private TextField artistField;
    @FXML
    private TextField artistInputField;
    @FXML
    private Button artistRemoveButton;
    @FXML
    private TextField artistRemoveInputField;
    @FXML
    private Button clearButton;
    @FXML
    private TextField countryInputField;
    @FXML
    private ChoiceBox<FormatEnum> formatPicker;
    @FXML
    private Button genreAddButton;
    @FXML
    private TextField genreInputField;
    @FXML
    private Button genreRemoveButton;
    @FXML
    private TextField genreRemoveInputField;
    @FXML
    private TextField genresField;
    @FXML
    private Button langAddButton;
    @FXML
    private TextField langInputField;
    @FXML
    private Button langRemoveButton;
    @FXML
    private TextField langRemoveInputField;
    @FXML
    private TextField languagesField;
    @FXML
    private TableColumn<TableSong, Duration> lengthCol;
    @FXML
    private TableColumn<TableMediaLink, Hyperlink> linkCol;
    @FXML
    private CheckBox listenedCheckBox;
    @FXML
    private Button mediaAddButton;
    @FXML
    private TextField mediaFieldRemove;
    @FXML
    private TextField mediaLinkField;
    @FXML
    private TextField mediaNameField;
    @FXML
    private Button mediaRemoveButton;
    @FXML
    private TableView<TableMediaLink> mediaTable;
    @FXML
    private TableColumn<TableMediaLink, String> nameCol;
    @FXML
    private TextArea noteInputField;
    @FXML
    private TableColumn<TableSong, Integer> numberCol;
    @FXML
    private CheckBox ownedCheckBox;
    @FXML
    private DatePicker releaseDatePicker;
    @FXML
    private VBox addRoot;
    @FXML
    private Button songAddButton;
    @FXML
    private TextField songMinInputFiled;
    @FXML
    private TextField songNumberField;
    @FXML
    private Button songRemoveButton;
    @FXML
    private TextField songRemoveField;
    @FXML
    private TextField songSecInputField;
    @FXML
    private TextField songTitleInputField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button tagAddButton;
    @FXML
    private TextField tagInputField;
    @FXML
    private Button tagRemoveButton;
    @FXML
    private TextField tagRemoveInputField;
    @FXML
    private TextField tagsField;
    @FXML
    private TableColumn<TableSong, String> titleCol;
    @FXML
    private TextField titleInputField;
    @FXML
    private TableView<TableSong> trackListingTable;
    @FXML
    private ChoiceBox<TypeEnum> typePicker;
    private Album newAlbum;
    private List<String> artists;
    private List<String> genres;
    private List<String> languages;
    private List<String> tags;
    private ObservableList<TableMediaLink> mediaData;
    private ObservableList<TableSong> songsData;
    private Duration albumLength;
    private String coverPath;
    @Autowired
    private AlbumService albumService;
    private ApiService apiService;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialization
        newAlbum = new Album();
        artists = new ArrayList<>();
        genres = new ArrayList<>();
        tags = new ArrayList<>();
        languages = new ArrayList<>();
        mediaData = FXCollections.observableArrayList();
        songsData = FXCollections.observableArrayList();
        albumLength = Duration.ofMinutes(0);
        //setting up controls on the screen
        setButtonHandlers();
        setupTextFields();
        setupMediaTable();
        setupTrackListingTable();
        typePicker.setItems(FXCollections.observableArrayList(TypeEnum.values()));
        formatPicker.setItems(FXCollections.observableArrayList(FormatEnum.values()));
        releaseDatePicker.setShowWeekNumbers(false);
        albumCover.setOnMouseClicked(this::handleCoverImageClicked);
        apiService = new ApiService();
    }

    public void handleAddToDbButton (ActionEvent event) {
        List<Genre> genresToAdd = new ArrayList<>();
        for (String genre : genres) {
            Genre temp = new Genre();
            temp.setName(genre.toLowerCase());
            genresToAdd.add(temp);
        }
        List<Artist> artistsToAdd = new ArrayList<>();
        for (String artist : artists) {
            Artist temp = new Artist();
            temp.setName(artist);
            artistsToAdd.add(temp);
        }
        List<Tags> tagsToAdd = new ArrayList<>();
        for (String tag : tags) {
            Tags temp = new Tags();
            temp.setTag(tag.toLowerCase());
            tagsToAdd.add(temp);
        }
        List<Language> langsToAdd = new ArrayList<>();
        for (String lang : languages) {
            Language temp = new Language();
            temp.setLanguage(lang.toLowerCase());
            langsToAdd.add(temp);
        }
        List<MediaLinks> mediaToAdd = new ArrayList<>();
        for (TableMediaLink mediaLink : mediaTable.getItems()) {
            MediaLinks temp = new MediaLinks();
            temp.setSiteName(mediaLink.getSiteName());
            temp.setLink(mediaLink.getUrl().getText());
            System.out.println(temp.getLink());
            mediaToAdd.add(temp);
        }
        List<Songs> songsToAdd = new ArrayList<>();
        for (TableSong song : trackListingTable.getItems()) {
            Songs temp = new Songs();
            temp.setNumber(song.getNumber());
            temp.setTitle(song.getTitle());
            temp.setLength((int)song.getLength().getSeconds());
            songsToAdd.add(temp);
        }

        newAlbum.setTitle(titleInputField.getText());
        newAlbum.setListenedTo(listenedCheckBox.isSelected());
        if (listenedCheckBox.isSelected()) {
            newAlbum.setListenedDate(LocalDate.now());
        }
        newAlbum.setOwned(ownedCheckBox.isSelected());
        newAlbum.setCountry(countryInputField.getText());
        newAlbum.setAdditionDate(LocalDate.now());
        newAlbum.setNotes(noteInputField.getText());
        String albumFormat = formatPicker.getSelectionModel().getSelectedItem() != null
                ? formatPicker.getSelectionModel().getSelectedItem().toString() : null;
        newAlbum.setFormat(albumFormat);
        String albumType = typePicker.getSelectionModel().getSelectedItem() != null
                ? typePicker.getSelectionModel().getSelectedItem().toString() : null;
        newAlbum.setType(albumType);
        newAlbum.setReleaseDate(releaseDatePicker.getValue());
        newAlbum.setLength((int)albumLength.getSeconds());
        newAlbum.setArtists(artistsToAdd);
        newAlbum.setGenres(genresToAdd);
        newAlbum.setLanguages(langsToAdd);
        newAlbum.setTags(tagsToAdd);
        newAlbum.setMediaLinks(mediaToAdd);
        newAlbum.setSongs(songsToAdd);
        newAlbum.setCoverPath(coverPath);
        albumService.saveAlbum(newAlbum);
        statusLabel.setText("Data saved");
        statusLabel.setTextFill(Color.GREEN);
    }
    public void handleClearButton (ActionEvent event) {
        clearData();
    }
    public void handleArtistAddButton (ActionEvent event) {
        artists.add(artistInputField.getText().trim());
        if (artistField.getText().isEmpty()) {
            artistField.setText(artistInputField.getText().trim());
        }
        else {
            artistField.setText(artistField.getText() + "; " + artistInputField.getText().trim());
        }
        artistInputField.clear();
    }
    public void handleArtistRemoveButton (ActionEvent event) {
        int tempPos = Integer.parseInt(artistRemoveInputField.getText());
        if (tempPos != 0) {     //emulate indexing from 1 for user convenience
            tempPos--;
        }
        if (tempPos < artists.size()) {
            artists.remove(tempPos);
        }
        //update the artists field in UI
        if (!artists.isEmpty()) {
            artistField.setText(artists.getFirst());
            for (int i = 1; i < artists.size(); i++) {
                artistField.setText(artistField.getText() + "; " + artists.get(i));
            }
        }
        else {
            artistField.setText("");
        }
    }
    public void handleGenreAddButton (ActionEvent event) {
        String input = genreInputField.getText().trim();
        if (input.contains(",")) {
            List<String> inputSplit = new ArrayList<>(List.of(input.split(",")));
            inputSplit.replaceAll(String::trim);
            genres.addAll(inputSplit);
        } else {
            genres.add(genreInputField.getText().trim());
        }
        String newText = String.join("; ", genres);
        genresField.setText(newText);
    }
    public void handleGenreRemoveButton (ActionEvent event) {
        int tempPos = Integer.parseInt(genreRemoveInputField.getText());
        if (tempPos != 0) {     //emulate indexing from 1 for user convenience
            tempPos--;
        }
        if (tempPos < genres.size()) {
            genres.remove(tempPos);
        }
        //update the genre field in UI
        if (!genres.isEmpty()) {
            genresField.setText(genres.getFirst());
            for (int i = 1; i < genres.size(); i++) {
                genresField.setText(genresField.getText() + "; " + genres.get(i));
            }
        }
        else {
            genresField.setText("");
        }
    }
    public void handleTagAddButton (ActionEvent event) {
        String input = tagInputField.getText().trim();
        if (input.contains(",")) {
            List<String> inputSplit = new ArrayList<>(List.of(input.split(",")));
            inputSplit.replaceAll(String::trim);
            tags.addAll(inputSplit);
        } else {
            tags.add(tagInputField.getText().trim());
        }
        String newText = String.join("; ", tags);
        tagsField.setText(newText);
    }
    public void handleTagRemoveButton (ActionEvent event) {
        int tempPos = Integer.parseInt(tagRemoveInputField.getText());
        if (tempPos != 0) {     //emulate indexing from 1 for user convenience
            tempPos--;
        }
        if (tempPos < tags.size()) {
            tags.remove(tempPos);
        }
        //update the tag field in UI
        if (!tags.isEmpty()) {
            tagsField.setText(tags.getFirst());
            for (int i = 1; i < tags.size(); i++) {
                tagsField.setText(tagsField.getText() + "; " + tags.get(i));
            }
        }
        else {
            tagsField.setText("");
        }
    }
    public void handleLanguageAddButton (ActionEvent event) {
        String input = langInputField.getText().trim();
        if (input.contains(",")) {
            List<String> inputSplit = new ArrayList<>(List.of(input.split(",")));
            inputSplit.replaceAll(String::trim);
            languages.addAll(inputSplit);
        } else {
            languages.add(langInputField.getText().trim());
        }
        String newText = String.join("; ", languages);
        languagesField.setText(newText);
    }
    public void handleLanguageRemoveButton (ActionEvent event) {
        int tempPos = Integer.parseInt(langRemoveInputField.getText());
        if (tempPos != 0) {     //emulate indexing from 1 for user convenience
            tempPos--;
        }
        if (tempPos < languages.size()) {
            languages.remove(tempPos);
        }
        //update the artists field in UI
        if (!languages.isEmpty()) {
            languagesField.setText(languages.getFirst());
            for (int i = 1; i < languages.size(); i++) {
                languagesField.setText(languagesField.getText() + "; " + languages.get(i));
            }
        }
        else {
            languagesField.setText("");
        }
    }
    public void handleAddMediaButton (ActionEvent event) {
        TableMediaLink tableMediaLink = new TableMediaLink(mediaNameField.getText(), mediaLinkField.getText());
        mediaData.add(tableMediaLink);
        mediaNameField.clear();
        mediaLinkField.clear();
    }
    public void handleRemoveMediaButton (ActionEvent event) {
        int tempPos = Integer.parseInt(mediaFieldRemove.getText());
        if (tempPos != 0) {     //emulate indexing from 1 for user convenience
            tempPos--;
        }
        if (tempPos < mediaData.size()) {
            mediaTable.getItems().remove(tempPos);
        }
    }
    public void handleAddSongButton (ActionEvent event) {
        Duration songLength = Duration.ofMinutes(Integer.parseInt(songMinInputFiled.getText()));
        songLength = songLength.plusSeconds(Integer.parseInt(songSecInputField.getText()));
        TableSong tableSong = new TableSong(songTitleInputField.getText(), songLength, songNumberField.getText());
        songsData.add(tableSong);
        albumLength = albumLength.plusMinutes(Integer.parseInt(songMinInputFiled.getText()));
        albumLength = albumLength.plusSeconds(Integer.parseInt(songSecInputField.getText()));
        if (albumLength.toSecondsPart() < 10) {
            albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":0" + albumLength.toSecondsPart());
        } else {
            albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":" + albumLength.toSecondsPart());
        }
        songNumberField.clear();
        songTitleInputField.clear();
        songMinInputFiled.clear();
        songSecInputField.clear();
    }
    public void handleRemoveSongButton (ActionEvent event) {
        int tempPos = Integer.parseInt(songRemoveField.getText());
        if (tempPos != 0) {     //emulate indexing from 1 for user convenience
            tempPos--;
        }
        if (tempPos < songsData.size()) {
            TableSong temp = trackListingTable.getItems().get(tempPos);
            albumLength = albumLength.minusMinutes(Integer.parseInt(songMinInputFiled.getText()));
            albumLength = albumLength.minusSeconds(Integer.parseInt(songSecInputField.getText()));
            long seconds = albumLength.getSeconds() - albumLength.toMinutes()*60;
            if (albumLength.toSecondsPart() < 10) {
                albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":0" + albumLength.toSecondsPart());
            } else {
                albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":" + albumLength.toSecondsPart());
            }
            trackListingTable.getItems().remove(tempPos);
        }
    }
    public void handleCoverImageClicked (MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose album cover");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File originalFile = fileChooser.showOpenDialog(addRoot.getScene().getWindow());
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "\\covers")); //create dir if it doesn't exist
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Original path: " + originalFile.toPath());
        try {
            File newFile = new File(System.getProperty("user.dir") + "\\covers\\" + originalFile.getName());
            coverPath = newFile.toURI().toString();
            if (originalFile != newFile) {
                FileUtil.copyFile(originalFile, newFile);
            }
            albumCover.setImage(new Image(coverPath));
        } catch (IOException e) {
           statusLabel.setText("An error occurred while getting cover");
           statusLabel.setTextFill(Color.RED);
        }
    }
    private Album getAlbumByMbid (String mbid) throws Exception {
        String baseUrlStart = "https://musicbrainz.org/ws/2/release/";
        String baseUrlEnd = "?inc=release-groups+genres+recordings+artists";
        String fullUrl = baseUrlStart + mbid + baseUrlEnd;
        Album apiAlbum = apiService.getAlbum(fullUrl);
        mediaData.add(apiService.getMusicbrainzLink());
        return getAlbumCover(mbid, apiAlbum);
    }

    private Album getAlbumCover(String mbid, Album apiAlbum) {
        if (apiService.isHasCover()) {
            try {
                String coverFilePath = apiService.getCover(mbid);
                if (coverFilePath != null) {
                    apiAlbum.setCoverPath(coverFilePath);
                    coverPath = coverFilePath;
                }
            } catch (IOException e) {
                statusLabel.setText("An error occurred while getting cover");
                statusLabel.setTextFill(Color.RED);
            }
        }
        return apiAlbum;
    }

    private Album getAlbumByTitleAndArtist (String title, String artist) throws Exception {
        String baseUrlStart = "http://musicbrainz.org/ws/2/release/?query=release:";
        String baseUrlMiddle = " AND artist:";
        String fullUrl = baseUrlStart + title.trim() + baseUrlMiddle + artist.trim();
        fullUrl = fullUrl.replace(" ", "%20");
        String mbid = apiService.getMbid(fullUrl);
        Album apiAlbum = new Album();
        if (mbid != null) {
            apiAlbum = getAlbumByMbid(mbid);
        }
        return getAlbumCover(mbid, apiAlbum);
    }
    private void handleApiFindButton(ActionEvent event) {
        clearData();
        statusLabel.setText("Searching...");
        if (!apiIdField.getText().isEmpty() || !apiIdField.getText().isBlank()) {
            try {
                doApiCall(ApiCallModeEnum.MBID);
            } catch (Exception e) {
                statusLabel.setText("An error occurred getting data");
                statusLabel.setTextFill(Color.RED);
            }
        } else if (apiArtistField.getText() != null && apiTitleField.getText() != null) {
            try {
                doApiCall(ApiCallModeEnum.TITLE_AND_ARTIST);
            } catch (Exception e) {
                statusLabel.setText("An error occurred getting data");
                statusLabel.setTextFill(Color.RED);
            }
        }
    }
    private void handleApiFindCoverButton (ActionEvent event) {
        statusLabel.setText("Searching...");
        if (!apiIdField.getText().isEmpty() || !apiIdField.getText().isBlank()) {
            try {
                doApiCall(ApiCallModeEnum.COVER_ONLY);
            } catch (Exception e) {
                statusLabel.setText("An error occurred while getting cover");
                statusLabel.setTextFill(Color.RED);
            }
        } else {
            statusLabel.setText("Finding cover is only supported using MBID");
        }
    }
    private void doApiCall(ApiCallModeEnum mode) throws Exception{
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            switch (mode) {
                case MBID -> {
                    Task<Album> task = new Task<Album>() {
                        @Override
                        protected Album call() throws Exception {
                            return getAlbumByMbid(apiIdField.getText().trim());
                        }
                    };
                    setupTask(task);
                    executorService.submit(task);
                    break;
                } case TITLE_AND_ARTIST -> {
                    Task<Album> task = new Task<Album>() {
                        @Override
                        protected Album call() throws Exception {
                            return getAlbumByTitleAndArtist(apiArtistField.getText().trim(), apiTitleField.getText().trim());
                        }
                    };
                    setupTask(task);
                    executorService.submit(task);
                    break;
                } case COVER_ONLY -> {
                    Task<String> task = new Task<String>() {
                        @Override
                        protected String call() throws Exception {
                            return apiService.getCover(apiIdField.getText().trim());
                        }
                    };
                    task.setOnRunning((e) -> statusLabel.setText("Searching..."));
                    task.setOnSucceeded((e) -> {
                        coverPath = task.getValue();
                        File coverFile = new File (coverPath);
                        albumCover.setImage(new Image(coverFile.toURI().toString()));
                        statusLabel.setText("Cover found!");
                        statusLabel.setTextFill(Color.GREEN);
                    });
                    task.setOnFailed((e) -> {
                        statusLabel.setText("An error occurred getting cover");
                        statusLabel.setTextFill(Color.RED);
                    });
                    executorService.submit(task);
                    break;
                }
            }
        }
    }

    private void setupTask(Task<Album> task) {
        task.setOnRunning((e) -> statusLabel.setText("Searching..."));
        task.setOnSucceeded((e) -> {
            setData(task.getValue());
            statusLabel.setText("Data found!");
            statusLabel.setTextFill(Color.GREEN);
        });
        task.setOnFailed((e) -> {
            statusLabel.setText("An error occurred getting data");
            statusLabel.setTextFill(Color.RED);
        });
    }

    private void setButtonHandlers() {
        addToDb.setOnAction(this::handleAddToDbButton);
        artistAddButton.setOnAction(this::handleArtistAddButton);
        artistRemoveButton.setOnAction(this::handleArtistRemoveButton);
        genreAddButton.setOnAction(this::handleGenreAddButton);
        genreRemoveButton.setOnAction(this::handleGenreRemoveButton);
        tagAddButton.setOnAction(this::handleTagAddButton);
        tagRemoveButton.setOnAction(this::handleTagRemoveButton);
        langAddButton.setOnAction(this::handleLanguageAddButton);
        langRemoveButton.setOnAction(this::handleLanguageRemoveButton);
        mediaAddButton.setOnAction(this::handleAddMediaButton);
        mediaRemoveButton.setOnAction(this::handleRemoveMediaButton);
        songAddButton.setOnAction(this::handleAddSongButton);
        songRemoveButton.setOnAction(this::handleRemoveSongButton);
        apiFindButton.setOnAction(this::handleApiFindButton);
        apiFindCoverButton.setOnAction(this::handleApiFindCoverButton);
        clearButton.setOnAction(this::handleClearButton);
    }
    private void setupTextFields() {
        artistField.setEditable(false);
        artistRemoveInputField.textProperty().addListener(new ChangeListener<String>() {    //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    artistRemoveInputField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        genresField.setEditable(false);
        genreRemoveInputField.textProperty().addListener(new ChangeListener<String>() {     //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    genreRemoveInputField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        languagesField.setEditable(false);
        langRemoveInputField.textProperty().addListener(new ChangeListener<String>() {      //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    langRemoveInputField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        tagsField.setEditable(false);
        tagRemoveInputField.textProperty().addListener(new ChangeListener<String>() {       //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tagRemoveInputField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        songRemoveField.textProperty().addListener(new ChangeListener<String>() {       //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    songRemoveField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        songMinInputFiled.textProperty().addListener(new ChangeListener<String>() {       //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    songMinInputFiled.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        songSecInputField.textProperty().addListener(new ChangeListener<String>() {       //set as numeric input only
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    songSecInputField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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
        titleCol.setSortable(false);
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));
        lengthCol.setSortable(false);
        trackListingTable.setItems(songsData);
        trackListingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    public void setData(Album album) {
        if (album.getArtists() != null) {
            for (Artist artist : album.getArtists()) {
                artists.add(artist.getName());
            }
            artistField.setText(String.join("; ", artists));
        }
        if (album.getGenres() != null) {
            for (Genre genre : album.getGenres()) {
                genres.add(genre.getName());
            }
            genresField.setText(String.join("; ", genres));
        }
        if (album.getTags() != null) {
            for (Tags tag : album.getTags()) {
                tags.add(tag.getTag());
            }
            tagsField.setText(String.join("; ", tags));
        }
        if (album.getLanguages() != null) {
            for (Language lang : album.getLanguages()) {
                languages.add(lang.getLanguage());
            }
            languagesField.setText(String.join("; ", languages));
        }
        countryInputField.setText(album.getCountry() == null ? "" : album.getCountry());
        if (album.getFormat() != null) {
            formatPicker.getSelectionModel().select(FormatEnum.valueOf(album.getFormat()));
        }
        if (album.getType() != null) {
            typePicker.getSelectionModel().select(TypeEnum.valueOf(album.getType()));
        }
        noteInputField.setText(album.getNotes() == null ? "" : album.getNotes());
        if (album.getOwned() != null) {
            ownedCheckBox.setSelected(album.getOwned());
        }
        if (album.getListenedDate() != null) {
            listenedCheckBox.setSelected(album.getListenedTo());
        }
        if (album.getReleaseDate() != null) {
            releaseDatePicker.setValue(album.getReleaseDate());
        }
        titleInputField.setText(album.getTitle() == null ? "" : album.getTitle());
        if (album.getSongs() != null) {
            for (Songs song : album.getSongs()) {
                TableSong temp = new TableSong(song.getTitle(), Duration.ofSeconds(song.getLength()), song.getNumber());
                songsData.add(temp);
            }
            setupTrackListingTable();
            for (TableSong song : trackListingTable.getItems()) {
                albumLength = albumLength.plus(song.getLength());
            }
            if (albumLength.toSecondsPart() < 10) {
                albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":0" + albumLength.toSecondsPart());
            } else {
                albumLengthLabel.setText("Album length: " + albumLength.toMinutes() + ":" + albumLength.toSecondsPart());
            }
        }
        if (album.getMediaLinks() != null) {
            for (MediaLinks link : album.getMediaLinks()) {
                TableMediaLink temp = new TableMediaLink(link.getSiteName(), link.getLink());
                mediaData.add(temp);
            }
            setupMediaTable();
        }
        if (album.getCoverPath() != null) {
            File coverFile = new File (album.getCoverPath());
            albumCover.setImage(new Image(coverFile.toURI().toString()));
            System.out.println(coverPath);
        }
    }
    private void clearData() {
        newAlbum = new Album();
        artists = new ArrayList<>();
        genres = new ArrayList<>();
        tags = new ArrayList<>();
        languages = new ArrayList<>();
        mediaData = FXCollections.observableArrayList();
        songsData = FXCollections.observableArrayList();
        albumLength = Duration.ofMinutes(0);
        mediaData.clear();
        setupMediaTable();
        songsData.clear();
        setupTrackListingTable();
        titleInputField.clear();
        artistField.clear();
        genresField.clear();
        tagsField.clear();
        languagesField.clear();
        releaseDatePicker.getEditor().clear();
        formatPicker.setValue(null);
        typePicker.setValue(null);
        ownedCheckBox.setSelected(false);
        listenedCheckBox.setSelected(false);
        countryInputField.clear();
        albumLengthLabel.setText("Album length:");
        noteInputField.clear();
        statusLabel.setText("");
        statusLabel.setTextFill(Color.color(102.0/255.0, 102.0/255.0, 102.0/255.0));
        try {
            albumCover.setImage(new Image(getClass().getClassLoader().getResource("album_cover_placeholder.png").toURI().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
