package devgui;

import taskEntry.Category;
import taskEntry.Task;
import taskEntry.Priority;

import java.util.List;

/**
 * @author Group 4
 *
 */
public abstract class Model {

    /**
     * Add a new task to the model.
     *
     * @param task the task to add
     */
    abstract void addTask(Task task);

    /**
     * Deletes the task with identifier id from the model.
     *
     * @param id identifier of the task to delete
     */
    abstract void deleteTask(int id);

    /**
     * Returns an task with the identifier id.
     *
     * @param id the identifier of the task
     * @return the task with the parameter id
     */
    abstract Task getTask(int id);

    /**
     * Returns the list of tasks.
     *
     * @return the list of tasks
     */
    abstract List<Task> getTaskList();

    /**
     * Changes the name of an task.
     *
     * @param id       task identifier
     * @param newName  the new name for the task
     */
    abstract void changeTaskName(int id, String newName);

    /**
     * Changes the date of an task.
     *
     * @param id       task identifier
     * @param newDate  the new date for the task
     */
    abstract void changeTaskDate(int id, java.util.Calendar newDate);

    /**
     * Changes the description of an task.
     *
     * @param id              task identifier
     * @param newDescription  the new description for the task
     */
    abstract void changeTaskDescription(int id, String newDescription);

    /**
     * Changes the category of an task.
     *
     * @param id           task identifier
     * @param newCategory  the new category for the task
     */
    abstract void changeTaskCategory(int id, Category newCategory);

    /**
     * Changes the priority of an task.
     *
     * @param id           task identifier
     * @param newPriority  the new priority for the task
     */
    abstract void changeTaskPriority(int id, Priority newPriority);

    /**
     * Changes the finished state of an task.
     *
     * @param id task identifier
     */
    abstract void toggleTaskFinished(int id);

    /**
     * Returns a list of ways to sort tasks.
     *
     * @return a list of string of ways to sort tasks
     */
    abstract String[] getSortingTypes();

    /**
     * Returns the possible priorities.
     *
     * @return a list of priorities
     */
    abstract Priority[] getPriorityList();

    /**
     * Returns the possible categories.
     *
     * @return a list of categories
     */
    abstract Category[] getCategoryList();

}
