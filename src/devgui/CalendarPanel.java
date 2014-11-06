package devgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

/**
 * @author Tim Isbister
 *
 */

public class CalendarPanel extends JPanel {

    private static final long serialVersionUID = -6329329517325915618L;

    SimpleDateFormat month = new SimpleDateFormat("MMMM");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    SimpleDateFormat day = new SimpleDateFormat("d");

    Font LARGE = new Font("", Font.BOLD, 16);
    Font SMALL = new Font("", Font.PLAIN, 12);

    Date date = new Date();

    public void setDate(Date date) {
        this.date = date;
    }

    @SuppressWarnings("deprecation")
    public void paintComponent(Graphics g) {

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.setFont(LARGE);

        g.drawString(month.format(date), 34, 36);
        g.drawString(year.format(date), 235, 36);

        Calendar today = Calendar.getInstance();
        today.setTime(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK) + 2);

        g.setFont(SMALL);

        for (int week = 0; week < 6; week++) {
            for (int d = 0; d < 7; d++) {
                g.setColor(Color.black);
                g.setFont(SMALL);

                // horizon lines
                g.drawLine(30, 50 + ((d + 1) * 30), 285, 49 + ((d + 1) * 30));

                // checks if the calendar day is equal to the current day in
                // reality.

                if (cal.getTime().getDate() == Calendar.getInstance().get(
                            Calendar.DAY_OF_MONTH)) {
                    g.setColor(Color.GREEN);
                    g.setFont(LARGE);
                            }

                g.drawString(day.format(cal.getTime()), d * 36 + 30 + 4,
                        week * 31 + 76 + 20);
                cal.add(Calendar.DATE, +1);
            }
        }
        drawWeek(g);
    }

    private void drawWeek(Graphics g) {

        g.setColor(Color.blue);
        g.drawString("M", 36, 70);
        g.drawString("T", 36 * 2, 70);
        g.drawString("W", 36 * 3, 70);
        g.drawString("T", 36 * 4, 70);
        g.drawString("F", 36 * 5, 70);
        g.setColor(Color.red);
        g.drawString("S", 36 * 6, 70);
        g.drawString("S", 36 * 7, 70);

    }

    private boolean isFreeDay() {
        // TODO Check if day is booked or not. Access the model etc
        // maybe show different strength in color to indicate number of events.
        return false;
    }

}
