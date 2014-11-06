package devgui;

import taskEntry.Category;
import taskEntry.Task;
import taskEntry.Priority;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.Transient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import resource.ResourceHandler;

/**
 *
 * @author ahmetkucuk
 * @author Daniel S. McCain
 *
 *         Expandable List implementation
 *
 */
public class ExpandableListView extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ActionListener globalActionListener;
    private List<Task> tasks = new ArrayList<>();
    Map<Integer, JPanel> panelById = new TreeMap<Integer, JPanel>();
    private int id = 0;

    // Color list for listview
    //
    public static final Color SmallistBackgroundColor = new Color(121012);
    public static final Color SmallistTextColor = Color.white;
    public static final Color LargeListTitleTextColor = Color.white;
    public static final Color LargeBackgroundColor = new Color(121012);

    public static final int distanceBetweenRows = 15;
    public static final int smallRowHeight = 40;
    public static final int largeRowHeight = 170;

    // public static Color SmallistBackgroundColor = Color.pink;
    ResourceHandler resourceHandler;
    private boolean isSortedByDate = false;

    public ExpandableListView(ActionListener a) {

        this.globalActionListener = a;

        addMouseListener(adapter);
        resourceHandler = new ResourceHandler();
        FlowLayout f = new FlowLayout();
        f.setAlignOnBaseline(true);
        setLayout(f);
    }

    // Listable Interface for Panels
    //
    public interface Listable {

        // @depracated
        int getId();

        Task getTask();
    }

    // Set tasks to listview
    //
    public void setTasks(List<Task> list, String selectedSort) {
        this.tasks = list;
        panelById.clear();
        String dateSortString = LanguageManager.getInstance().getString(
                "ui.sort.date");
        if (selectedSort.equalsIgnoreCase(dateSortString)) {
            isSortedByDate = true;
        } else {
            isSortedByDate = false;
        }
        for (Task i : list) {

            SmallListItem tempP = new SmallListItem(i);
            panelById.put(i.getID(), tempP);

        }
        // after tasks settled call repaintList
        //
        repaintList();
    }

    /**
     *
     * @author ahmetkucuk Divider panel to show overdue tasks
     */
    class DividerPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public DividerPanel() {
            setBackground(Color.red);
        }

        @Override
        public Dimension getPreferredSize() {

            return new Dimension(this.getParent().getWidth(), 3);
        }
    }

    // Repaint List when something changed
    //
    public void repaintList() {

        removeAll();
        List<JPanel> panelList = new ArrayList<JPanel>(panelById.values());

        boolean isDividerAdded = false;
        for (int i = 0; i < tasks.size(); i++) {
            for (JPanel p : panelList) {
                if (((Listable) p).getTask().getID() == tasks.get(i).getID()) {
                    Calendar cal = Calendar.getInstance();
                    if (!tasks.get(i).getDate().getTime().before(cal.getTime())
                            && !isDividerAdded && isSortedByDate) {
                        add(new DividerPanel());
                        isDividerAdded = true;
                    }
                    // We divide all of the tasks with a small divider
                    else {
                        JPanel taskDivider = new JPanel();
                        taskDivider.setPreferredSize(new Dimension(250, 3));
                        add(taskDivider);
                    }
                    add(p);
                }
            }

        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                revalidate();
                repaint();

            }
        });
    }

    // Normal row in listview
    //
    class SmallListItem extends JPanel implements Listable {

        private static final long serialVersionUID = 1L;
        private JLabel titleLabel = new JLabel();

        Task task;
        private int theId;

        public SmallListItem(Task i) {
            setOpaque(true);

            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(
                    Color.LIGHT_GRAY.brighter(), 2, true));

            titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            titleLabel.setForeground(SmallistTextColor);

            // setPreferredSize(new Dimension(250, 30));

            JLabel plusLabel = new JLabel(" + ");
            plusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            plusLabel.setForeground(Color.white);

            add(plusLabel, BorderLayout.WEST);
            add(titleLabel, BorderLayout.CENTER);

            // imagePriority.setImage(resourceHandler.getImage(ResourceHandler.CHECKED_BUTTON_ICON));
            ImageIcon imageCategory = new ImageIcon();
            if (i.getCategory() == Category.HOME) {
                imageCategory.setImage(resourceHandler
                        .getImage(ResourceHandler.G_HOME_ICON));
            } else if (i.getCategory() == Category.SCHOOL) {
                imageCategory.setImage(resourceHandler
                        .getImage(ResourceHandler.G_SCHOOL_ICON));
            } else if (i.getCategory() == Category.WORK) {
                imageCategory.setImage(resourceHandler
                        .getImage(ResourceHandler.G_WORK_ICON));
            }

            /*
             * To show the priority, it will show as many '!' as possible
             * priorities. The higher the priority, the more black and less
             * white exclamation marks shall appear.
             */
            JLabel selectedPriority = new JLabel();
            JLabel remainingPriority = new JLabel();
            int totalPriorities = Priority.values().length;
            int priority = i.getPriority().getId();
            selectedPriority.setText(" "
                    + new String(new char[priority]).replace("\0", "!"));
            remainingPriority.setText(new String(new char[totalPriorities
                    - priority - 1]).replace("\0", "!")
                    + " ");

            selectedPriority.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            remainingPriority.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            selectedPriority.setForeground(Color.black);
            remainingPriority.setForeground(Color.white);

            JPanel priorityCategoryPanel = new JPanel();
            // No padding, we will control that with spaces
            priorityCategoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0,
                    0));

            priorityCategoryPanel.add(new JLabel(imageCategory));
            priorityCategoryPanel.add(selectedPriority);
            priorityCategoryPanel.add(remainingPriority);
            priorityCategoryPanel.setBackground(SmallistBackgroundColor);
            add(priorityCategoryPanel, BorderLayout.EAST);

            task = i;

            titleLabel.setText(task.getName());
            setBackground(SmallistBackgroundColor);
            theId = id;
            id++;

        }

        @Override
        public Dimension getPreferredSize() {

            return new Dimension(this.getParent().getWidth() - 20,
                    smallRowHeight);
        }

        public int getId() {
            return this.theId;
        }

        public Task getTask() {
            return this.task;
        }

    }

    // Expanded row Panel in Listview
    //
    class LargeListItem extends JPanel implements Listable {

        private static final long serialVersionUID = 1L;
        private JLabel titleLabel = new JLabel();
        private JLabel contentLabel = new JLabel();
        private TaskProgressBar progressBar = new TaskProgressBar();
        private JLabel dateLabel = new JLabel();
        // private JLabel contentLabel = new JLabel();
        private JButton removeButton = new JButton();
        private JButton editButton = new JButton();
        private JButton checkButton = new JButton();

        Task task;
        private int theId;

        public LargeListItem(Task i) {
            setOpaque(true);
            setLayout(new GridLayout(0, 1));
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY.darker(),
                    2, true));

            titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            titleLabel.setForeground(LargeListTitleTextColor);

            task = i;
            setBackground(LargeBackgroundColor);
            // setPreferredSize(new Dimension(250, 170));

            titleLabel.setText(task.getName());
            contentLabel.setText(task.getDescription());
            contentLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            // Get default date format
            DateFormat format = DateFormat.getDateTimeInstance(
            // Date style:
                    DateFormat.SHORT,
                    // Time style:
                    DateFormat.SHORT);
            // Extract the format, such as yyyy-MM-dd hh:mm a
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) format;
            dateLabel.setText(simpleDateFormat.format(
                    task.getDate().getTime()));
            this.theId = id;
            id++;

            // Header panel
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BorderLayout());
            JLabel plusLabel = new JLabel(" - ");
            plusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            plusLabel.setForeground(LargeListTitleTextColor);

            headerPanel.add(plusLabel, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            ImageIcon imageCategory = new ImageIcon();
            if (i.getCategory() == Category.HOME) {
                imageCategory.setImage(resourceHandler
                        .getImage(ResourceHandler.G_HOME_ICON));
            } else if (i.getCategory() == Category.SCHOOL) {
                imageCategory.setImage(resourceHandler
                        .getImage(ResourceHandler.G_SCHOOL_ICON));
            } else if (i.getCategory() == Category.WORK) {
                imageCategory.setImage(resourceHandler
                        .getImage(ResourceHandler.G_WORK_ICON));
            }

            /*
             * To show the priority, it will show as many '!' as possible
             * priorities. The higher the priority, the more black and less
             * white exclamation marks shall appear.
             */
            JLabel selectedPriority = new JLabel();
            JLabel remainingPriority = new JLabel();
            int totalPriorities = Priority.values().length;
            int priority = i.getPriority().getId();
            selectedPriority.setText(" "
                    + new String(new char[priority]).replace("\0", "!"));
            remainingPriority.setText(new String(new char[totalPriorities
                    - priority - 1]).replace("\0", "!")
                    + " ");

            selectedPriority.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            remainingPriority.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            selectedPriority.setForeground(Color.black);
            remainingPriority.setForeground(Color.white);

            JPanel priorityCategoryPanel = new JPanel();
            // No padding, we will control that with spaces
            priorityCategoryPanel.setLayout(
                    new FlowLayout(FlowLayout.LEFT, 0, 0));

            priorityCategoryPanel.add(new JLabel(imageCategory));
            priorityCategoryPanel.add(selectedPriority);
            priorityCategoryPanel.add(remainingPriority);

            priorityCategoryPanel.setBackground(SmallistBackgroundColor);
            headerPanel.add(priorityCategoryPanel, BorderLayout.EAST);

            JPanel progressAndDate = new JPanel();
            progressAndDate.setLayout(new GridLayout(0, 1));

            progressBar.setValue(i.getProgress());
            progressAndDate.add(progressBar);
            JPanel dateLabelContainer = new JPanel(
                    new FlowLayout(FlowLayout.CENTER));
            dateLabelContainer.setBackground(SmallistBackgroundColor);
            dateLabelContainer.add(dateLabel);
            progressAndDate.add(dateLabelContainer);

            JPanel buttonsPanel = new JPanel();

            buttonsPanel.setLayout(new GridBagLayout());

            removeButton.addActionListener(globalActionListener);
            checkButton.addActionListener(globalActionListener);
            editButton.addActionListener(globalActionListener);

            // Set icons to buttons
            //
            removeButton.setIcon(new ImageIcon(resourceHandler
                    .getImage(ResourceHandler.G_REMOVE_ICON)));
            editButton.setIcon(new ImageIcon(resourceHandler
                    .getImage(ResourceHandler.G_EDIT_ICON)));
            if (task.getFinished()) {
                checkButton.setIcon(new ImageIcon(resourceHandler
                        .getImage(ResourceHandler.G_CHECKED_BUTTON_ICON)));
            } else {
                checkButton.setIcon(new ImageIcon(resourceHandler
                        .getImage(ResourceHandler.G_UNCHECKED_BUTTON_ICON)));
            }

            // Add button to button panel
            //
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 5, 0);
            buttonsPanel.add(removeButton, c);
            c.weightx = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 5, 0);
            buttonsPanel.add(checkButton, c);
            c.weightx = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 2;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 5, 0);
            buttonsPanel.add(editButton, c);

            // Remove backgroun color of button
            //
            removeButton.setOpaque(false);
            removeButton.setContentAreaFilled(false);
            removeButton.setBorderPainted(false);

            checkButton.setOpaque(false);
            checkButton.setContentAreaFilled(false);
            checkButton.setBorderPainted(false);

            editButton.setOpaque(false);
            editButton.setContentAreaFilled(false);
            editButton.setBorderPainted(false);

            // Set actions to detect later
            //
            checkButton.setActionCommand("Done");
            editButton.setActionCommand("Edit");
            removeButton.setActionCommand("Remove");

            // Set all panel's backgroun color to current backgroun color
            //
            headerPanel.setBackground(LargeBackgroundColor);
            contentLabel.setBackground(LargeBackgroundColor);
            progressAndDate.setBackground(LargeBackgroundColor);
            buttonsPanel.setBackground(LargeBackgroundColor);

            // add panels to large row
            add(headerPanel);
            add(contentLabel);
            add(progressAndDate);
            add(buttonsPanel);

        }

        @Override
        public Dimension getPreferredSize() {

            return new Dimension(this.getParent().getWidth() - 20,
                    largeRowHeight);
        }

        public int getId() {
            return this.theId;
        }

        public Task getTask() {
            return this.task;
        }

    }

    // mouse listener for expanding list
    //
    MouseAdapter adapter = new MouseAdapter() {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            // TODO Auto-generated method stub
            super.mouseClicked(e);
            ExpandableListView source = (ExpandableListView) e.getSource();
            if (source.getComponentAt(e.getPoint()) instanceof Listable) {
                Listable l = (Listable) source.getComponentAt(e.getPoint());
                if (l instanceof SmallListItem) {
                    panelById.clear();
                    for (Task i : tasks) {
                        if (i.getID() == l.getTask().getID()) {
                            LargeListItem tempP = new LargeListItem(i);
                            panelById.put(i.getID(), tempP);

                        } else {
                            SmallListItem tempP = new SmallListItem(i);
                            panelById.put(i.getID(), tempP);
                        }
                    }
                } else {
                    panelById.put(l.getTask().getID(),
                            new SmallListItem(l.getTask()));
                }
                repaintList();

            }
        }
    };

    // returns expanded Panel's task's id
    //
    public int getSelectedTaskId() {

        List<JPanel> tempList = new ArrayList<JPanel>(panelById.values());
        for (JPanel p : tempList) {
            if (p instanceof LargeListItem)
                return ((LargeListItem) p).getTask().getID();
        }
        return -1;

    }

    // returns expanded Panel's task
    //
    public Task getSelectedTask() {
        List<JPanel> tempList = new ArrayList<JPanel>(panelById.values());
        for (JPanel p : tempList) {
            if (p instanceof LargeListItem)
                return ((LargeListItem) p).getTask();
        }
        return null;
    }

    @Override
    @Transient
    public Dimension getPreferredSize() {
        // TODO Auto-generated method stub
        return new Dimension(getParent().getWidth(), tasks.size()
                * (smallRowHeight + distanceBetweenRows) + largeRowHeight);
    }

}
