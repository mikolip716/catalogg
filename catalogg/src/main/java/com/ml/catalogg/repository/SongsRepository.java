package com.ml.catalogg.repository;

import com.ml.catalogg.entity.Songs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SongsRepository extends CrudRepository<Songs, Long> {
    public Songs findFirstByTitleAndNumberAndLength (String title, String number, int length);
    public List<Songs> findAllByAlbum_Id (long albumId);
    public Songs findTopByOrderByLengthAsc();
    public Songs findTopByOrderByLengthDesc();
    @Query(value = "SELECT avg(s.length) FROM Songs s")
    public Double findAvg();
    @Query("SELECT s FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date" +
            " ORDER BY s.length ASC")
    public List<Songs> findFirstByLengthAscAfterListenedDate(@Param("date")LocalDate date);
    @Query("SELECT s FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date" +
            " ORDER BY s.length ASC")
    public List<Songs> findFirstByLengthAscAfterAddDateListenedToFalse(@Param("date")LocalDate date);
    @Query("SELECT s FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.additionDate > :date" +
            " ORDER BY s.length ASC")
    public List<Songs> findFirstByLengthAscAfterAddDate(@Param("date")LocalDate date);
    @Query("SELECT s FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date" +
            " ORDER BY s.length DESC")
    public List<Songs> findFirstByLengthDescAfterListenedDate(@Param("date")LocalDate date);
    @Query("SELECT s FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date" +
            " ORDER BY s.length DESC")
    public List<Songs> findFirstByLengthDescAfterAddDateListenedToFalse(@Param("date")LocalDate date);
    @Query("SELECT s FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.additionDate > :date" +
            " ORDER BY s.length DESC")
    public List<Songs> findFirstByLengthDescAfterAddDate(@Param("date")LocalDate date);
    @Query(value = "SELECT avg(s.length) FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.listenedTo = true AND alb.listenedDate > :date")
    public Double findAvgAfterListenedDate(@Param("date")LocalDate date);
    @Query(value = "SELECT avg(s.length) FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.additionDate > :date")
    public Double findAvgAfterAddDate(@Param("date")LocalDate date);
    @Query(value = "SELECT avg(s.length) FROM Songs s" +
            " JOIN s.album alb" +
            " WHERE alb.listenedTo = false AND alb.additionDate > :date")
    public Double findAvgAfterAddDateListenedToFalse(@Param("date")LocalDate date);
}
