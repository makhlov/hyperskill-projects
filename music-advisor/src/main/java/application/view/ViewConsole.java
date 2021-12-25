/* Class name: ViewConsole
 * Date: 22.12.21
 * Version 1.0
 * Author: makhlov
 */
package application.view;

import java.util.List;

/**
 * Simple console implementation of <code>View</code> interface
 */
public class ViewConsole implements View {

    private static final String NO_MORE_PAGES = "No more pages.";
    private static final String PAGE_POSITION = "---PAGE %d OF %d---\n\n";

    private List<String> data;

    private final int itemsPerPage;
    private int itemIndexPointer;
    private int currentPageNumber;
    private int pagesTotalAmount;

    /**
     * Private <code>ViewConsole</code> constructor
     *
     * @param itemsPerPage number of displayed items on one page
     */
    private ViewConsole(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * Method for creating new class instances
     *
     * @param itemsPerPage number of items displayed on one page
     * @return             new <code>ViewConsole</code> class instance
     */
    public static View create(int itemsPerPage) {
        return new ViewConsole(itemsPerPage);
    }

    /** {@inheritDoc} */
    @Override
    public void addToOutput(final String string) {
        println(string);
    }

    /** {@inheritDoc} */
    @Override
    public void addToOutput(final List<String> data) {
        this.data = data;
        resetPagination();
        next();
    }

    /**
     * Sets the state of <code>ViewConsole</code> class to the initial
     * values that corresponding with the beginning of new information output
     */
    private void resetPagination() {
        itemIndexPointer = 0;
        currentPageNumber = 0;
        pagesTotalAmount = data.size() / itemsPerPage;
        pagesTotalAmount += data.size() % itemsPerPage != 0 ? 1 : 0;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /**
     * Outputs a range of elements that match the requested page number
     */
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
            System.out.printf(PAGE_POSITION, currentPageNumber, pagesTotalAmount);
        else println(NO_MORE_PAGES);
    }

    /**
     * Just shortening <code>println</code> console output
     * @param out string to print
     */
    private void println(String out) {
        System.out.println(out);
    }
}