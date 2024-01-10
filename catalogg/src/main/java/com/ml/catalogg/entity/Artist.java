package com.ml.catalogg.entity;

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
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "atrist_id")
    private Long id;
    private String name;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
    },
    targetEntity = Album.class)
    @JoinTable(name = "album_artist",
    inverseJoinColumns = @JoinColumn(name = "album_id",
        nullable = false),
    joinColumns = @JoinColumn(name = "artist_id"))
    private List<Album> albums;
}
