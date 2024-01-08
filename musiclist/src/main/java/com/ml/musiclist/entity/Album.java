package com.ml.musiclist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "album_id")
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    private String country;
    private String type;
    private Integer length;
    @Column(name = "release_date", columnDefinition = "DATE")
    private LocalDate releaseDate;
    private Boolean owned;
    @Column(name = "listened_to")
    private Boolean listenedTo;
    private String format;
    @Column(name = "addition_date", columnDefinition = "DATE", nullable = false)
    private LocalDate additionDate;
    @Column(name = "listened_date", columnDefinition = "DATE")
    private LocalDate listenedDate;
    private String notes;
    private String coverPath;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Songs> songs;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaLinks> mediaLinks;
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },
            targetEntity = Artist.class)
    @JoinTable(name = "album_artist",
            joinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private List<Artist> artists;
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },
            targetEntity = Language.class)
    @JoinTable(name = "album_languages",
            joinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private List<Language> languages;
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },
            targetEntity = Genre.class)
    @JoinTable(name = "album_genres",
            joinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    },
            targetEntity = Tags.class)
    @JoinTable(name = "album_tags",
            joinColumns = @JoinColumn(name = "album_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tags> tags;
}
