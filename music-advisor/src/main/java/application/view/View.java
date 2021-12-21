package application.view;

import java.util.List;

public interface View {
    void addToOutputQueue(String string);
    void addToOutputQueue(List<String> stringList);

    void prev();
    void next();
}
