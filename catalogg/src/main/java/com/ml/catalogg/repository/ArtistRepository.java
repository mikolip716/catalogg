package com.ml.catalogg.repository;

import com.ml.catalogg.entity.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {
    Artist findFirstByName(String name);
}
