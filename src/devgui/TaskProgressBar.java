/**
 *
 */
package devgui;

import java.awt.Dimension;

import javax.swing.JProgressBar;

/**
 * @author Daniel S. McCain
 *
 */
public class TaskProgressBar extends JProgressBar {

    private static final long serialVersionUID = 1L;

    private int min = 0;
    private int max = 100;

    TaskProgressBar (int progress) {
        // The bar goes from 0 to the number of possible priorities
        setMinimum(min);
        setMaximum(max);
        setValue(progress);

        // Set text is progress percentage
        setStringPainted(true);
        setString(Integer.toString(progress) + "%");

        // Small bar by default
        setPreferredSize(new Dimension(30, 20));
    }

    TaskProgressBar() {
        setMinimum(min);
        setMaximum(max);
        setValue(0);

        // Set text is progress percentage
        setStringPainted(true);
        setString("0%");

        // Small bar by default
        setPreferredSize(new Dimension(30, 20));
    }

    /**
     * @author ahmetkucuk
     */
    @Override
    public void setValue(int n) {
        super.setValue(n);
        setString(Integer.toString(n) + "%");
    }

}
