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
@Table(name = "songs")
public class Songs {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "song_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    private Integer length;
    private String number;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private Album album;
}
