package com.ml.musiclist.repository;

import com.ml.musiclist.dbcount.ArtistCount;
import com.ml.musiclist.dbcount.GenreCount;
import com.ml.musiclist.dbcount.TagCount;
import com.ml.musiclist.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    public Album getAlbumById(long Id);
    public List<Album> findAlbumsByListenedTo(boolean listenedTo);
    public List<Album> findAlbumsByListenedToIsNull();
    public List<Album> findAlbumsByAdditionDateBefore(LocalDate date);
    public List<Album> findAlbumsByAdditionDateBeforeAndListenedTo(LocalDate date, boolean listenedTo);
    public List<Album> findAlbumsByAdditionDateAfter(LocalDate date);
    public List<Album> findAlbumsByAdditionDateAfterAndListenedTo(LocalDate date, boolean listenedTo);
    @Query ("SELECT alb FROM Album alb" +
            " JOIN alb.artists art JOIN art.albums album " +
            "WHERE art.name LIKE %:artist% AND alb.listenedTo = :listened")
    public List<Album> findAlbumsByArtistLikeAndListenedTo(@Param("artist") String artist, @Param("listened") boolean listened);
    @Query ("SELECT alb FROM Album alb" +
            " JOIN alb.genres gen JOIN gen.albums album " +
            "WHERE gen.name LIKE %:genre% AND alb.listenedTo = :listened")
    public List<Album> findAlbumsByGenreLikeAndListenedTo(@Param("genre") String genre, @Param("listened") boolean listened);
    @Query ("SELECT alb FROM Album alb" +
            " JOIN alb.languages lan JOIN lan.albums album " +
            "WHERE lan.language LIKE %:lang% AND alb.listenedTo = :listened")
    public List<Album> findAlbumsByLanguageLikeAndListenedTo(@Param("lang") String lang, @Param("listened") boolean listened);
    @Query ("SELECT alb FROM Album alb" +
            " JOIN alb.tags tg JOIN tg.albums album " +
            "WHERE tg.tag LIKE %:tag% AND alb.listenedTo = :listened")
    public List<Album> findAlbumsByTagLikeAndListenedTo(@Param("tag") String tag, @Param("listened") boolean listened);
    public List<Album> findAlbumsByFormat(String format);
    public List<Album> findAlbumsByFormatAndListenedTo(String format, boolean listenedTo);
    public List<Album> findAlbumsByType(String type);
    public List<Album> findAlbumsByTypeAndListenedTo(String type, boolean listenedTo);
    public List<Album> findAlbumsByLengthLessThan(int length);
    public List<Album> findAlbumsByLengthLessThanAndListenedTo(int length, boolean listenedTo);
    public List<Album> findAlbumsByLengthGreaterThan(int length);
    public List<Album> findAlbumsByLengthGreaterThanAndListenedTo(int length, boolean listenedTo);
    public List<Album> findAlbumsByOwned(boolean owned);
    public List<Album> findAlbumsByOwnedAndListenedTo(boolean owned, boolean listenedTo);
    public List<Album> findAlbumsByTitleContaining(String title);
    public List<Album> findAlbumsByTitleContainingAndListenedTo(String title, boolean listenedTo);
    public List<Album> findAlbumsByReleaseDateBefore(LocalDate date);
    public List<Album> findAlbumsByReleaseDateBeforeAndListenedTo(LocalDate date, boolean listenedTo);
    public List<Album> findAlbumsByReleaseDateAfter(LocalDate date);
    public List<Album> findAlbumsByReleaseDateAfterAndListenedTo(LocalDate date, boolean listenedTo);
    public List<Album> findAlbumsByListenedDateBefore(LocalDate date);
    public List<Album> findAlbumsByListenedDateBeforeAndListenedTo(LocalDate date, boolean listenedTo);
    public List<Album> findAlbumsByListenedDateAfter(LocalDate date);
    public List<Album> findAlbumsByListenedDateAfterAndListenedTo(LocalDate date, boolean listenedTo);
    public List<Album> findAlbumsByFormatAndListenedToFalseAndAdditionDateAfter(String format, LocalDate date);
    public List<Album> findAlbumsByFormatAndListenedToTrueAndListenedDateAfter(String format, LocalDate date);
    public List<Album> findAlbumsByTypeAndListenedToFalseAndAdditionDateAfter(String format, LocalDate date);
    public List<Album> findAlbumsByTypeAndListenedToTrueAndListenedDateAfter(String format, LocalDate date);
    public List<Album> findAlbumsByOwnedAndListenedToTrueAndListenedDateAfter(boolean owned, LocalDate date);
    public List<Album> findAlbumsByOwnedAndListenedToFalseAndAdditionDateAfter(boolean owned, LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.ArtistCount(art.name, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.artists art" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date" +
            " GROUP BY art.name")
    public List<ArtistCount> getCountByArtistIfListenedDateAfter(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.ArtistCount(art.name, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.artists art" +
            " WHERE alb.additionDate > :date" +
            " GROUP BY art.name")
    public List<ArtistCount> getCountByArtistIfAdditionDateAfter(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.ArtistCount(art.name, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.artists art" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date" +
            " GROUP BY art.name")
    public List<ArtistCount> getCountByArtistIfAdditionDateAfterAndNotListened(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.GenreCount(gen.name, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.genres gen" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date" +
            " GROUP BY gen.name")
    public List<GenreCount> getCountByGenreIfListenedDateAfter(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.GenreCount(gen.name, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.genres gen" +
            " WHERE alb.additionDate > :date" +
            " GROUP BY gen.name")
    public List<GenreCount> getCountByGenreIfAdditionDateAfter(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.GenreCount(gen.name, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.genres gen" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date" +
            " GROUP BY gen.name")
    public List<GenreCount> getCountByGenreIfAdditionDateAfterAndNotListened(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.TagCount(tg.tag, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.tags tg" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date" +
            " GROUP BY tg.tag")
    public List<TagCount> getCountByTagIfListenedDateAfter(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.TagCount(tg.tag, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.tags tg" +
            " WHERE alb.additionDate > :date" +
            " GROUP BY tg.tag")
    public List<TagCount> getCountByTagIfAdditionDateAfter(@Param("date") LocalDate date);
    @Query ("SELECT new com.ml.musiclist.dbcount.TagCount(tg.tag, count(alb) ) " +
            "FROM Album alb" +
            " JOIN alb.tags tg" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date" +
            " GROUP BY tg.tag")
    public List<TagCount> getCountByTagIfAdditionDateAfterAndNotListened(@Param("date") LocalDate date);
    public Album findTopByListenedToFalseAndAdditionDateAfterOrderByLengthDesc(LocalDate date);
    public Album findTopByListenedDateAfterOrderByLengthDesc(LocalDate date);
    public Album findTopByAdditionDateAfterOrderByLengthDesc(LocalDate date);
    public Album findTopByListenedToFalseAndAdditionDateAfterOrderByLengthAsc(LocalDate date);
    public Album findTopByListenedDateAfterOrderByLengthAsc(LocalDate date);
    public Album findTopByAdditionDateAfterOrderByLengthAsc(LocalDate date);
    @Query(value = "SELECT avg(alb.length)" +
            "FROM Album alb" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date")
    public Double findAvgLengthListenedToFalseAndAdditionDateAfter(@Param("date") LocalDate date);
    @Query(value = "SELECT avg(alb.length)" +
            "FROM Album alb" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date")
    public Double findAvgLengthListenedDateAfter(@Param("date") LocalDate date);
    @Query(value = "SELECT avg(alb.length)" +
            "FROM Album alb" +
            " WHERE alb.additionDate > :date")
    public Double findAvgLengthAdditionDateAfter(@Param("date") LocalDate date);
}
