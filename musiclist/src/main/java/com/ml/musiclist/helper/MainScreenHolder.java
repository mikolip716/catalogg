package com.ml.musiclist.helper;

import com.ml.musiclist.ui.MainScreenController;
import lombok.Getter;
import lombok.Setter;

public class MainScreenHolder {
    @Getter
    @Setter
    private MainScreenController mainScreenController;
    private final static MainScreenHolder INSTANCE = new MainScreenHolder();
    private MainScreenHolder(){};
    public static MainScreenHolder getInstance() {
        return INSTANCE;
    }
}
