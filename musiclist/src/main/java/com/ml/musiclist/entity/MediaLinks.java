package com.ml.musiclist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "media_links")
public class MediaLinks {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "media_id")
    private Long id;
    private String link;
    @Column(name = "site_name")
    private String siteName;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private Album album;
}
