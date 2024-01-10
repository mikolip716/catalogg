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
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(
            name = "language_id"
    )
    private Long id;
    private String language;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
    },
            targetEntity = Album.class)
    @JoinTable(name = "album_languages",
            inverseJoinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            joinColumns = @JoinColumn(name = "language_id"))
    private List<Album> albums;
}
