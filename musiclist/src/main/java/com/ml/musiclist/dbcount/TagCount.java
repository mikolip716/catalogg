package com.ml.musiclist.dbcount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCount {
    private String name;
    private long count;
    public TagCount (String name, long count) {
        this.name = name;
        this.count = count;
    }
    public int CompareTo (TagCount tagCount) {
        return  Long.compare(tagCount.getCount(), this.getCount());
    }
}
