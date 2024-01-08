package com.ml.musiclist.repository;

import com.ml.musiclist.entity.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {
    Artist findFirstByName(String name);
}
