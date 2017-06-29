package tenhodownloader;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tenhouvisualizer.App;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class DownloaderController implements Initializable {
    public TabPane tabPane;
    public ListView<LocalDate> dateListView;
    public ListView<LocalDateTime> hourListView;
    private final DownloadService service = new DownloadService();
    public TableView<InfoSchema> tableView;
    public TableColumn<InfoSchema, String> takuColumn;
    public TableColumn<InfoSchema, String> playersColumn;
    public TableColumn<InfoSchema, String> downloadColumn;
    public Label statusBarLabel;
    public Tab currentYearTab;
    public Tab currentWeekTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        {
            LocalDate from = LocalDate.of(2017, 1, 1);
            LocalDate to = LocalDate.now().minusDays(7);
            for (LocalDate i = from; to.isAfter(i); i = i.plusDays(1)) {
                this.dateListView.getItems().add(i);
            }
        }

        {
            LocalDateTime from = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.MIN);
            LocalDateTime to = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
            for (LocalDateTime i = from; to.isAfter(i); i = i.plusHours(1)) {
                this.hourListView.getItems().add(i);
            }
        }

        this.tableView.setItems(this.service.infoSchemas);
        this.takuColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().taku));
        this.playersColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().payers));
        this.downloadColumn.setCellValueFactory(e ->
            new SimpleStringProperty(this.service.isDownloaded(e.getValue()) ? "✓" : "")
        );
        this.statusBarLabel.textProperty().bind(Bindings.convert(Bindings.size(this.tableView.getItems())));
    }

    public void downloadIndex(ActionEvent actionEvent) {
        if (tabPane.getSelectionModel().getSelectedItem() == currentYearTab) {
            if (dateListView.getSelectionModel().getSelectedItem() != null) {
                this.service.downloadDate(dateListView.getSelectionModel().getSelectedItem());
            }
        } else if (tabPane.getSelectionModel().getSelectedItem() == currentWeekTab) {
            if (hourListView.getSelectionModel().getSelectedItem() != null) {
                this.service.downloadHour(hourListView.getSelectionModel().getSelectedItem());
            }
        }
    }

    public void downloadMjlog(ActionEvent actionEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            InfoSchema infoSchema = tableView.getSelectionModel().getSelectedItem();
            if (!App.databaseService.existsId(infoSchema.id)) {
                this.service.downloadMjlogToDatabase(infoSchema);
                tableView.getItems().set(tableView.getSelectionModel().getFocusedIndex(), infoSchema);
            }
        }
    }

    public void dump(ActionEvent actionEvent) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Backup File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SQLite Files", "*.sqlite"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(this.dateListView.getScene().getWindow());
        if (selectedFile != null) {
            App.databaseService.dump(selectedFile);
        }
    }
}
