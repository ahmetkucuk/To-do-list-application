package devgui;

import taskEntry.Category;
import taskEntry.Task;
import taskEntry.Priority;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author Group 4
 *
 */
public class AppModel extends Model {

    // maps unique integers with the tasks
    private HashMap<Integer, Task> tasks;
    // the next unique integer
    private int nextTaskID;
    private String[] sortingTypes = {"Category", "Date", "Priority"};
    private Priority[] priorityList = Priority.values();
    private Category[] categoryList = Category.values();

    /**
     * Class constructor
     */
    public AppModel() {
        tasks = new HashMap<Integer, Task>();
        // The initial id will be 1
        nextTaskID = 1;
    }

    @Override
    public void addTask(Task task) {
        if(tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        } else {
            Task newTask = task;
            newTask.setID(nextTaskID);
            tasks.put(nextTaskID++, task);
        }
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.get(id).cancelReminder();
            tasks.remove(id);
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = null;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        }
        return task;
    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<Task>(tasks.values());
    }

    @Override
    public void changeTaskName(int id, String newName) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setName(newName);
        }
    }

    @Override
    public void changeTaskDate(int id, Calendar newDate) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setDate(newDate);
        }
    }

    @Override
    public void changeTaskDescription(int id, String newDescription) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setDescription(newDescription);
        }
    }

    @Override
    public void changeTaskCategory(int id, Category newCategory) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setCategory(newCategory);
        }
    }

    @Override
    public void changeTaskPriority(int id, Priority newPriority) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setPriority(newPriority);
        }
    }

    @Override
    public void toggleTaskFinished(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            task.setFinished(!task.getFinished());
        }
    }

    @Override
    public String[] getSortingTypes() {
        return sortingTypes;
    }

    @Override
    Priority[] getPriorityList() {
        return priorityList;
    }

    @Override
    Category[] getCategoryList() {
        return categoryList;
    }

}
