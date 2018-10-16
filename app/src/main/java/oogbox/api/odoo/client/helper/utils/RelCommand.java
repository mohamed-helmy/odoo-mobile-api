package oogbox.api.odoo.client.helper.utils;


public enum RelCommand {
    /**
     * (0, _, values)
     * Adds a new record created from the provided value dict.
     */
    ADD(0),
    /**
     * (1, id, values)
     * Updates an existing record of id with the values in
     * values. Can not be used in method create()
     */
    UPDATE(1),
    /**
     * (2, id, _)
     * Removes the record of id from the set, then deletes it
     * (from the database). Can not be used in method create().
     */
    REMOVE(2),
    /**
     * (3, id, _)
     * Removes the record of id from the set, but does not delete it. Can not be used on
     * One2Many. Can not be used in method create();
     */
    UNLINK(3),
    /**
     * (4, id, _)
     * Adds an existing record of id to the set. Can not be used on One2Many.
     */
    LINK(4),
    /**
     * (5, _, _)
     * Removes all records from the set, equivalent to using the command UNLINK on every record
     * explicitly. Can not be used on One2Many and can not be used in create() method.
     */
    REMOVE_ALL(5),
    /**
     * (6, _, ids)
     * Replaces all existing records in the set by the ids list, equivalent to using the
     * command REMOVE_ALL followed by a command LINK for each id in ids
     */
    REPLACE(6);

    private int commandValue;

    RelCommand(int command) {
        this.commandValue = command;
    }

    public int getCommand() {
        return this.commandValue;
    }
}
