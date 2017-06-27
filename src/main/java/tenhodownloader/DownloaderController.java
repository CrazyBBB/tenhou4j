package tenhodownloader;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DownloaderController implements Initializable {
    public ListView<LocalDate> listView;
    private final DownloadService service = new DownloadService();
    public TableView<InfoSchema> tableView;
    public TableColumn<InfoSchema, String> idColumn;
    public TableColumn<InfoSchema, String> playersColumn;
    public TableColumn<InfoSchema, String> downloadColumn;
    public Label statusBarLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalDate from = LocalDate.of(2017, 1, 1);
        LocalDate to = LocalDate.now().minusDays(10);
        for (LocalDate i = from; to.isAfter(i); i = i.plusDays(1)) {
            this.listView.getItems().add(i);
        }
        this.tableView.setItems(this.service.infoSchemas);
        this.idColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().id));
        this.playersColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().payers));
        this.downloadColumn.setCellValueFactory(e ->
            new SimpleStringProperty(this.service.isDownloaded(e.getValue()) ? "✓" : "")
        );
        this.statusBarLabel.textProperty().bind(Bindings.convert(Bindings.size(this.tableView.getItems())));
    }

    public void downloadIndex(ActionEvent actionEvent) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            this.service.download(listView.getSelectionModel().getSelectedItem());
        }
    }

    public void downloadMjlog(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            this.service.downloadMjlogToDatabase(tableView.getSelectionModel().getSelectedItem());
        }
    }

    public void dump(ActionEvent actionEvent) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Backup File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQLite3 Files", "*.sqlite3"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(this.listView.getScene().getWindow());
        if (selectedFile != null) {
            Main.databaseService.dump(selectedFile);
        }
    }
}
