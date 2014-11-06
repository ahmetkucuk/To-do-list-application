package devgui;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/**
 * Set up a timer for each task
 * @author Jingxiao Zhang
 **/
public class Reminder {

    private Timer timer;
    private String taskName;
    private class RemindMessage extends TimerTask{

        @Override
        public void run() {
            LanguageManager language = LanguageManager.getInstance();
            JOptionPane.showMessageDialog(null,
                    language.getString("ui.text.thetask") + ": " +
                    taskName + "\n" +
                    language.getString("ui.text.startsinhour"),
                    language.getString("ui.text.upcomingtask"),
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }
    public Reminder(String taskName, Date date){
        this.taskName = taskName;
        timer = new Timer();
        timer.schedule(new RemindMessage(), date);
    }

    public void cancelTimer(){
        this.timer.cancel();
    }
}
