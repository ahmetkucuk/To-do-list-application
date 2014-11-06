package resource;

import java.awt.Image;
import java.net.URL;

/**
 * @author ahmetkucuk
 *
 * This class helps you to load image from resource
 *
 * Usage:
 *
 * put your image into resource package
 * define path for you image
 * get image using following
 * Image i = new ResourceHandler().getImage(ResourceHandler.SAVE_BUTTON_ICON_LARGE);
 *
 * More features can be added as we need
 */

public class ResourceHandler {


    public final static String SAVE_BUTTON_ICON_LARGE = "/resource/icons/save_button_icon_large.png";
    public final static String SAVE_BUTTON_ICON_SMALL = "/resource/icons/save_button_icon_small.png";
    public final static String UNDO_BUTTON_ICON = "/resource/icons/undo_button_icon.png";
    public final static String REDO_BUTTON_ICON = "/resource/icons/redo_button_icon.png";

    public final static String CHECKED_BUTTON_ICON = "/resource/icons/checked_icon.png";
    public final static String UNCHECKED_BUTTON_ICON = "/resource/icons/unchecked_icon.png";
    public final static String REMOVE_BUTTON_ICON = "/resource/icons/remove_icon.png";
    public final static String EDIT_BUTTON_ICON = "/resource/icons/edit_icon.png";

    public final static String G_CHECKED_BUTTON_ICON = "/resource/icons/g_checked.png";
    public final static String G_UNCHECKED_BUTTON_ICON = "/resource/icons/g_unchecked.png";
    public final static String G_EDIT_ICON = "/resource/icons/g_edit.png";
    public final static String G_HOME_ICON = "/resource/icons/g_home.png";
    public final static String G_REMOVE_ICON = "/resource/icons/g_remove.png";
    public final static String G_SCHOOL_ICON = "/resource/icons/g_school.png";
    public final static String G_WORK_ICON = "/resource/icons/g_work.png";

    public ResourceHandler () {

    }

    /**
     *
     * @param resource
     * @return URL of resource
     */
    public URL getResourceUrl(String resource) {

        URL url = ResourceHandler.class.getResource(resource);
        return url;
    }

    /**
     *
     * @param resource
     * @return get image resource
     */
    public Image getImage(String resource) {
        URL url = getResourceUrl(resource);

        return java.awt.Toolkit.getDefaultToolkit().getImage(url);
    }

    /**
     *
     * @param resource
     * @return path of resource
     */
    public String getResourcePath(String resource) {

        return getResourceUrl(resource).getPath();
    }

}
