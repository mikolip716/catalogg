package com.ml.catalogg.helper;

import javafx.application.HostServices;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HyperlinkCell implements Callback<TableColumn<TableMediaLink, Hyperlink>, TableCell<TableMediaLink, Hyperlink>> {
    private static HostServices hostServices;

    public static HostServices getHostServices() {
        return hostServices;
    }

    @Override
    public TableCell<TableMediaLink, Hyperlink> call(TableColumn<TableMediaLink, Hyperlink> arg) {
        TableCell<TableMediaLink, Hyperlink> cell = new TableCell<TableMediaLink, Hyperlink>() {
            @Override
            protected void updateItem(Hyperlink item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : item);
                if (!empty) {
                    item.setOnAction(e -> {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI(item.getText()));
                            } catch (IOException | URISyntaxException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            }
        };
        return cell;
    }
}
