package devgui;

import taskEntry.Category;
import taskEntry.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

/**
 * @author Daniel McCain
 * @author Tim Isbister
 * @author ahmetkucuk
 */
/*
 * This class is the Observer, observing the subject A.K.A the view class. All
 * logic happens here. Add a method to the interface WindowListener class that
 * you want to implement.
 */

public class Controller implements WindowListener {

    private Model model;
    private View view;

    // Add an operator to load and save data to local XML file
    private XMLOperation xmlOp;

    // Control the current sort type
    private SortMethod currentSort;

    /**
     * Constructor for the Controller.
     *
     * @param view
     *            the view that will be controlled
     * @param model
     *            the model that will be controlled
     */
    public Controller(View view, Model model) {
        this.model = model;
        this.view = view;

        this.currentSort = SortMethod.Category;

        // Load XML data
        String homeDirectory = System.getProperty("user.home");
        String savedTaskFile = homeDirectory + "/TODO-tasks";
        xmlOp = new XMLOperation(savedTaskFile);

        // Populate the model and send the tasks to the view
        List<Task> dataFromFile = xmlOp.restore();
        for (Task it : dataFromFile) {
            model.addTask(it);
        }
        view.setTasks(model.getTaskList());
        sortByCategory();
    }

    public void addTask() {
        Task task = view.newTask(null);
        if (task != null) { // When the user doesn't cancel the task creation
            // Don't add a task with an empty name
            if (task.getName().equals("")) {
                view.emptyTaskNameError();
            }
            else if (task != null) {
                model.addTask(task);
                switch (currentSort) {
                case Category:
                    sortByCategory();
                    break;
                case Date:
                    sortByDate();
                    break;
                case Priority:
                default:
                     sortByPriority();
                     break;
                }
            }
        }

    }

    @Override
    public void editTask(Task i) {
        Task task = view.newTask(i);
        if (task != null) { // When the user doesn't cancel the task edit
            // Don't add a task with an empty name
            if (task.getName().equals("")) {
                view.emptyTaskNameError();
            }
            else {
                model.addTask(task);
                switch (currentSort) {
                case Category:
                    sortByCategory();
                    break;
                case Date:
                    sortByDate();
                    break;
                case Priority:
                default:
                     sortByPriority();
                     break;
                }
            }
        }
    }

    @Override
    public void deleteTask() {
        Integer id = view.getSelectedTaskId();
        if (id == null)
            return;
        int response = view.deleteConfirmation();
        if (response == JOptionPane.OK_OPTION) {
            model.deleteTask(id);
            // TODO: Make current sorting persistent
            view.setTasks(model.getTaskList());
        }
    }

    @Override
    public void undo() {
        // TODO: Implement
    }

    @Override
    public void redo() {
        // TODO: Implement
    }

    @Override
    public void quit() {
        // Save tasks and properties before exiting
        xmlOp.save(model.getTaskList());
        view.saveViewProperties();
        System.exit(0);
    }

    @Override
    public void about() {
        view.showAboutInformation();
    }

    @Override
    public void sortByCategory() {
        List<Task> taskList = model.getTaskList();
        Collections.sort(taskList, new Comparator<Task>() {
            public int compare(Task i1, Task i2) {
                Category c1 = i1.getCategory();
                Category c2 = i2.getCategory();
                return c1.compareTo(c2);
            }
        });
        currentSort = SortMethod.Category;
        view.setTasks(taskList);
    }

    @Override
    public void sortByDate() {
        List<Task> taskList = model.getTaskList();
        Collections.sort(taskList, new Comparator<Task>() {
            public int compare(Task i1, Task i2) {
                Calendar date1 = i1.getDate();
                Calendar date2 = i2.getDate();
                return date1.compareTo(date2);
            }
        });
        currentSort = SortMethod.Date;
        view.setTasks(taskList);
    }

    @Override
    public void sortByPriority() {
        List<Task> taskList = model.getTaskList();
        Collections.sort(taskList, new Comparator<Task>() {
            public int compare(Task i1, Task i2) {
                /*
                 * We want the priority to be: Critical > High > Medium > Low >
                 * None
                 *
                 * To achieve this, i2 must be compared to i1
                 * Else, the order would be inverse: N > L > M > H > C
                 */
                return i2.getPriority().compareTo(i1.getPriority());
            }
        });
        currentSort = SortMethod.Priority;
        view.setTasks(taskList);
    }

    @Override
    public void setLanguageToEN() {
        Locale english = new Locale("en");
        String englishString = english.getDisplayLanguage();
        view.setLanguage(englishString);
    }

    @Override
    public void setLanguageToSE() {
        Locale swedish = new Locale("sv", "SE");
        String swedishString = swedish.getDisplayLanguage();
        view.setLanguage(swedishString);
    }

    @Override
    public void setLanguageToES() {
        Locale spanish = new Locale("es", "ES");
        String spanishString = spanish.getDisplayLanguage();
        view.setLanguage(spanishString);
    }


    public void setFinished(Task i) {
        Task task = i;
        task.setFinished(!task.getFinished());
        // Don't add a task with an empty name
        if (task != null && !task.getName().equals("")) {
            if (task.getFinished())
                task.setProgress(100);
            else
                task.setProgress(task.getProgress() - 10);
            model.addTask(task);
            ArrayList<Task> taskList = (ArrayList<Task>) model.getTaskList();
            view.setTasks(taskList);
        }
    }

    /**
     * Call theme change to system default theme in View
     */
    @Override
    public void systemTheme() {
        view.setSystemTheme();
    }

    /**
     * Call theme change to Metal in View
     */
    @Override
    public void metalTheme() {
        view.setMetalTheme();
    }

    /**
     * Call theme change to Nimbus in View
     */
    @Override
    public void nimbusTheme() {
        view.setNimbusTheme();
    }

    private enum SortMethod {
        Category, Date, Priority;
    }

}
