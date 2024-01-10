package com.ml.catalogg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(
            name = "genre_id"
    )
    private Long id;
    private String name;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
    },
            targetEntity = Album.class)
    @JoinTable(name = "album_genres",
            inverseJoinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            joinColumns = @JoinColumn(name = "genre_id"))
    private List<Album> albums;
}
