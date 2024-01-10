package com.ml.catalogg.dbcount;

import lombok.Getter;

@Getter
public class ArtistCount {
    private String name;
    private long count;
    public ArtistCount (String name, long count) {
        this.name = name;
        this.count = count;
    }
    public int CompareTo (ArtistCount artistCount) {
        return  Long.compare(artistCount.getCount(), this.getCount());
    }
}
