package devgui;

import taskEntry.Category;
import taskEntry.Task;
import taskEntry.Priority;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * @author Daniel S. McCain
 * @author Tim Isbister
 * @author Mathias Jernberg
 * @author ahmetkucuk
 *
 */
public class View extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private Model model;
    private VisualAppPreferences preferences;
    private WindowListener windowListener;
    private LanguageManager languageManager;

    // Components
    private JComboBox<String> sortByList;
    private JButton addButton;
    private ExpandableListView taskList;
    private JLabel sortByLabel;
    private ArrayList<JMenuItem> menuLanguageList;

    // Menu bar buttons
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem newTask;
    private JMenuItem quitItem;
    private JMenuItem availableLanguages;
    private JMenuItem themes;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenuItem aboutItem;
    private JCheckBoxMenuItem systemThemeItem;
    private JCheckBoxMenuItem nimbusThemeItem;
    private JCheckBoxMenuItem metalThemeItem;

    // Keep a buffer for item id so as to operate on corresponding task
    private ArrayList<Integer> taskIdList = new ArrayList<Integer>();

    /**
     * Class constructor for the View.
     *
     * @param model the model that the view gets the information from
     */
    public View(Model model) {
        this.model = model;
        preferences = new VisualAppPreferences();
        languageManager = LanguageManager.getInstance();

        /*
         *  Main window is composed of two panels:
         *  - Left side (taskPanel): Task list, with operations for tasks
         *  - Right side (calendarPanel): Calendar (TODO)
         */

        JPanel mainPanel = new JPanel();
        JPanel taskPanel = setTaskPanel();
        JPanel calendarPanel = setCalendarPanel();

        // This layout splits the application in two: left and right
        mainPanel.setLayout(new GridLayout(1, 2, 2, 0));
        mainPanel.add(taskPanel);
        mainPanel.add(calendarPanel);

        // Create and add menu bar to main frame
        createMenuBar();

        this.setContentPane(mainPanel);

        this.setTitle("Time Manager");
        setInitialViewProperties(); // Location, size and language
        setLabelTexts(); // The texts are set depending on the language
        setVisible(true);

        // When closing the window, save the current settings and tasks
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { windowListener.quit(); }
        });
    }

    private JPanel setTaskPanel() {
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());

        // categoryPanel used to display task sort options
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new FlowLayout());
        categoryPanel.setMaximumSize(new Dimension(250, 100));
        sortByLabel = new JLabel();
        categoryPanel.add(sortByLabel);
        sortByList = new JComboBox<String>();
        sortByList.addActionListener(this);
        categoryPanel.add(sortByList);

        taskList = new ExpandableListView(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton actionCommand = ((JButton) e.getSource());
                if(actionCommand.getActionCommand().equalsIgnoreCase("Remove")) {
                    if(windowListener != null)
                        windowListener.deleteTask();
                } else if(actionCommand.getActionCommand().equalsIgnoreCase("Done")){
                    windowListener.setFinished(taskList.getSelectedTask());
                } else if(actionCommand.getActionCommand().equalsIgnoreCase("Edit")) {
                    if(windowListener != null)
                        windowListener.editTask(taskList.getSelectedTask());
                }
            }
        });
        JScrollPane scroller = new JScrollPane(taskList);

        // buttonPanel contains an add button for the notes
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton();
        buttonPanel.add(addButton);
        // Listener to add button
        addButton.addActionListener(this);

        // Below the sort options, the note list and the buttons are displayed
        taskPanel.add(categoryPanel, BorderLayout.NORTH);
        taskPanel.add(scroller, BorderLayout.CENTER);
        taskPanel.add(buttonPanel, BorderLayout.SOUTH);

        return taskPanel;
    }

    private JPanel setCalendarPanel() {
        JPanel calendarPanel = new JPanel();
        CalendarPanel cal = new CalendarPanel();

        // Temporarily show a gray box instead of a calendar
        JPanel currentTimePanel = new JPanel();
        final JLabel currentTimeLabel = new JLabel();
        currentTimePanel.setLayout(new FlowLayout());
        currentTimePanel.setPreferredSize(new Dimension(10, 37)); // Equal size to sortByList
        //currentTimePanel.setMaximumSize(new Dimension(250, 100));
        final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        ActionListener timerListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                String time = timeFormat.format(date);
                currentTimeLabel.setText(time);
            }
        };
        Timer timer = new Timer(1000, timerListener);
        // to make sure it doesn't wait one second at the start
        timer.setInitialDelay(0);
        timer.start();

        currentTimePanel.add(currentTimeLabel);
        calendarPanel.setLayout(new BorderLayout());
        calendarPanel.add(currentTimePanel, BorderLayout.NORTH);
        calendarPanel.setBackground(Color.LIGHT_GRAY);
        calendarPanel.add(cal, BorderLayout.CENTER);

        return calendarPanel;
    }

    /**
     *  Initiate the creation of the menu bar
     */
    private void createMenuBar() {
        // If OS is a Mac, set menu bar correctly
        if (isMacSystem()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                    "Time Manager");
        }

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create file menu
        fileMenu = new JMenu();
        newTask = new JMenuItem();
        // Shortcut to create a new task
        if (isMacSystem()) {
            newTask.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_N, ActionEvent.META_MASK));
        }
        else {
            newTask.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        }
        fileMenu.add(newTask);
        fileMenu.addSeparator();
        quitItem = new JMenuItem();
        if (isMacSystem()) {
            quitItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_W, ActionEvent.META_MASK));
        }
        else {
            quitItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        }
        fileMenu.add(quitItem);

        // Listeners to file menu
        newTask.addActionListener(this);
        quitItem.addActionListener(this);

        // Create edit menu
        editMenu = new JMenu();
        undoItem = new JMenuItem();
        redoItem = new JMenuItem();
        themes = new JMenu();
        systemThemeItem = new JCheckBoxMenuItem();
        metalThemeItem = new JCheckBoxMenuItem();
        nimbusThemeItem = new JCheckBoxMenuItem();
        themes.add(systemThemeItem);
        themes.add(metalThemeItem);
        themes.add(nimbusThemeItem);
        // TODO: make undo and redo work. Until then, deactivated
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        availableLanguages = new JMenu();
        editMenu.add(availableLanguages);
        editMenu.add(themes);

        menuLanguageList = new ArrayList<JMenuItem>();

        for (String language : languageManager.getAvailableLanguages()) {
            JCheckBoxMenuItem languageItem = new JCheckBoxMenuItem(language);

            languageItem.addActionListener(this);
            availableLanguages.add(languageItem);
            // list with all the JMenuItems, used for actionPerformed
            menuLanguageList.add(languageItem);
        }

        // Listeners to edit menu
        undoItem.addActionListener(this);
        redoItem.addActionListener(this);
        systemThemeItem.addActionListener(this);
        metalThemeItem.addActionListener(this);
        nimbusThemeItem.addActionListener(this);

        // Create help menu
        helpMenu = new JMenu();
        aboutItem = new JMenuItem();
        helpMenu.add(aboutItem);

        // Listeners to help menu
        aboutItem.addActionListener(this);

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        // Add menu bar to main frame
        this.setJMenuBar(menuBar);
    }

    /**
     *  Method called when the language is changed to redraw all labels
     */
    private void setLabelTexts() {
        // Task Side
        String addString = languageManager.getString("ui.action.add");
        String sortByString = languageManager.getString("ui.text.sortby");

        // Translate the sorting types to the current language
        String[] sortingTypes = model.getSortingTypes();
        sortByList.removeAllItems();
        for (int i = 0; i < sortingTypes.length; i++) {
            sortByList.addItem(languageManager.getString(
                    "ui.sort." + sortingTypes[i].toLowerCase()));
        }

        addButton.setText("+ " + addString);
        sortByLabel.setText(sortByString + ": ");

        // Menu Bar
        String fileString = languageManager.getString("ui.menu.file");
        String editString = languageManager.getString("ui.menu.edit");
        String helpString = languageManager.getString("ui.menu.help");
        String newTaskString = languageManager.getString("ui.menu.newtask");
        String quitString = languageManager.getString("ui.menu.quit");
        String undoString = languageManager.getString("ui.menu.undo");
        String redoString = languageManager.getString("ui.menu.redo");
        String languageChangeString =
            languageManager.getString("ui.menu.languagechange");
        String aboutString = languageManager.getString("ui.menu.about");
        String themesString = languageManager.getString("ui.menu.themes");
        String systemThemeString =
            languageManager.getString("ui.menu.systemTheme");
        String metalThemeString =
            languageManager.getString("ui.menu.metalTheme");
        String nimbusThemeString =
            languageManager.getString("ui.menu.nimbusTheme");
        fileMenu.setText(fileString);
        newTask.setText(newTaskString);
        quitItem.setText(quitString);
        editMenu.setText(editString);
        undoItem.setText(undoString);
        redoItem.setText(redoString);
        availableLanguages.setText(languageChangeString);
        helpMenu.setText(helpString);
        aboutItem.setText(aboutString);
        themes.setText(themesString);
        systemThemeItem.setText(systemThemeString);
        metalThemeItem.setText(metalThemeString);
        nimbusThemeItem.setText(nimbusThemeString);

        // Confirmation and alert windows
        UIManager.put("OptionPane.okButtonText",
                languageManager.getString("ui.optionpane.ok"));
        UIManager.put("OptionPane.cancelButtonText",
                languageManager.getString("ui.optionpane.cancel"));
        UIManager.put("OptionPane.yesButtonText",
                languageManager.getString("ui.optionpane.yes"));
        UIManager.put("OptionPane.noButtonText",
                languageManager.getString("ui.optionpane.no"));
    }

    private void setInitialViewProperties() {
        // Set preferences
        this.setSize(preferences.getWidth(), preferences.getHeight());
        this.setLocation(preferences.getX(), preferences.getY());
        String language = preferences.getLanguage();
        // Show in the menu bar which language is set
        for (JMenuItem languageChoice: menuLanguageList) {
            if (languageChoice.getText().equals(language))
                languageChoice.setSelected(true);
            else
                languageChoice.setSelected(false);
        }
        languageManager.setLanguage(language);
        try {
            String theme = preferences.getTheme();
            UIManager.setLookAndFeel(theme);
            SwingUtilities.updateComponentTreeUI(this);
            if (theme.equals(UIManager.getCrossPlatformLookAndFeelClassName()))
                setMetalThemeItem();
            else if (theme.equals(UIManager.getSystemLookAndFeelClassName()))
                setSystemThemeItem();
            else if (theme.equals("javax.swing.plaf.nimbus.NimbusLookAndFeel"))
                setNimbusThemeItem();
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // Default is SystemTheme
            setSystemTheme();
        }
    }

    /**
     * Saves the current properties.
     */
    void saveViewProperties() {
        preferences.setWindowLocationPreference(this.getX(), this.getY());
        preferences.setWindowSizePreference(this.getWidth(), this.getHeight());
        preferences.setLanguage(languageManager.getLanguage());
        String themeClassString = UIManager.getLookAndFeel().getClass().toString();
        /*
         *  themeClassString will be something like:
         *  "class javax.swing.plaf.metal.MetalLookAndFeel"
         *  We want to eliminate "class ", leaving the result in variable theme
         */
        int themeStringStart = themeClassString.indexOf(" ") + 1;
        String theme = themeClassString.substring(themeStringStart);
        preferences.setTheme(theme);
        preferences.savePreferences();
    }

    /**
     * Displays a window and creates an task with the data that is input.
     *
     * @return the new task
     */
    Task newTask(Task item) {
        // A popup will display the main options for a note
        JPanel confirmationPanel = new JPanel(new GridLayout(0, 1));

        String nameString = languageManager.getString("ui.task.name");
        confirmationPanel.add(new JLabel(nameString + ":"));
        JTextField nameField = new JTextField();
        confirmationPanel.add(nameField);

        String categoryString = languageManager.getString("ui.task.category");
        confirmationPanel.add(new JLabel(categoryString + ":"));
        JComboBox<String> categoryField = new JComboBox<>();
        // The categories are translated to the current language
        Category[] categoryList = model.getCategoryList();
        for (int i = 0; i < categoryList.length; i++) {
            categoryField.addItem(languageManager.getString(
                    "ui.category." + categoryList[i].name().toLowerCase()));
        }
        confirmationPanel.add(categoryField);

        String priorityString = languageManager.getString("ui.task.priority");
        confirmationPanel.add(new JLabel(priorityString + ":"));
        JComboBox<String> priorityField = new JComboBox<>();
        // The priorities are translated to the current language
        Priority[] priorityList = model.getPriorityList();
        for (int i = 0; i < priorityList.length; i++) {
            priorityField.addItem(languageManager.getString(
                    "ui.priority." + priorityList[i].name().toLowerCase()));
        }
        confirmationPanel.add(priorityField);

        String dateString = languageManager.getString("ui.task.date");
        confirmationPanel.add(new JLabel(dateString + ":"));
        /*
         *  A spinner is added to select time and date.
         *  The style will be that of the default user's locale.
         */
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(spinnerDateModel);
        DateFormat format = DateFormat.getDateTimeInstance(
                // Date style:
                DateFormat.SHORT,
                // Time style:
                DateFormat.SHORT);
        // Extract the format, such as yyyy-MM-dd hh:mm a
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat)format;
        // Set the format for the spinner
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner,
                simpleDateFormat.toPattern()));
        confirmationPanel.add(dateSpinner);

        String descriptionString =
            languageManager.getString("ui.task.description");
        confirmationPanel.add(new JLabel(descriptionString + ":"));
        JTextField descriptionField = new JTextField();        
        confirmationPanel.add(descriptionField);

        String progressString = languageManager.getString("ui.task.progress");
        confirmationPanel.add(new JLabel(progressString + ":"));
        // Slider from 0 to 100, start at 0
        JSlider progressSlider = new JSlider(0, 100, 0);
        progressSlider.setMajorTickSpacing(10);
        progressSlider.setPaintTicks(true); // Show spacing ticks
        confirmationPanel.add(progressSlider);

        // String that shows on the popup window title
        String taskPopupWindowString;

        if(item != null) {
            nameField.setText(item.getName());

            String selectedCategoryString = languageManager.getString(
                    "ui.category." + item.getCategory().name().toLowerCase());
            categoryField.setSelectedItem(selectedCategoryString);

            String selectedPriorityString = languageManager.getString(
                    "ui.priority." + item.getPriority().name().toLowerCase());
            priorityField.setSelectedItem(selectedPriorityString);

            spinnerDateModel.setValue(item.getDate().getTime());

            descriptionField.setText(item.getDescription());

            progressSlider.setValue(item.getProgress());

            taskPopupWindowString = languageManager.getString("ui.task.edit");
        }
        else {
            taskPopupWindowString = languageManager.getString("ui.task.new");
        }

        int result = JOptionPane.showConfirmDialog(null, confirmationPanel,
                taskPopupWindowString, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        // the task that is created; null if not created
        Task task;

        if (result == JOptionPane.OK_OPTION) {
            // Check what type of category is chosen if note is created
            Category category = Category.fromId(categoryField.getSelectedIndex());

            // Check what type of priority is chosen if note is created
            Priority priority = Priority.fromId(priorityField.getSelectedIndex());

            // Date is extracted for the new task
            Date taskDate = (Date)dateSpinner.getValue();
            Calendar taskCalendarDate = Calendar.getInstance();
            taskCalendarDate.setTime(taskDate);

            int taskProgress = progressSlider.getValue();
            boolean taskFinished = taskProgress == 100;
            task = new Task(nameField.getText(), category, priority,
                    taskCalendarDate, descriptionField.getText(),
                    taskProgress, taskFinished);
        }
        else {
            task = null;
        }

        if(item != null && task != null) {
            task.setID(item.getID());
        }

        return task;
    }

    /**
     * Clear the task id buffer and update it as current model.
     * Display the current model on the task list
     */
    void setTasks(List<Task> tasks) {
        taskIdList.clear();
        for (Task i : tasks)
            taskIdList.add(new Integer(i.getID()));
        taskList.setTasks(tasks, sortByList.getSelectedItem().toString());
    }

    /**
     * Error that is displayed indicating that the task name is empty.
     */
    void emptyTaskNameError() {
        String emptyNameString =
                languageManager.getString("ui.text.notaskname");
        String errorString =
                languageManager.getString("ui.text.error");
        JOptionPane.showMessageDialog(null, emptyNameString, errorString,
                    JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns the selected task's identifier.
     *
     * @return the identifier of the selected task
     */
    Integer getSelectedTaskId() {
        // Get selected task index
        int id = taskList.getSelectedTaskId();
        // Selected nothing
        if (id == -1) {
            // We may create a MessageBox to show it
            String noTaskString =
                languageManager.getString("ui.text.noselectedtask");
            String errorString =
                languageManager.getString("ui.text.error");
            JOptionPane.showMessageDialog(null, noTaskString, errorString,
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        /*
         * Get the task id from the buffer. It is in the current order even
         * after sorted.
         */
        return id;
    }

    /**
     * Confirmation window to check if the user really wants to delete the task.
     *
     * @return the confirmation number
     */
    int deleteConfirmation() {
        String confirmationString =
            languageManager.getString("ui.text.areyousure");
        String deleteWindowName =
            languageManager.getString("ui.text.deletetask");
        return JOptionPane.showConfirmDialog(null, confirmationString,
                deleteWindowName, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Changes the language of the application.
     *
     * @param language
     */
    void setLanguage(String language) { languageManager.setLanguage(language);
        // After having set the language, the labels have to be changed
        setLabelTexts();
    }

    /**
     * Displays a window with information about the project.
     */
    void showAboutInformation() {
        String aboutTitleString = languageManager.getString("ui.menu.about");
        String aboutMessage = "Task Manager\n"
                            + "Version: 0.2\n\n"
                            + "Developed for the User Interface Programming I\n"
                            + "at Uppsala University by:\n\n"
                            + "- Akmanlar, Elif\n"
                            + "- Isbister, Tim\n"
                            + "- Jernberg, Mathias\n"
                            + "- Kucuk, Ahmet\n"
                            + "- McCain, Daniel\n"
                            + "- Zhang, Jingxiao"
                              ;
        JOptionPane.showMessageDialog(this, aboutMessage, aboutTitleString,
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Checks witch action that e is, so it knows to what method to call in the
     * controller.
     */
    public void actionPerformed(ActionEvent e) {

        JComponent source = (JComponent) e.getSource();

        if (windowListener != null) {
            if (source == addButton)
                windowListener.addTask();
            else if (source == undoItem)
                windowListener.undo();
            else if (source == redoItem)
                windowListener.redo();
            else if (source == newTask)
                windowListener.addTask();
            else if (source == quitItem)
                windowListener.quit();
            else if (source == aboutItem)
                windowListener.about();
            else if (source == systemThemeItem)
                windowListener.systemTheme();
            else if (source == metalThemeItem)
                windowListener.metalTheme();
            else if (source == nimbusThemeItem)
                windowListener.nimbusTheme();
            else if (source == sortByList) {
                switch (sortByList.getSelectedIndex()) {
                case 0:
                    windowListener.sortByCategory();
                    break;
                case 1:
                    windowListener.sortByDate();
                    break;
                case 2:
                    windowListener.sortByPriority();
                    break;
                }
            }
            else if (source == menuLanguageList.get(0)) {
                menuLanguageList.get(0).setSelected(true);
                menuLanguageList.get(1).setSelected(false);
                menuLanguageList.get(2).setSelected(false);
                windowListener.setLanguageToEN();
            }
            else if (source == menuLanguageList.get(1)) {
                menuLanguageList.get(0).setSelected(false);
                menuLanguageList.get(1).setSelected(true);
                menuLanguageList.get(2).setSelected(false);
                windowListener.setLanguageToES();
            }
            else if (source == menuLanguageList.get(2)) {
                menuLanguageList.get(0).setSelected(false);
                menuLanguageList.get(1).setSelected(false);
                menuLanguageList.get(2).setSelected(true);
                windowListener.setLanguageToSE();
            }
            else
                System.out.println("EVENT :  " + e.getSource());
        }
    }

    public void addWindowListener(WindowListener windowListener) {
        this.windowListener = windowListener;
    }

    /**
     * Checks if the operating system is MacOS
     * @return true if OS is MacOS, else false
     */
    private boolean isMacSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.startsWith("mac os x");
    }

    /**
     * Set the theme to system's default theme
     */
    public void setSystemTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        setSystemThemeItem();
    }

    private void setSystemThemeItem() {
        systemThemeItem.setState(true);
        metalThemeItem.setState(false);
        nimbusThemeItem.setState(false);
    }

    /**
     * Set the theme to Nimbus
     */
    public void setNimbusTheme() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        setNimbusThemeItem();
    }

    private void setNimbusThemeItem() {
        systemThemeItem.setState(false);
        metalThemeItem.setState(false);
        nimbusThemeItem.setState(true);
    }

    /**
     * Set the theme to Metal
     */
    public void setMetalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);
        setMetalThemeItem();
    }

    private void setMetalThemeItem() {
        systemThemeItem.setState(false);
        metalThemeItem.setState(true);
        nimbusThemeItem.setState(false);
    }

}
