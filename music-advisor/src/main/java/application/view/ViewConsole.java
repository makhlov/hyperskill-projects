package application.view;

import java.util.Arrays;
import java.util.List;

public class ViewConsole implements View {

    @Override
    public void addToOutputQueue(String string) {
        System.out.println(string);
    }

    @Override
    public void addToOutputQueue(List<String> stringList) {
        //TODO: Replace mock to correct impl
        System.out.println(Arrays.toString(stringList.toArray()));
    }

    @Override
    public void prev() {

    }

    @Override
    public void next() {

    }
}
