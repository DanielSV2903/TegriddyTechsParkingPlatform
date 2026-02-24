package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingSpaceConfigDialogController {

    public static final class TypeRow {
        private final ObjectProperty<VehicleType> vehicleType = new SimpleObjectProperty<>();
        private final IntegerProperty count = new SimpleIntegerProperty(0);

        public TypeRow(VehicleType vehicleType, int count) {
            this.vehicleType.set(Objects.requireNonNull(vehicleType));
            this.count.set(Math.max(0, count));
        }

        public VehicleType getVehicleType() { return vehicleType.get(); }
        public ObjectProperty<VehicleType> vehicleTypeProperty() { return vehicleType; }

        public int getCount() { return count.get(); }
        public IntegerProperty countProperty() { return count; }

        public void setCount(int value) { count.set(Math.max(0, value)); }
    }

    @FXML private Spinner<Integer> spTotalSpaces;
    @FXML private Spinner<Integer> spPreferentialPercent;

    @FXML private Label lblPreferentialCount;
    @FXML private Label lblSumByType;
    @FXML private Label lblRemaining;
    @FXML private Label lblValidation;

    @FXML private TableView<TypeRow> tblTypes;
    @FXML private TableColumn<TypeRow, String> colTypeName;
    @FXML private TableColumn<TypeRow, Integer> colCount;

    @FXML private Button btnApply;

    private final ObservableList<TypeRow> rows = FXCollections.observableArrayList();

    private ParkingLot targetLot;
    private boolean applied = false;
    private ParkingSpace[] resultSpaces;

    @FXML
    private void initialize() {
        spTotalSpaces.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50_000, 0));
        spPreferentialPercent.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        spTotalSpaces.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
        spPreferentialPercent.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

        colTypeName.setCellValueFactory(data -> {
            VehicleType vt = data.getValue().getVehicleType();
            String name = vt != null ? vt.getDescription() : "";
            return new SimpleStringProperty(name);
        });

        colCount.setCellValueFactory(data -> data.getValue().countProperty().asObject());
        colCount.setCellFactory(tc -> new TableCell<>() {
            private final Spinner<Integer> spinner = new Spinner<>(0, 50_000, 0);

            {
                spinner.setEditable(true);
                spinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
                spinner.valueProperty().addListener((obs, oldV, newV) -> {
                    TypeRow row = getTableRow() == null ? null : (TypeRow) getTableRow().getItem();
                    if (row != null && newV != null) {
                        row.setCount(newV);
                        refreshComputed();
                    }
                });
            }

            @Override
            protected void updateItem(Integer value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    spinner.getValueFactory().setValue(value == null ? 0 : Math.max(0, value));
                    setGraphic(spinner);
                }
            }
        });

        tblTypes.setItems(rows);

        spTotalSpaces.valueProperty().addListener((o, a, b) -> refreshComputed());
        spPreferentialPercent.valueProperty().addListener((o, a, b) -> refreshComputed());

        refreshComputed();
    }

    public void init(ParkingLot lot, List<VehicleType> vehicleTypes) {
        this.targetLot = Objects.requireNonNull(lot, "lot");
        rows.clear();

        if (vehicleTypes != null) {
            for (VehicleType vt : vehicleTypes) {
                if (vt != null) {
                    rows.add(new TypeRow(vt, 0));
                }
            }
        }
        refreshComputed();
    }

    public boolean isApplied() {
        return applied;
    }

    public ParkingSpace[] getResultSpaces() {
        return resultSpaces;
    }

    @FXML
    private void onAutoDistribute() {
        normalizeCountsToTotal();
        tblTypes.refresh();
        refreshComputed();
    }

    @FXML
    private void onCancel() {
        applied = false;
        resultSpaces = null;
        closeWindow();
    }

    @FXML
    private void onApply() {
        // Auto-ajuste SIEMPRE, como pediste
        normalizeCountsToTotal();
        refreshComputed();

        int total = safeInt(spTotalSpaces.getValue());
        int preferentialCount = computePreferentialCount(total);

        List<ParkingSpace> spaces = new ArrayList<>(total);

        int number = 1;
        int remainingPreferential = preferentialCount;

        for (TypeRow row : rows) {
            VehicleType vt = row.getVehicleType();
            int count = Math.max(0, row.getCount());

            for (int i = 0; i < count; i++) {
                boolean preferential = remainingPreferential > 0;
                if (preferential) remainingPreferential--;

                ParkingSpace s = new ParkingSpace(number++, vt.getSpaceType(), preferential, false);
                s.setParkingLot(targetLot);
                spaces.add(s);
            }
        }

        // Por seguridad, si algo quedara corto (no debería), rellena con el primer tipo disponible
        while (spaces.size() < total) {
            VehicleType fallback = rows.isEmpty() ? null : rows.get(0).getVehicleType();
            boolean preferential = remainingPreferential > 0;
            if (preferential) remainingPreferential--;

            ParkingSpace s = new ParkingSpace(number++, fallback != null ? fallback.getSpaceType() : null, preferential, false);
            s.setParkingLot(targetLot);
            spaces.add(s);
        }

        resultSpaces = spaces.toArray(new ParkingSpace[0]);
        applied = true;
        closeWindow();
    }

    private void normalizeCountsToTotal() {
        int total = safeInt(spTotalSpaces.getValue());
        if (rows.isEmpty()) {
            return;
        }

        int sum = computeSumByType();

        // Si falta, distribuye equitativamente
        if (sum < total) {
            int remaining = total - sum;
            int n = rows.size();
            int base = remaining / n;
            int extra = remaining % n;

            for (int i = 0; i < n; i++) {
                int add = base + (i < extra ? 1 : 0);
                rows.get(i).setCount(rows.get(i).getCount() + add);
            }
            return;
        }

        // Si se pasa, recorta desde el final
        if (sum > total) {
            int over = sum - total;
            for (int i = rows.size() - 1; i >= 0 && over > 0; i--) {
                TypeRow row = rows.get(i);
                int c = row.getCount();
                if (c <= 0) continue;

                int dec = Math.min(c, over);
                row.setCount(c - dec);
                over -= dec;
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnApply.getScene().getWindow();
        stage.close();
    }

    private void refreshComputed() {
        int total = safeInt(spTotalSpaces.getValue());
        int sumByType = computeSumByType();
        int remaining = total - sumByType;

        lblPreferentialCount.setText(String.valueOf(computePreferentialCount(total)));
        lblSumByType.setText(String.valueOf(sumByType));
        lblRemaining.setText(String.valueOf(Math.max(0, remaining)));

        if (remaining < 0) {
            lblValidation.setText("La suma por tipo excede el total (se corregirá automáticamente al aplicar).");
        } else {
            lblValidation.setText("");
        }
    }

    private int computeSumByType() {
        int sum = 0;
        for (TypeRow row : rows) sum += Math.max(0, row.getCount());
        return sum;
    }

    private int computePreferentialCount(int total) {
        int percent = safeInt(spPreferentialPercent.getValue());
        percent = Math.max(0, Math.min(100, percent));
        return (int) Math.round(total * (percent / 100.0));
    }

    private int safeInt(Integer v) {
        return v == null ? 0 : v;
    }
}