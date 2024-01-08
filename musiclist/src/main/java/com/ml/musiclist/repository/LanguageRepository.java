package com.ml.musiclist.repository;

import com.ml.musiclist.entity.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
    Language findFirstByLanguage(String language);
}
