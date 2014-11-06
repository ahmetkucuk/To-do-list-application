package taskEntry;

/**
 * @author Daniel S. McCain
 *
 */
public enum Priority {

    // The different possible priorities:
    NONE(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private int id;

    private Priority(int id) {
        this.id = id;
    }

    public static Priority fromId(int id) {
        for (Priority my: Priority.values()) {
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
