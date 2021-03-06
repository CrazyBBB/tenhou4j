package tenhouvisualizer.app.downloader;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableRow;
import tenhouvisualizer.domain.model.InfoSchema;
import tenhouvisualizer.domain.service.DownloadService;

public class InfoSchemaTableRow extends TableRow<InfoSchema> {
    private final DownloaderController controller;
    private final DownloadService service;

    public InfoSchemaTableRow(DownloaderController controller, DownloadService service) {
        this.controller = controller;
        this.service = service;
    }

    @Override
    protected void updateItem(InfoSchema item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            this.setContextMenu(null);
        } else {
            ContextMenu contextMenu = new ContextMenu();
            if (!this.service.isDownloaded(item)) {
                MenuItem downloadMjlog = new MenuItem("牌譜をデータベースに追加する");
                downloadMjlog.setOnAction(e -> this.controller.downloadMjlog(null));
                contextMenu.getItems().add(downloadMjlog);
            } else {
                MenuItem removeMjlog = new MenuItem("牌譜をデータベースから削除する");
                removeMjlog.setOnAction(e -> this.controller.removeMjlog(null));
                contextMenu.getItems().add(removeMjlog);
            }
            contextMenu.getItems().add(new SeparatorMenuItem());
            MenuItem filterWithFirst = new MenuItem("「" + item.getFirst() + "」で検索");
            filterWithFirst.setOnAction(e -> updateFilter(item.getFirst()));
            MenuItem filterWithSecond = new MenuItem("「 " + item.getSecond() + "」で検索");
            filterWithSecond.setOnAction(e -> updateFilter(item.getSecond()));
            MenuItem filterWithThird = new MenuItem("「 " + item.getThird() + "」で検索");
            filterWithThird.setOnAction(e -> updateFilter(item.getThird()));
            contextMenu.getItems().addAll(filterWithFirst, filterWithSecond, filterWithThird);
            if (item.getFourth() != null) {
                MenuItem filterWithFourth = new MenuItem("「 " + item.getFourth() + "」で検索");
                filterWithFourth.setOnAction(e -> updateFilter(item.getFourth()));
                contextMenu.getItems().add(filterWithFourth);
            }
            this.setContextMenu(contextMenu);
        }
    }

    private void updateFilter(String filter) {
        this.controller.filterField.setText(filter);
        this.controller.search(null);
    }

}

