package devgui;

import taskEntry.Task;

/**
 *
 * @author Tim Isbister
 *
 */
public interface WindowListener {

    public void addTask();
    public void deleteTask();
    public void editTask(Task i);
    public void setFinished(Task i);

    public void undo();
    public void redo();
    public void quit();
    public void about();
    public void systemTheme();
    public void metalTheme();
    public void nimbusTheme();

    public void sortByCategory();
    public void sortByDate();
    public void sortByPriority();

    public void setLanguageToEN();
    public void setLanguageToSE();
    public void setLanguageToES();

}
