package com.ml.musiclist.ui;

import com.ml.musiclist.dbcount.ArtistCount;
import com.ml.musiclist.dbcount.GenreCount;
import com.ml.musiclist.dbcount.TagCount;
import com.ml.musiclist.entity.Album;
import com.ml.musiclist.entity.Songs;
import com.ml.musiclist.enums.*;
import com.ml.musiclist.service.AlbumService;
import com.ml.musiclist.service.SongsService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Component
public class StatsScreenController implements Initializable {
    @FXML
    private Button applySettingsButton;
    @FXML
    private NumberAxis artistAmountAxis;
    @FXML
    private CategoryAxis artistAxis;
    @FXML
    private BarChart<String, Integer> artistsBarChart;
    @FXML
    private Label avgAlbumLabel;
    @FXML
    private Label avgSongLabel;
    @FXML
    private ChoiceBox<DbDataModeEnum> chartModePicker;
    @FXML
    private ChoiceBox<ChartTimeEnum> chartTimePicker;
    @FXML
    private NumberAxis decadesAmountAxis;
    @FXML
    private CategoryAxis decadesAxis;
    @FXML
    private BarChart<String, Integer> decadesBarChart;
    @FXML
    private PieChart formatPieChart;
    @FXML
    private NumberAxis genreAmountAxis;
    @FXML
    private CategoryAxis genreAxis;
    @FXML
    private BarChart<String, Integer> genresBarChart;
    @FXML
    private NumberAxis listenedToAmountAxis;
    @FXML
    private CategoryAxis listenedToDateAxis;
    @FXML
    private LineChart<String, Integer> listenedToLineChart;
    @FXML
    private PieChart listenedToPieChart;
    @FXML
    private Label longAlbumLabel;
    @FXML
    private Label longSongLabel;
    @FXML
    private PieChart ownedPieChart;
    @FXML
    private Label shortAlbumLabel;
    @FXML
    private Label shortSongLabel;
    @FXML
    private NumberAxis tagAmountAxis;
    @FXML
    private CategoryAxis tagsAxis;
    @FXML
    private BarChart<String, Integer> tagsBarChart;
    @FXML
    private PieChart typePieChart;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private SongsService songsService;
    private int lineChartMaxVal;
    private int decadesChartMaxVal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lineChartMaxVal = 0;
        setupPickers();
        setupCharts();
        applySettingsButton.setOnAction(this::handleApplySettingsButton);
    }
    private void setupPickers() {
        chartTimePicker.setItems(FXCollections.observableArrayList(ChartTimeEnum.values()));
        chartTimePicker.setValue(ChartTimeEnum.LAST_MONTH);
        chartModePicker.setItems(FXCollections.observableArrayList(DbDataModeEnum.values()));
        chartModePicker.setValue(DbDataModeEnum.LISTENED);
    }
    private void setupCharts() {
        listenedToPieChart.setLabelsVisible(false);
        updateListenedToPieChart(ChartTimeEnum.LAST_MONTH);
        formatPieChart.setLabelsVisible(false);
        formatPieChart.setLegendSide(Side.TOP);
        updateFormatPieChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        typePieChart.setLabelsVisible(false);
        typePieChart.setLegendSide(Side.TOP);
        updateTypePieChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        ownedPieChart.setLabelsVisible(false);
        ownedPieChart.setLegendSide(Side.TOP);
        updateOwnedPieChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        updateListenedLineChart(ChartTimeEnum.LAST_MONTH);
        listenedToLineChart.setLegendVisible(true);
        decadesBarChart.setLegendVisible(false);
        updateDecadesBarChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        updateTopArtistsChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        artistsBarChart.setLegendVisible(false);
        updateTopGenresChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        genresBarChart.setLegendVisible(false);
        updateTopTagsChart(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        tagsBarChart.setLegendVisible(false);
        updateAlbumLengthStats(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        updateSongLengthStats(ChartTimeEnum.LAST_MONTH, DbDataModeEnum.LISTENED);
        }
    private void updateCharts (DbDataModeEnum dataMode, ChartTimeEnum timeMode) {
        updateListenedToPieChart(timeMode);
        updateFormatPieChart(timeMode, dataMode);
        updateTypePieChart(timeMode, dataMode);
        updateOwnedPieChart(timeMode, dataMode);
        updateListenedLineChart(timeMode);
        updateDecadesBarChart(timeMode, dataMode);
        updateTopArtistsChart(timeMode, dataMode);
        updateTopGenresChart(timeMode, dataMode);
        updateTopTagsChart(timeMode, dataMode);
        updateAlbumLengthStats(timeMode, dataMode);
        updateSongLengthStats(timeMode, dataMode);
    }
    public void handleApplySettingsButton (ActionEvent event) {
        updateCharts(chartModePicker.getValue(), chartTimePicker.getValue());
    }
    private void updateSongLengthStats (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        Songs shortest = songsService.getShortest(cutoffDate, listenedMode.toString());
        Songs longest = songsService.getLongest(cutoffDate, listenedMode.toString());
        Double average = songsService.getAverage(cutoffDate, listenedMode.toString());
        if (shortest.getLength() != null && shortest.getTitle() != null) {
            Duration shortestLength = Duration.ofSeconds(shortest.getLength());
            if (shortestLength.toSecondsPart() < 10) {
                shortSongLabel.setText(shortest.getTitle() + " - " + shortestLength.toMinutes() + ":0" + shortestLength.toSecondsPart()
                        + " from " + shortest.getAlbum().getTitle());
            } else {
                shortSongLabel.setText(shortest.getTitle() + " - " + shortestLength.toMinutes() + ":" + shortestLength.toSecondsPart()
                        + " from " + shortest.getAlbum().getTitle());
            }
        }
        if (longest.getLength() != null && longest.getTitle() != null) {
            Duration longestLength = Duration.ofSeconds(longest.getLength());
            if (longestLength.toSecondsPart() < 10) {
                longSongLabel.setText(longest.getTitle() + " - " + longestLength.toMinutes() + ":0" + longestLength.toSecondsPart()
                        + " from " + longest.getAlbum().getTitle());
            } else {
                longSongLabel.setText(longest.getTitle() + " - " + longestLength.toMinutes() + ":" + longestLength.toSecondsPart()
                        + " from " + longest.getAlbum().getTitle());
            }
        }
        if (average != null) {
            Duration avgLength = Duration.ofSeconds(average.longValue());
            if (avgLength.toSecondsPart() < 10) {
                avgSongLabel.setText(avgLength.toMinutes() + ":0" + avgLength.toSecondsPart());
            } else {
                avgSongLabel.setText(avgLength.toMinutes() + ":" + avgLength.toSecondsPart());
            }
        }
    }
    private void updateAlbumLengthStats (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        Album shortest = albumService.getShortest(cutoffDate, listenedMode.toString());
        Album longest = albumService.getLongest(cutoffDate, listenedMode.toString());
        Double average = albumService.getAverage(cutoffDate, listenedMode.toString());
        if (shortest != null) {
            Duration shortestLength = Duration.ofSeconds(shortest.getLength());
            if (shortestLength.toSecondsPart() < 10) {
                shortAlbumLabel.setText(shortest.getTitle() + " - " + shortestLength.toMinutes() + ":0" + shortestLength.toSecondsPart());
            } else {
                shortAlbumLabel.setText(shortest.getTitle() + " - " + shortestLength.toMinutes() + ":" + shortestLength.toSecondsPart());
            }
        }
        if (longest != null) {
            Duration longestLength = Duration.ofSeconds(longest.getLength());
            if (longestLength.toSecondsPart() < 10) {
                longAlbumLabel.setText(longest.getTitle() + " - " + longestLength.toMinutes() + ":0" + longestLength.toSecondsPart());
            } else {
                longAlbumLabel.setText(longest.getTitle() + " - " + longestLength.toMinutes() + ":" + longestLength.toSecondsPart());
            }
        }
        if (average != null) {
            Duration avgLength = Duration.ofSeconds(average.longValue());
            if (avgLength.toSecondsPart() < 10) {
                avgAlbumLabel.setText(avgLength.toMinutes() + ":0" + avgLength.toSecondsPart());
            } else {
                avgAlbumLabel.setText(avgLength.toMinutes() + ":" + avgLength.toSecondsPart());
            }
        }
    }
    private void updateTopArtistsChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        List<ArtistCount> top10 = new ArrayList<>();
        List<ArtistCount> allCounts = albumService.getCountByArtistAfter(cutoffDate, listenedMode.toString());
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        for (int i = 0; i < 10; i++) {
            if (i < allCounts.size()){
                top10.add(allCounts.get(i));
            }
        }
        if (artistsBarChart.getData() != null) {
            artistsBarChart.getData().clear();
        }
        for (ArtistCount c : top10) {
            series.getData().add(new XYChart.Data<String, Integer>(c.getName(), Math.toIntExact(c.getCount())));
        }
        artistsBarChart.getData().add(series);
        if (!top10.isEmpty()) {
            artistAmountAxis.setAutoRanging(false);
            artistAmountAxis.setLowerBound(0);
            artistAmountAxis.setUpperBound(top10.getFirst().getCount() + 1);
            if (top10.getFirst().getCount() % 3 == 0) {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 3));
            } else if (top10.getFirst().getCount() % 4 == 0) {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 4));
            } else if (top10.getFirst().getCount() % 5 == 0) {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 5));
            } else if (top10.getFirst().getCount() % 7 == 0) {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 7));
            } else if (top10.getFirst().getCount() % 8 == 0) {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 8));
            } else if (top10.getFirst().getCount() % 9 == 0) {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 9));
            } else {
                artistAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 3));
            }
        }
        artistAmountAxis.setMinorTickVisible(true);
    }
    private void updateTopGenresChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        List<GenreCount> top10 = new ArrayList<>();
        List<GenreCount> allCounts = albumService.getCountByGenreAfter(cutoffDate, listenedMode.toString());
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        for (int i = 0; i < 10; i++) {
            if (i < allCounts.size()){
                top10.add(allCounts.get(i));
            }
        }
        if (genresBarChart.getData() != null) {
            genresBarChart.getData().clear();
        }
        for (GenreCount c : top10) {
            series.getData().add(new XYChart.Data<String, Integer>(c.getName(), Math.toIntExact(c.getCount())));
        }
        genresBarChart.getData().add(series);
        if (!top10.isEmpty()) {
            genreAmountAxis.setAutoRanging(false);
            genreAmountAxis.setLowerBound(0);
            genreAmountAxis.setUpperBound(top10.getFirst().getCount() + 1);
            if (top10.getFirst().getCount() % 3 == 0) {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 3));
            } else if (top10.getFirst().getCount() % 4 == 0) {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 4));
            } else if (top10.getFirst().getCount() % 5 == 0) {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 5));
            } else if (top10.getFirst().getCount() % 7 == 0) {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 7));
            } else if (top10.getFirst().getCount() % 8 == 0) {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 8));
            } else if (top10.getFirst().getCount() % 9 == 0) {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 9));
            } else {
                genreAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 3));
            }
        }
        genreAmountAxis.setMinorTickVisible(true);
    }
    private void updateTopTagsChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        List<TagCount> top10 = new ArrayList<>();
        List<TagCount> allCounts = albumService.getCountByTagAfter(cutoffDate, listenedMode.toString());
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        for (int i = 0; i < 10; i++) {
            if (i < allCounts.size()){
                top10.add(allCounts.get(i));
            }
        }
        if (tagsBarChart.getData() != null) {
            tagsBarChart.getData().clear();
        }
        for (TagCount c : top10) {
            series.getData().add(new XYChart.Data<String, Integer>(c.getName(), Math.toIntExact(c.getCount())));
        }
        tagsBarChart.getData().add(series);
        if (!top10.isEmpty()) {
            tagAmountAxis.setAutoRanging(false);
            tagAmountAxis.setLowerBound(0);
            tagAmountAxis.setUpperBound(top10.getFirst().getCount() + 1);
            if (top10.getFirst().getCount() % 3 == 0) {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 3));
            } else if (top10.getFirst().getCount() % 4 == 0) {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 4));
            } else if (top10.getFirst().getCount() % 5 == 0) {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 5));
            } else if (top10.getFirst().getCount() % 7 == 0) {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 7));
            } else if (top10.getFirst().getCount() % 8 == 0) {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 8));
            } else if (top10.getFirst().getCount() % 9 == 0) {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 9));
            } else {
                tagAmountAxis.setTickUnit(Math.round((double)top10.getFirst().getCount() / 3));
            }
        }
        tagAmountAxis.setMinorTickVisible(true);
    }
    private void updateDecadesBarChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        List<Album> albums = new ArrayList<>();
        List<LocalDate> albumDates = new ArrayList<>();
        if (listenedMode.equals(DbDataModeEnum.LISTENED)) {
            albums = albumService.selectByListenedDateAndListened(cutoffDate, DateModeEnum.AFTER.toString(), true);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED)) {
            albums = albumService.selectByAdditionDateAndListened(cutoffDate, DateModeEnum.AFTER.toString(), false);
        } else {
            albums = albumService.selectByAdditionDateAfter(cutoffDate);
        }
        for (Album album : albums) {
            albumDates.add(album.getReleaseDate());
        }
        if (decadesBarChart.getData() != null) {
            decadesBarChart.getData().clear();
        }
        decadesBarChart.getData().add(createDecadesChartData(albumDates));
        decadesAmountAxis.setAutoRanging(false);
        decadesAmountAxis.setLowerBound(0);
        decadesAmountAxis.setUpperBound(decadesChartMaxVal);
        if (decadesChartMaxVal % 3 == 0) {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 3));
        } else if (decadesChartMaxVal % 4 == 0) {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 4));
        } else if (decadesChartMaxVal % 5 == 0) {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 5));
        } else if (decadesChartMaxVal % 7 == 0) {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 7));
        } else if (decadesChartMaxVal % 8 == 0) {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 8));
        } else if (decadesChartMaxVal % 9 == 0) {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 9));
        } else {
            decadesAmountAxis.setTickUnit(Math.round((double)decadesChartMaxVal / 3));
        }
        decadesAmountAxis.setMinorTickVisible(true);
    }
    private XYChart.Series<String, Integer> createDecadesChartData (List<LocalDate> albumDates) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        int[] amountsArray = new int[10];
        Arrays.fill(amountsArray, 0);
        for (LocalDate date : albumDates) {
            if (date.isBefore(LocalDate.of(1940, 1, 1))) {
                amountsArray[9]++;
            } else if (date.isBefore(LocalDate.of(1950, 1, 1))
                && date.isAfter(LocalDate.of(1940, 1, 1))) {
                amountsArray[8]++;
            } else if (date.isBefore(LocalDate.of(1960, 1, 1))
                    && date.isAfter(LocalDate.of(1950, 1, 1))) {
                amountsArray[7]++;
            } else if (date.isBefore(LocalDate.of(1970, 1, 1))
                    && date.isAfter(LocalDate.of(1960, 1, 1))) {
                amountsArray[6]++;
            } else if (date.isBefore(LocalDate.of(1980, 1, 1))
                    && date.isAfter(LocalDate.of(1970, 1, 1))) {
                amountsArray[5]++;
            } else if (date.isBefore(LocalDate.of(1990, 1, 1))
                    && date.isAfter(LocalDate.of(1980, 1, 1))) {
                amountsArray[4]++;
            } else if (date.isBefore(LocalDate.of(2000, 1, 1))
                    && date.isAfter(LocalDate.of(1990, 1, 1))) {
                amountsArray[3]++;
            } else if (date.isBefore(LocalDate.of(2010, 1, 1))
                    && date.isAfter(LocalDate.of(2000, 1, 1))) {
                amountsArray[2]++;
            } else if (date.isBefore(LocalDate.of(2020, 1, 1))
                    && date.isAfter(LocalDate.of(2010, 1, 1))) {
                amountsArray[1]++;
            } else if (date.isAfter(LocalDate.of(2020, 1, 1))) {
                amountsArray[0]++;
            }
        }
        decadesChartMaxVal = Arrays.stream(amountsArray).max().getAsInt() + 1;
        series.getData().add(new XYChart.Data<String, Integer>("before 1940s", amountsArray[9]));
        int decadeNumber = 4;
        for (int i = 10; i > 4; i--) {
            series.getData().add(new XYChart.Data<String, Integer>("19" + decadeNumber + "0s", amountsArray[i-1]));
            decadeNumber++;
        }
        decadeNumber = 0;
        for (int i = 3; i > 0; i--) {
            series.getData().add(new XYChart.Data<String, Integer>("20" + decadeNumber + "0s", amountsArray[i-1]));
            decadeNumber++;
        }
        return series;
    }
    private void updateListenedLineChart (ChartTimeEnum mode) {
        LocalDate cutoffDate = parseDateMode(mode);
        List<Album> albumsListenedAfterDate = albumService.selectByListenedDateAfter(cutoffDate);
        List<LocalDate> albumListenedDates = new ArrayList<>();
        for (Album album : albumsListenedAfterDate) {
            albumListenedDates.add(album.getAdditionDate());
        }
        if (listenedToLineChart.getData() != null) {
            listenedToLineChart.getData().clear();
        }
        XYChart.Series<String, Integer> listenedSeries = createChartData(albumListenedDates, mode);
        listenedSeries.setName("Listened to");
        listenedToLineChart.getData().add(listenedSeries);
        List<Album> albumsAddedAfterDate = albumService.selectByAdditionDateAfter(cutoffDate);
        List<LocalDate> albumAddedDates = new ArrayList<>();
        for (Album album : albumsAddedAfterDate) {
            albumAddedDates.add(album.getAdditionDate());
        }
        XYChart.Series<String, Integer> addedSeries = createChartData(albumAddedDates, mode);
        addedSeries.setName("Added");
        listenedToLineChart.getData().add(addedSeries);
        listenedToAmountAxis.setAutoRanging(false);
        listenedToAmountAxis.setLowerBound(0);
        listenedToAmountAxis.setUpperBound(lineChartMaxVal);
        if (lineChartMaxVal % 3 == 0) {
            listenedToAmountAxis.setTickUnit(Math.round((double) lineChartMaxVal / 3));
        } else if (lineChartMaxVal % 4 == 0) {
            listenedToAmountAxis.setTickUnit(Math.round((double)lineChartMaxVal / 4));
        } else if (lineChartMaxVal % 5 == 0) {
            listenedToAmountAxis.setTickUnit(Math.round((double)lineChartMaxVal / 5));
        } else if (lineChartMaxVal % 7 == 0) {
            listenedToAmountAxis.setTickUnit(Math.round((double)lineChartMaxVal / 7));
        } else if (lineChartMaxVal % 8 == 0) {
            listenedToAmountAxis.setTickUnit(Math.round((double)lineChartMaxVal / 8));
        } else if (lineChartMaxVal % 9 == 0) {
            listenedToAmountAxis.setTickUnit(Math.round((double)lineChartMaxVal / 9));
        } else {
            listenedToAmountAxis.setTickUnit(Math.round((double) lineChartMaxVal / 3));
        }
        listenedToAmountAxis.setMinorTickVisible(true);
    }
    private XYChart.Series<String, Integer> createChartData(List<LocalDate> albumDates, ChartTimeEnum mode) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        if (mode.equals(ChartTimeEnum.LAST_WEEK)) {
            series = getLastWeekData(albumDates);
        } else if (mode.equals(ChartTimeEnum.LAST_MONTH)) {
            series = getLastMonthData(albumDates);
        } else if (mode.equals(ChartTimeEnum.LAST_QUARTER)) {
            series = getLastQuarterData(albumDates);
        } else if (mode.equals(ChartTimeEnum.LAST_YEAR)) {
            series = getLastYearData(albumDates);
        }
        return series;
    }
    private XYChart.Series<String, Integer> getLastWeekData(List<LocalDate> dates) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        int[] amountsArray = new int[7];
        Arrays.fill(amountsArray, 0);
        for (LocalDate date : dates) {
            if (date.equals(LocalDate.now().minusDays(6))) {
                amountsArray[6]++;
            } else if (date.equals(LocalDate.now().minusDays(5))) {
                amountsArray[5]++;
            } else if (date.equals(LocalDate.now().minusDays(4))) {
                amountsArray[4]++;
            } else if (date.equals(LocalDate.now().minusDays(3))) {
                amountsArray[3]++;
            } else if (date.equals(LocalDate.now().minusDays(2))) {
                amountsArray[2]++;
            } else if (date.equals(LocalDate.now().minusDays(1))) {
                amountsArray[1]++;
            } else if (date.equals(LocalDate.now())) {
                amountsArray[0]++;
            }
        }
        lineChartMaxVal = Arrays.stream(amountsArray).max().getAsInt() + 1;
        for (int i = 7; i > 0; i--) {
            series.getData().add(new XYChart.Data<String, Integer>(LocalDate.now().minusDays(i-1).toString(), amountsArray[i-1]));
        }
        return series;
    }
    private XYChart.Series<String, Integer> getLastMonthData(List<LocalDate> dates) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        int[] amountsArray = new int[4];
        Arrays.fill(amountsArray, 0);
        for (LocalDate date : dates) {
            if (date.isAfter(LocalDate.now().minusWeeks(4).plusDays(1))
                    && date.isBefore(LocalDate.now().minusWeeks(3))) {
                amountsArray[3]++;
            } else if (date.isAfter(LocalDate.now().minusWeeks(3).plusDays(1))
                    && date.isBefore(LocalDate.now().minusWeeks(2))) {
                amountsArray[2]++;
            } else if (date.isAfter(LocalDate.now().minusWeeks(2).plusDays(1))
                    && date.isBefore(LocalDate.now().minusWeeks(1))) {
                amountsArray[1]++;
            } else if (date.isAfter(LocalDate.now().minusWeeks(1).plusDays(1))
                    && date.isBefore(LocalDate.now().plusDays(1))) {
                amountsArray[0]++;
            }
        }
        lineChartMaxVal = Arrays.stream(amountsArray).max().getAsInt() + 1;
        for (int i = 4; i > 0; i--) {
            series.getData().add(new XYChart.Data<String, Integer>(
                    LocalDate.now().minusWeeks(i).plusDays(1).getDayOfMonth() + "/" +
                            LocalDate.now().minusWeeks(i).plusDays(1).getMonthValue() + "-" +
                            LocalDate.now().minusWeeks(i-1).getDayOfMonth() + "/" +
                            LocalDate.now().minusWeeks(i-1).getMonthValue(),
                    amountsArray[i-1]));
        }
        return series;
    }
    private XYChart.Series<String, Integer> getLastQuarterData(List<LocalDate> dates) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        int[] amountsArray = new int[3];
        Arrays.fill(amountsArray, 0);
        for (LocalDate date : dates) {
            if (date.isAfter(LocalDate.now().minusMonths(3).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(2))) {
                amountsArray[2]++;
            } else if (date.isAfter(LocalDate.now().minusMonths(2).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(1))) {
                amountsArray[1]++;
            } else if (date.isAfter(LocalDate.now().minusMonths(1).plusDays(1))
                    && date.isBefore(LocalDate.now().plusDays(1))) {
                amountsArray[0]++;
            }
        }
        lineChartMaxVal = Arrays.stream(amountsArray).max().getAsInt() + 1;
        for (int i = 3; i > 0; i--) {
            series.getData().add(new XYChart.Data<String, Integer>(
                    LocalDate.now().minusMonths(i).plusDays(1).getDayOfMonth() + "/" +
                            LocalDate.now().minusMonths(i).plusDays(1).getMonthValue() + "-" +
                            LocalDate.now().minusMonths(i-1).getDayOfMonth() + "/" +
                            LocalDate.now().minusMonths(i-1).getMonthValue(),
                    amountsArray[i-1]));
        }
        return series;
    }
    private XYChart.Series<String, Integer> getLastYearData(List<LocalDate> dates) {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        int[] amountsArray = new int[12];
        Arrays.fill(amountsArray, 0);
        for (LocalDate date : dates) {
            if (date.isAfter(LocalDate.now().minusMonths(12).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(11))) {
                amountsArray[11]++;
            } else if (date.isAfter(LocalDate.now().minusMonths(11).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(10))) {
                amountsArray[10]++;
            } else if (date.isAfter(LocalDate.now().minusMonths(10).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(9))) {
                amountsArray[9]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(9).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(8))) {
                amountsArray[8]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(8).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(7))) {
                amountsArray[7]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(7).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(6))) {
                amountsArray[6]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(6).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(5))) {
                amountsArray[5]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(5).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(4))) {
                amountsArray[4]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(4).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(3))) {
                amountsArray[3]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(3).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(2))) {
                amountsArray[2]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(2).plusDays(1))
                    && date.isBefore(LocalDate.now().minusMonths(1))) {
                amountsArray[1]++;
            }  else if (date.isAfter(LocalDate.now().minusMonths(1).plusDays(1))
                    && date.isBefore(LocalDate.now().plusDays(1))) {
                amountsArray[0]++;
            }
        }
        lineChartMaxVal = Arrays.stream(amountsArray).max().getAsInt() + 1;
        for (int i = 12; i > 0; i--) {
            series.getData().add(new XYChart.Data<String, Integer>(
                    LocalDate.now().minusMonths(i).plusDays(1).getDayOfMonth() + "/" +
                            LocalDate.now().minusMonths(i).plusDays(1).getMonthValue() + "-" +
                            LocalDate.now().minusMonths(i-1).getDayOfMonth() + "/" +
                            LocalDate.now().minusMonths(i-1).getMonthValue(),
                    amountsArray[i-1]));
        }
        return series;
    }
    private void updateListenedToPieChart (ChartTimeEnum mode) {
        LocalDate cutoffDate = parseDateMode(mode);
        int listenedAmount = albumService
                .selectByListenedDateAndListened(cutoffDate, DateModeEnum.AFTER.toString(), true).size();
        int notListenedAmount = albumService
                .selectByAdditionDateAndListened(cutoffDate, DateModeEnum.AFTER.toString(), false).size();
        int totalAmount = listenedAmount + notListenedAmount;
        String listenedPercentage = String.format("%.1f%%", (double)100*listenedAmount/totalAmount);
        String notListenedPercentage = String.format("%.1f%%", (double)100*notListenedAmount/totalAmount);
        ObservableList<PieChart.Data> listenedToPieData = FXCollections.observableArrayList(
                new PieChart.Data("listened to", listenedAmount),
                new PieChart.Data("not listened to", notListenedAmount)
        );
        listenedToPieData = removeZeroValues(listenedToPieData);
        listenedToPieChart.setTitle("Listened to: " + listenedPercentage + ", Not listened to: " + notListenedPercentage);
        listenedToPieChart.setData(listenedToPieData);
        listenedToPieData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), ": ", data.pieValueProperty().intValue()
                    )
            );
        });
    }
    private void updateOwnedPieChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        boolean listenedTo = true;
        boolean useAll = false;
        if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED)) {
            listenedTo = false;
        } else if (listenedMode.equals(DbDataModeEnum.ALL)) {
            useAll = true;
        }
        int ownedAmount = albumService
                .selectByOwnedAndListenedToAndAfterDate(true, cutoffDate, listenedTo).size();
        int notOwnedAmount = albumService
                .selectByOwnedAndListenedToAndAfterDate(false, cutoffDate, listenedTo).size();
        if (useAll) {
            ownedAmount += albumService
                    .selectByOwnedAndListenedToAndAfterDate(true, cutoffDate, false).size();
            notOwnedAmount += albumService
                    .selectByOwnedAndListenedToAndAfterDate(false, cutoffDate, false).size();
        }
        ObservableList<PieChart.Data> ownedPieData = FXCollections.observableArrayList(
                new PieChart.Data("Owned", ownedAmount),
                new PieChart.Data("Not owned", notOwnedAmount)
        );
        ownedPieData = removeZeroValues(ownedPieData);
        ownedPieData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), ": ", data.pieValueProperty().intValue()
                    )
            );
        });
        ownedPieChart.setData(ownedPieData);
    }
    private void updateTypePieChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        boolean listenedTo = true;
        boolean useAll = false;
        if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED)) {
            listenedTo = false;
        } else if (listenedMode.equals(DbDataModeEnum.ALL)) {
            useAll = true;
        }
        int albumAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.ALBUM.toString(), cutoffDate, listenedTo).size();
        int singleAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.SINGLE.toString(), cutoffDate, listenedTo).size();
        int epAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.EP.toString(), cutoffDate, listenedTo).size();
        int compAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.COMPILATION.toString(), cutoffDate, listenedTo).size();
        int soundtrackAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.SOUNDTRACK.toString(), cutoffDate, listenedTo).size();
        int mixtapeAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.MIXTAPE.toString(), cutoffDate, listenedTo).size();
        int demoAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.DEMO.toString(), cutoffDate, listenedTo).size();
        int bootlegAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.BOOTLEG.toString(), cutoffDate, listenedTo).size();
        int otherAmount = albumService
                .selectByTypeAndListenedToAndAfterDate(TypeEnum.OTHER.toString(), cutoffDate, listenedTo).size();
        if (useAll) {
            albumAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.ALBUM.toString(), cutoffDate, false).size();
            singleAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.SINGLE.toString(), cutoffDate, false).size();
            epAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.EP.toString(), cutoffDate, false).size();
            compAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.COMPILATION.toString(), cutoffDate, false).size();
            soundtrackAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.SOUNDTRACK.toString(), cutoffDate, false).size();
            mixtapeAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.MIXTAPE.toString(), cutoffDate, false).size();
            demoAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.DEMO.toString(), cutoffDate, false).size();
            bootlegAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.BOOTLEG.toString(), cutoffDate, false).size();
            otherAmount += albumService
                    .selectByTypeAndListenedToAndAfterDate(TypeEnum.OTHER.toString(), cutoffDate, false).size();
        }
        ObservableList<PieChart.Data> typePieData = FXCollections.observableArrayList(
                new PieChart.Data("Album", albumAmount),
                new PieChart.Data("Single", singleAmount),
                new PieChart.Data("EP", epAmount),
                new PieChart.Data("Compilation", compAmount),
                new PieChart.Data("Soundtrack", soundtrackAmount),
                new PieChart.Data("Mixtape", mixtapeAmount),
                new PieChart.Data("Demo", demoAmount),
                new PieChart.Data("Bootleg", bootlegAmount),
                new PieChart.Data("Other", otherAmount)
        );
        typePieData = removeZeroValues(typePieData);
        typePieData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), ": ", data.pieValueProperty().intValue()
                    )
            );
        });
        typePieChart.setData(typePieData);
    }
    private void updateFormatPieChart (ChartTimeEnum timeMode, DbDataModeEnum listenedMode) {
        LocalDate cutoffDate = parseDateMode(timeMode);
        boolean listenedTo = true;
        boolean useAll = false;
        if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED)) {
            listenedTo = false;
        } else if (listenedMode.equals(DbDataModeEnum.ALL)) {
            useAll = true;
        }
        int cdAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.CD.toString(), cutoffDate, listenedTo).size();
        int vinylAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.VINYL.toString(), cutoffDate, listenedTo).size();
        int digitalAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.DIGITAL_MEDIA.toString(), cutoffDate, listenedTo).size();
        int cassetteAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.CASSETTE.toString(), cutoffDate, listenedTo).size();
        int sacdAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.SACD.toString(), cutoffDate, listenedTo).size();
        int shellacAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.SHELLAC.toString(), cutoffDate, listenedTo).size();
        int minidiscAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.MINIDISC.toString(), cutoffDate, listenedTo).size();
        int reelAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.REEL_TO_REEL.toString(), cutoffDate, listenedTo).size();
        int otherAmount = albumService
                .selectByFormatAndListenedToAndAfterDate(FormatEnum.OTHER.toString(), cutoffDate, listenedTo).size();
        if (useAll) {
            cdAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.CD.toString(), cutoffDate, false).size();
            vinylAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.VINYL.toString(), cutoffDate, false).size();
            digitalAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.DIGITAL_MEDIA.toString(), cutoffDate, false).size();
            cassetteAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.CASSETTE.toString(), cutoffDate, false).size();
            sacdAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.SACD.toString(), cutoffDate, false).size();
            shellacAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.SHELLAC.toString(), cutoffDate, false).size();
            minidiscAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.MINIDISC.toString(), cutoffDate, false).size();
            reelAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.REEL_TO_REEL.toString(), cutoffDate, false).size();
            otherAmount += albumService
                    .selectByFormatAndListenedToAndAfterDate(FormatEnum.OTHER.toString(), cutoffDate, false).size();
        }
        ObservableList<PieChart.Data> formatPieData = FXCollections.observableArrayList(
                new PieChart.Data("CD", cdAmount),
                new PieChart.Data("Vinyl", vinylAmount),
                new PieChart.Data("Digital", digitalAmount),
                new PieChart.Data("Cassette", cassetteAmount),
                new PieChart.Data("SACD", sacdAmount),
                new PieChart.Data("Shellac", shellacAmount),
                new PieChart.Data("MiniDisc", minidiscAmount),
                new PieChart.Data("Reel", reelAmount),
                new PieChart.Data("Other", otherAmount)
        );
        formatPieData = removeZeroValues(formatPieData);
        formatPieData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), ": ", data.pieValueProperty().intValue()
                    )
            );
        });
        formatPieChart.setData(formatPieData);
    }
    private LocalDate parseDateMode (ChartTimeEnum mode) {
        LocalDate cutoffDate = LocalDate.now();
        if (mode.equals(ChartTimeEnum.LAST_WEEK)) {
            cutoffDate = cutoffDate.minusWeeks(1);
        } else if (mode.equals(ChartTimeEnum.LAST_MONTH)) {
            cutoffDate = cutoffDate.minusMonths(1);
        } else if (mode.equals(ChartTimeEnum.LAST_QUARTER)) {
            cutoffDate = cutoffDate.minusMonths(3);
        } else if (mode.equals(ChartTimeEnum.LAST_YEAR)) {
            cutoffDate = cutoffDate.minusYears(1);
        } else {
            cutoffDate = LocalDate.MIN;
        }
        return cutoffDate;
    }
    private ObservableList<PieChart.Data> removeZeroValues(ObservableList<PieChart.Data> data) {
        ObservableList<PieChart.Data> newData = FXCollections.observableArrayList();
        for (PieChart.Data typePieDatum : data) {
            if (typePieDatum.getPieValue() != 0) {
                newData.add(typePieDatum);
            }
        }
        return newData;
    }
}
