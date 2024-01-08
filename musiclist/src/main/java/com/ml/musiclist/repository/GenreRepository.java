package com.ml.musiclist.repository;

import com.ml.musiclist.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findFirstByName(String name);
}
