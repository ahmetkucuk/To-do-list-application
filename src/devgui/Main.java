package devgui;

import javax.swing.SwingUtilities;

/**
 * @author Group 4
 *
 * Inspirations from:
 *  http://leepoint.net/notes-java/GUI/structure/40mvc.html
 *  http://caveofprogramming.com/youtube/#11
 *
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Model model = new AppModel();
                View view = new View(model);
                Controller control = new Controller(view, model);

                view.addWindowListener(control);
            }

        });
    }
}
