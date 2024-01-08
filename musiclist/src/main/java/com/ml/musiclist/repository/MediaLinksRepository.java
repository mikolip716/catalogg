package com.ml.musiclist.repository;

import com.ml.musiclist.entity.MediaLinks;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaLinksRepository extends CrudRepository<MediaLinks, Long> {
    MediaLinks findFirstByLinkAndSiteName (String link, String siteName);
    List<MediaLinks> findAllByAlbum_Id (long albumId);
}
