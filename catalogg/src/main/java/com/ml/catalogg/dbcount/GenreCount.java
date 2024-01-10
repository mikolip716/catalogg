package com.ml.catalogg.dbcount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreCount {
    private String name;
    private long count;
    public GenreCount (String name, long count) {
        this.name = name;
        this.count = count;
    }
    public int CompareTo (GenreCount genreCount) {
        return  Long.compare(genreCount.getCount(), this.getCount());
    }
}
