package taskEntry;

import java.util.Calendar;

import devgui.Reminder;

/**
 * @author Group 4
 *
 */
public class Task {

    private int id;
    private String name;
    private Category category;
    private Priority priority;
    private Calendar date;
    private String description;
    private int progress;
    private Boolean finished;
    private Reminder reminder;
    
    /**
     * Constructor for a new task.
     *
     * @param name        task's name
     * @param category    task's category
     * @param priority    task's priority
     * @param date        task's date
     * @param description task's description
     */
    public Task(String name, Category category, Priority priority,
            Calendar date, String description, int progress) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.date = date;
        this.description = description;
        this.progress = progress;
        this.finished = false;
        setReminder();
    }

    /**
     * Constructor for a new task.
     *
     * @param name        task's name
     * @param category    task's category
     * @param priority    task's priority
     * @param date        task's date
     * @param description task's description
     * @param finished    task's finished state
     */
    public Task(String name, Category category, Priority priority,
            Calendar date, String description, int progress, boolean finished) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.date = date;
        this.description = description;
        this.progress = progress;
        this.finished = finished;
        setReminder();
    }

    /**
     * Returns the task identifier.
     *
     * @return the task identifier
     */
    public int getID() {
        return id;
    }

    /**
     * Changes an task identifier.
     *
     * @param id the new identifier
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Returns the task name.
     *
     * @return string name of the task
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the task name.
     *
     * @param name new name for the task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the task category.
     *
     * @return task category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Changes the task category.
     *
     * @param category the new category for the task
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Returns the task priority.
     *
     * @return the task priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Changes the task priority.
     *
     * @param priority the new priority for the task
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Returns the date for an task.
     *
     * @return the task date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Changes the date of an task.
     *
     * @param date the new date for the task
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * Returns the description for an task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of an task.
     *
     * @param description the new description of an task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the progress for an task.
     *
     * @return the task progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Changes the progress number of an task.
     *
     * @param num the progress that will be set to an task
     */
    public void setProgress(int num) {
        if (num < 0)
            progress = 0;
        else if (num > 100)
            progress = 100;
        else
            progress = num;
    }

    /**
     * Returns if an task has been completed.
     *
     * @return finished state of an task
     */
    public Boolean getFinished() {
        return finished;
    }

    /**
     * Changes the completed state of an task.
     *
     * @param finished the new finished state of an task
     */
    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    /**
     * Set up timer if the task is not finished and the due date is not passed
     * */
    public void setReminder(){
        Calendar date = (Calendar) this.date.clone();
        int hod = date.get(Calendar.HOUR_OF_DAY);
        //There would be a bug if the date passes one day
        date.set(Calendar.HOUR_OF_DAY,--hod);
        if(!finished && Calendar.getInstance().compareTo(this.date) < 0) {
            reminder = new Reminder(name,date.getTime());
        }
    }
    public void cancelReminder(){
        if (reminder != null)
            reminder.cancelTimer();
    }
}
