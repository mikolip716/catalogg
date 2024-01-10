package com.ml.catalogg.helper;

import com.ml.catalogg.ui.MainTableController;
import lombok.Getter;
import lombok.Setter;


public class TableDataHolder {
    @Setter
    @Getter
    private long albumId;
    @Getter
    @Setter
    private MainTableController tableController;
    private final static TableDataHolder INSTANCE = new TableDataHolder();
    private TableDataHolder(){
        albumId = -1;
    };
    public static TableDataHolder getInstance() {
        return INSTANCE;
    }
}
