package com.ml.musiclist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tags")
public class Tags {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(
            name = "tag_id"
    )
    private Long id;
    private String tag;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },
            targetEntity = Album.class)
    @JoinTable(name = "album_tags",
            inverseJoinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            joinColumns = @JoinColumn(name = "tag_id"))
    private List<Album> albums;
}
