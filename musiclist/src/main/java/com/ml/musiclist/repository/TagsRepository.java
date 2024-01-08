package com.ml.musiclist.repository;

import com.ml.musiclist.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
    Tags findFirstByTag (String tag);
}
