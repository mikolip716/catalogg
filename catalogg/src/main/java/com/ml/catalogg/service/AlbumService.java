package com.ml.catalogg.service;

import com.ml.catalogg.dbcount.ArtistCount;
import com.ml.catalogg.dbcount.GenreCount;
import com.ml.catalogg.dbcount.TagCount;
import com.ml.catalogg.entity.*;
import com.ml.catalogg.enums.DateModeEnum;
import com.ml.catalogg.enums.DbDataModeEnum;
import com.ml.catalogg.enums.LengthModeEnum;
import com.ml.catalogg.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private MediaLinksRepository mediaLinksRepository;
    @Autowired
    private SongsRepository songsRepository;
    @Autowired
    private TagsRepository tagsRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void changeAlbumListenedState(long albumId, boolean state) {
        Album album = albumRepository.getAlbumById(albumId);
        album.setListenedTo(state);
        if (state) {
            album.setListenedDate(LocalDate.now());
        } else {
            album.setListenedDate(null);
        }
        albumRepository.saveAndFlush(album);
    }
    @Transactional
    public void saveAlbum (Album album) {
        for (Artist artist : album.getArtists()) {
            Artist tempArtist = artistRepository.findFirstByName(artist.getName());
            if (tempArtist == null) {
                artistRepository.save(artist);
            }
            else {
                List<Artist> newArtists = album.getArtists();
                int index = newArtists.indexOf(artist);
                newArtists.set(index, tempArtist);
                album.setArtists(newArtists);
            }
        }
        for (Genre genre : album.getGenres()) {
            Genre tempGenre = genreRepository.findFirstByName(genre.getName());
            if (tempGenre == null) {
                genreRepository.save(genre);
            }
            else {
                List<Genre> newGenres = album.getGenres();
                int index = newGenres.indexOf(genre);
                newGenres.set(index, tempGenre);
                album.setGenres(newGenres);
            }
        }
        for (Language language : album.getLanguages()) {
            Language tempLang = languageRepository.findFirstByLanguage(language.getLanguage());
            if (tempLang == null) {
                languageRepository.save(language);
            }
            else {
                List<Language> newLanguages = album.getLanguages();
                int index = newLanguages.indexOf(language);
                newLanguages.set(index, tempLang);
                album.setLanguages(newLanguages);
            }
        }
        albumRepository.save(album);
        for (MediaLinks mediaLink : album.getMediaLinks()) {
            MediaLinks tempMediaLink = mediaLinksRepository.findFirstByLinkAndSiteName(mediaLink.getLink(), mediaLink.getSiteName());
            mediaLink.setAlbum(album);
            if (tempMediaLink == null) {
                mediaLinksRepository.save(mediaLink);
                List<MediaLinks> newMediaLink = album.getMediaLinks();
                newMediaLink.add(mediaLink);
                album.setMediaLinks(newMediaLink);
            }
        }
        for (Songs song : album.getSongs()) {
            Songs tempSong = songsRepository.findFirstByTitleAndNumberAndLength(song.getTitle(), song.getNumber(), song.getLength());
            song.setAlbum(album);
            if (tempSong == null) {
                songsRepository.save(song);
                List<Songs> newSongs = album.getSongs();
                newSongs.add(song);
                album.setSongs(newSongs);
            }
        }
        for (Tags tag : album.getTags()) {
            Tags tempTag = tagsRepository.findFirstByTag(tag.getTag());
            if (tempTag == null) {
                tagsRepository.save(tag);
            }
            else {
                List<Tags> newTags = album.getTags();
                int index = newTags.indexOf(tag);
                newTags.set(index, tempTag);
                album.setTags(newTags);
            }
        }
        albumRepository.saveAndFlush(album);
    }
    @Transactional
    public void updateAlbum(Album album) {
        //clear all old data to avoid duplicates
        mediaLinksRepository.deleteAll(mediaLinksRepository.findAllByAlbum_Id(album.getId()));
        songsRepository.deleteAll(songsRepository.findAllByAlbum_Id(album.getId()));
        entityManager.flush();
        saveAlbum(album);
    }
    @Transactional
    public void deleteAlbum(long albumId) {
        albumRepository.delete(albumRepository.getAlbumById(albumId));
    }
    @Transactional
    public List<Album> selectAll() {
        List<Album> resultSet = albumRepository.findAll();
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByListenedTo(boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByListenedTo(listenedTo);
        resultSet.addAll(albumRepository
                .findAlbumsByListenedToIsNull());
        return initializeAlbums(resultSet);
    }
    @Transactional
    public Album selectById(long albumId) {
        Album album = albumRepository
                .getAlbumById(albumId);
        Hibernate.initialize(album.getArtists());
        Hibernate.initialize(album.getGenres());
        Hibernate.initialize(album.getLanguages());
        Hibernate.initialize(album.getMediaLinks());
        Hibernate.initialize(album.getSongs());
        Hibernate.initialize(album.getTags());
        return album;
    }
    @Transactional
    public List<Album> selectByAdditionDateAndListened(LocalDate date, String mode, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (mode.equals(DateModeEnum.BEFORE.toString())){
            resultSet.addAll(albumRepository
                    .findAlbumsByAdditionDateBeforeAndListenedTo(date, listenedTo));
        } else if (mode.equals(DateModeEnum.AFTER.toString())) {
            resultSet.addAll(albumRepository
                    .findAlbumsByAdditionDateAfterAndListenedTo(date, listenedTo));
        }
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByAdditionDateAfter(LocalDate date) {
        List<Album> resultSet = albumRepository.findAlbumsByAdditionDateAfter(date);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByListenedDateAfter(LocalDate date) {
        List<Album> resultSet = albumRepository.findAlbumsByListenedDateAfter(date);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByListenedDateAndListened(LocalDate date, String mode, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (mode.equals(DateModeEnum.BEFORE.toString())){
            resultSet.addAll(albumRepository
                    .findAlbumsByListenedDateBeforeAndListenedTo(date, listenedTo));
        } else if (mode.equals(DateModeEnum.AFTER.toString())) {
            resultSet.addAll(albumRepository
                    .findAlbumsByListenedDateAfterAndListenedTo(date, listenedTo));
        }
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByTypeAndListenedToAndAfterDate(String type, LocalDate date, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (listenedTo){
            resultSet.addAll(albumRepository
                    .findAlbumsByTypeAndListenedToTrueAndListenedDateAfter(type, date));
        } else {
            resultSet.addAll(albumRepository
                    .findAlbumsByTypeAndListenedToFalseAndAdditionDateAfter(type, date));
        }
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByOwnedAndListenedToAndAfterDate(boolean owned, LocalDate date, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (listenedTo){
            resultSet.addAll(albumRepository
                    .findAlbumsByOwnedAndListenedToTrueAndListenedDateAfter(owned, date));
        } else {
            resultSet.addAll(albumRepository
                    .findAlbumsByOwnedAndListenedToFalseAndAdditionDateAfter(owned, date));
        }
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByFormatAndListenedToAndAfterDate(String format, LocalDate date, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (listenedTo){
            resultSet.addAll(albumRepository
                    .findAlbumsByFormatAndListenedToTrueAndListenedDateAfter(format, date));
        } else {
            resultSet.addAll(albumRepository
                    .findAlbumsByFormatAndListenedToFalseAndAdditionDateAfter(format, date));
        }
        return initializeAlbums(resultSet);
    }

    private List<Album> initializeAlbums(List<Album> resultSet) {
        for (Album album : resultSet) {
            Hibernate.initialize(album.getArtists());
            Hibernate.initialize(album.getGenres());
            Hibernate.initialize(album.getLanguages());
            Hibernate.initialize(album.getMediaLinks());
            Hibernate.initialize(album.getSongs());
            Hibernate.initialize(album.getTags());
        }
        return resultSet;
    }
    @Transactional
    public List<Album> selectByFormatAndListened(String format, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByFormatAndListenedTo(format, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByLengthAndListened(int length, String mode, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (mode.equals(LengthModeEnum.OVER.toString())){
            resultSet.addAll(albumRepository
                    .findAlbumsByLengthGreaterThanAndListenedTo(length, listenedTo));
        } else if (mode.equals(LengthModeEnum.UNDER.toString())) {
            resultSet.addAll(albumRepository
                    .findAlbumsByLengthLessThanAndListenedTo(length, listenedTo));
        }
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByOwnedAndListened(boolean owned, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByOwnedAndListenedTo(owned, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByReleaseDateAndListened(LocalDate date, String mode, boolean listenedTo) {
        List<Album> resultSet = new ArrayList<>();
        if (mode.equals(DateModeEnum.BEFORE.toString())){
            resultSet.addAll(albumRepository
                    .findAlbumsByReleaseDateBeforeAndListenedTo(date, listenedTo));
        } else if (mode.equals(DateModeEnum.AFTER.toString())) {
            resultSet.addAll(albumRepository
                    .findAlbumsByReleaseDateAfterAndListenedTo(date, listenedTo));
        }
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByTitleAndListened(String title, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByTitleContainingAndListenedTo(title, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByArtistAndListened(String artist, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByArtistLikeAndListenedTo(artist, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByGenreAndListened(String genre, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByGenreLikeAndListenedTo(genre, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByLanguageAndListened(String lang, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByLanguageLikeAndListenedTo(lang, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByTagAndListened(String tag, boolean listenedTo) {
        List<Album> resultSet = albumRepository
                .findAlbumsByTagLikeAndListenedTo(tag, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<Album> selectByTypeAndListened(String type, boolean listenedTo) {
        List<Album> resultSet = albumRepository.findAlbumsByTypeAndListenedTo(type, listenedTo);
        return initializeAlbums(resultSet);
    }
    @Transactional
    public List<ArtistCount> getCountByArtistAfter(LocalDate date, String mode) {
        List<ArtistCount> resultSet = new ArrayList<>();
        if (mode.equals(DbDataModeEnum.LISTENED.toString())){
            resultSet.addAll(albumRepository
                    .getCountByArtistIfListenedDateAfter(date));
        } else if (mode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            resultSet.addAll(albumRepository
                    .getCountByArtistIfAdditionDateAfterAndNotListened(date));
        } else {
            resultSet.addAll(albumRepository.getCountByArtistIfAdditionDateAfter(date));
        }
        resultSet.sort(ArtistCount::CompareTo);
        return  resultSet;
    }
    @Transactional
    public List<GenreCount> getCountByGenreAfter(LocalDate date, String mode) {
        List<GenreCount> resultSet = new ArrayList<>();
        if (mode.equals(DbDataModeEnum.LISTENED.toString())){
            resultSet.addAll(albumRepository
                    .getCountByGenreIfListenedDateAfter(date));
        } else if (mode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            resultSet.addAll(albumRepository
                    .getCountByGenreIfAdditionDateAfterAndNotListened(date));
        } else {
            resultSet.addAll(albumRepository.getCountByGenreIfAdditionDateAfter(date));
        }
        resultSet.sort(GenreCount::CompareTo);
        return  resultSet;
    }
    @Transactional
    public List<TagCount> getCountByTagAfter(LocalDate date, String mode) {
        List<TagCount> resultSet = new ArrayList<>();
        if (mode.equals(DbDataModeEnum.LISTENED.toString())){
            resultSet.addAll(albumRepository
                    .getCountByTagIfListenedDateAfter(date));
        } else if (mode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            resultSet.addAll(albumRepository
                    .getCountByTagIfAdditionDateAfterAndNotListened(date));
        } else {
            resultSet.addAll(albumRepository.getCountByTagIfAdditionDateAfter(date));
        }
        resultSet.sort(TagCount::CompareTo);
        return  resultSet;
    }
    @Transactional
    public Album getShortest(LocalDate date, String listenedMode) {
        Album result = new Album();
        if (listenedMode.equals(DbDataModeEnum.LISTENED.toString())){
            result = albumRepository.findTopByListenedDateAfterOrderByLengthAsc(date);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            result = albumRepository.findTopByListenedToFalseAndAdditionDateAfterOrderByLengthAsc(date);
        } else {
            result = albumRepository.findTopByAdditionDateAfterOrderByLengthAsc(date);
        }
        return  result;
    }
    @Transactional
    public Album getLongest(LocalDate date, String listenedMode) {
        Album result = new Album();
        if (listenedMode.equals(DbDataModeEnum.LISTENED.toString())){
            result = albumRepository.findTopByListenedDateAfterOrderByLengthDesc(date);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            result = albumRepository.findTopByListenedToFalseAndAdditionDateAfterOrderByLengthDesc(date);
        } else {
            result = albumRepository.findTopByAdditionDateAfterOrderByLengthDesc(date);
        }
        return  result;
    }
    @Transactional
    public Double getAverage(LocalDate date, String listenedMode) {
        Double result;
        if (listenedMode.equals(DbDataModeEnum.LISTENED.toString())){
            result = albumRepository.findAvgLengthListenedDateAfter(date);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            result = albumRepository.findAvgLengthListenedToFalseAndAdditionDateAfter(date);
        } else {
            result = albumRepository.findAvgLengthAdditionDateAfter(date);
        }
        return  result;
    }
}
