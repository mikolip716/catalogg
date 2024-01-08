package com.ml.musiclist.service;

import com.ml.musiclist.entity.Songs;
import com.ml.musiclist.enums.DbDataModeEnum;
import com.ml.musiclist.repository.SongsRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SongsService {
    @Autowired
    private SongsRepository songsRepository;
    @Transactional
    public List<Songs> selectAllSorted() {
        List<Songs> allSongs = (List<Songs>)songsRepository.findAll();
        allSongs.sort(Comparator.comparing(Songs::getLength));
        return allSongs;
    }
    @Transactional
    public Songs getShortest() {
        Songs result = songsRepository.findTopByOrderByLengthAsc();
        Hibernate.initialize(result.getAlbum());
        return result;
    }
    @Transactional
    public Songs getLongest() {
        Songs result = songsRepository.findTopByOrderByLengthDesc();
        Hibernate.initialize(result.getAlbum());
        return result;
    }
    @Transactional
    public Double getAvg() {
        return songsRepository.findAvg();
    }
    @Transactional
    public Songs getShortest(LocalDate date, String listenedMode) {
        Songs result = new Songs();
        List<Songs> temp = new ArrayList<>();
        if (listenedMode.equals(DbDataModeEnum.LISTENED.toString())){
            temp = songsRepository.findFirstByLengthAscAfterListenedDate(date);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            temp = songsRepository.findFirstByLengthAscAfterAddDateListenedToFalse(date);
        } else {
            temp = songsRepository.findFirstByLengthAscAfterAddDate(date);
        }
        if (!temp.isEmpty()) {
            result = temp.getFirst();
            Hibernate.initialize(result.getAlbum());
        }
        return result;
    }
    @Transactional
    public Songs getLongest(LocalDate date, String listenedMode) {
        Songs result = new Songs();
        List<Songs> temp = new ArrayList<>();
        if (listenedMode.equals(DbDataModeEnum.LISTENED.toString())){
            temp = songsRepository.findFirstByLengthDescAfterListenedDate(date);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            temp = songsRepository.findFirstByLengthDescAfterAddDateListenedToFalse(date);
        } else {
            temp = songsRepository.findFirstByLengthDescAfterAddDate(date);
        }
        if (!temp.isEmpty()) {
            result = temp.getFirst();
            Hibernate.initialize(result.getAlbum());
        }
        return result;
    }
    @Transactional
    public Double getAverage(LocalDate date, String listenedMode) {
        Double result;
        if (listenedMode.equals(DbDataModeEnum.LISTENED.toString())){
            result = songsRepository.findAvgAfterListenedDate(date);
        } else if (listenedMode.equals(DbDataModeEnum.NOT_LISTENED.toString())) {
            result = songsRepository.findAvgAfterAddDateListenedToFalse(date);
        } else {
            result = songsRepository.findAvgAfterAddDate(date);
        }
        return  result;
    }
}
