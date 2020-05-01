package cs3524.mud.client;

import java.util.ArrayList;

public class Inventory extends ArrayList<String> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        if (this.size() > 0) {
            return "Your current inventory: " + String.join(", ", this);
        } else {
            return "Your inventory is empty";
        }
    }
}