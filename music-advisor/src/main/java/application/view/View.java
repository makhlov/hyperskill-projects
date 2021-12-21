package application.view;

import java.util.List;

public interface View {
    void addToOutput(final String string);
    void addToOutput(final List<String> stringList);

    void prev();
    void next();
}
