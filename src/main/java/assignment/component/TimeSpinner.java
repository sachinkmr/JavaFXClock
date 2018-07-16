package assignment.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSpinner extends Spinner<LocalTime> {
    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS);

    private TimeSpinner(LocalTime time) {
        setEditable(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

            @Override
            public String toString(LocalTime time) {
                return formatter.format(time);
            }

            @Override
            public LocalTime fromString(String string) {
                return LocalTime.parse(string, formatter);
            }
        };

        // The textFormatter both manages the text to LocalTime conversion and vice versa
        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, LocalTime.now(), c -> {
            String newText = c.getControlNewText();
            if (newText.matches("[0-9]{0,2}:[0-9]{0,2} [AP]M")) {
                return c;
            }
            return null;
        });

        // The spinner value factory defines increment and decrement by
        // choosing to the current editing mode:
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(localTimeConverter);
                setValue(time);
            }

            @Override
            public void decrement(int steps) {
                setValue(mode.get().decrement(getValue(), steps));
                mode.get().select(TimeSpinner.this);
            }

            @Override
            public void increment(int steps) {
                setValue(mode.get().increment(getValue(), steps));
                mode.get().select(TimeSpinner.this);
            }
        };

        this.setValueFactory(valueFactory);
        this.getEditor().setTextFormatter(textFormatter);

        // Adding event handler for input
        this.getEditor().addEventHandler(InputEvent.ANY, e -> {
            int caretPos = this.getEditor().getCaretPosition();
            int hrIndex = this.getEditor().getText().indexOf(':');
            int minIndex = this.getEditor().getText().indexOf(' ');
            if (caretPos <= hrIndex) {
                mode.set(Mode.HOURS);
            } else if (caretPos <= minIndex) {
                mode.set(Mode.MINUTES);
            } else {
                mode.set(Mode.AM_PM);
            }
        });

        // When the mode changes, select the new portion:
        mode.addListener((obs, oldMode, newMode) -> newMode.select(this));

    }

    public TimeSpinner() {
        this(LocalTime.now());
    }

    private enum Mode {
        HOURS {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusHours(steps);
            }

            @Override
            void select(TimeSpinner spinner) {
                int index = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, index);
            }
        },
        MINUTES {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusMinutes(steps);
            }

            @Override
            void select(TimeSpinner spinner) {
                int hrIndex = spinner.getEditor().getText().indexOf(':');
                int minIndex = spinner.getEditor().getText().indexOf(' ');
                spinner.getEditor().selectRange(hrIndex + 1, minIndex);
            }
        },
        AM_PM {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusHours(steps * 12);
            }

            @Override
            void select(TimeSpinner spinner) {
                int index = spinner.getEditor().getText().lastIndexOf(" ");
                spinner.getEditor().selectRange(index + 1, spinner.getEditor().getText().length());
            }
        };

        abstract LocalTime increment(LocalTime time, int steps);

        abstract void select(TimeSpinner spinner);

        LocalTime decrement(LocalTime time, int steps) {
            return increment(time, -steps);
        }
    }
}