package taskEntry;

/**
 * @author Daniel S. McCain
 *
 */
public enum Category {

    NONE(0),
    HOME(1),
    SCHOOL(2),
    WORK(3);

    private int id;

    private Category(int id) {
        this.id = id;
    }

    public static Category fromId(int id) {
        for (Category my: Category.values()) {
            if (my.id == id) {
                return my;
            }
        }

        return null;
    }

    public int getId() {
        return id;
    }
}
