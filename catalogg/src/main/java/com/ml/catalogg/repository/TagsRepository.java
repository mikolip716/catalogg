package com.ml.catalogg.repository;

import com.ml.catalogg.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long> {
    Tags findFirstByTag (String tag);
}
