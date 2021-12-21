package application.view;

import java.util.List;

public class ViewConsole implements View {

    private static final String NO_MORE_PAGES = "No more pages.";
    private List<String> data;

    private int itemIndexPointer;
    private int currentPageNumber;
    private int pagesTotalAmount;
    private int itemsPerPage;

    private ViewConsole(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public static View create(int itemsPerPage) {
        return new ViewConsole(itemsPerPage);
    }

    @Override
    public void addToOutput(final String string) {
        println(string);
    }

    @Override
    public void addToOutput(final List<String> data) {
        this.data = data;
        resetPagination();
        next();
    }

    private void resetPagination() {
        itemIndexPointer = 0;
        currentPageNumber = 0;
        pagesTotalAmount = data.size() / itemsPerPage;
        pagesTotalAmount += data.size() % itemsPerPage != 0 ? 1 : 0;
    }

    @Override
    public void next() {
        if (currentPageNumber > pagesTotalAmount || currentPageNumber == pagesTotalAmount) {
            println(NO_MORE_PAGES);
        } else {
            itemIndexPointer += itemsPerPage;
            currentPageNumber++;
            imprint();
        }
    }

    @Override
    public void prev() {
        if (currentPageNumber == 1) {
            println(NO_MORE_PAGES);
        } else {
            itemIndexPointer -= itemsPerPage;
            currentPageNumber--;
            imprint();
        }
    }

    private void imprint() {
        if (itemsPerPage == 1) {
            System.out.println(data.get(currentPageNumber - 1));
        } else {
            for (int i = 0, j = 0; i < data.size(); i++) {
                if (i < itemIndexPointer) continue;
                if (j < itemsPerPage) {
                    println(data.get(i));
                    j++;
                }
            }
        }

        if (itemIndexPointer < data.size())
            System.out.printf("---PAGE %d OF %d---\n", currentPageNumber, pagesTotalAmount);
        else println(NO_MORE_PAGES);
    }

    private void println(String out) {
        System.out.println(out);
    }
}