package oogbox.api.odoo.client.helper.utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RelValues {

    private HashMap<RelCommand, List<Object>> commandsListHashMap = new HashMap<>();

    /**
     * (0, _, values)
     * Adds a new record created from the provided value dict.
     *
     * @param values Dict values
     * @return chaining object
     */
    public RelValues add(OdooValues values) {
        add(Collections.singletonList(values));
        return this;
    }

    /**
     * (0, _, values)
     * Adds a new record created from the provided value dict.
     *
     * @param valueList List of Dict values
     * @return chaining object
     */
    public RelValues add(List<OdooValues> valueList) {
        List<Object> values = getCommandList(RelCommand.ADD);
        values.addAll(valueList);
        commandsListHashMap.put(RelCommand.ADD, values);
        return this;
    }

    /**
     * (1, id, values)
     * Updates an existing record of id with the values in
     * values. Can not be used in method create()
     *
     * @param id    record id
     * @param value values dict to update
     * @return chaining object
     */
    public RelValues update(int id, OdooValues value) {
        value.put("_id", id);
        List<Object> values = getCommandList(RelCommand.UPDATE);
        values.add(value);
        commandsListHashMap.put(RelCommand.UPDATE, values);
        return this;
    }

    /**
     * (5, _, _)
     * Removes all records from the set, equivalent to using the command UNLINK on every record
     * explicitly. Can not be used on One2Many and can not be used in create() method.
     *
     * @return chaining object
     */
    public RelValues removeAll() {
        commandsListHashMap.put(RelCommand.REMOVE_ALL, getCommandList(RelCommand.REMOVE_ALL));
        return this;
    }

    /**
     * (3, id, _)
     * Removes the record of id from the set, but does not delete it. Can not be used on
     * One2Many. Can not be used in method create();
     *
     * @param id record id
     * @return chaining object
     */
    public RelValues unlink(int id) {
        return unlink(Collections.singletonList(id));
    }

    /**
     * (3, id, _)
     * Removes the record of id from the set, but does not delete it. Can not be used on
     * One2Many. Can not be used in method create();
     *
     * @param ids list of record ids
     * @return chaining object
     */
    public RelValues unlink(List<Integer> ids) {
        List<Object> items = getCommandList(RelCommand.UNLINK);
        items.addAll(ids);
        commandsListHashMap.put(RelCommand.UNLINK, items);
        return this;
    }

    /**
     * (4, id, _)
     * Adds an existing record of id to the set. Can not be used on One2Many.
     *
     * @param id record id to link with set
     * @return chaining method
     */
    public RelValues link(int id) {
        return link(Collections.singletonList(id));
    }

    /**
     * (4, id, _)
     * Adds an existing record of id to the set. Can not be used on One2Many.
     *
     * @param ids record ids to link with set
     * @return chaining method
     */
    public RelValues link(List<Integer> ids) {
        List<Object> items = getCommandList(RelCommand.LINK);
        items.addAll(ids);
        commandsListHashMap.put(RelCommand.LINK, items);
        return this;
    }

    /**
     * (2, id, _)
     * Removes the record of id from the set, then deletes it
     * (from the database). Can not be used in method create().
     *
     * @param id record id to remove
     * @return chaining method
     */
    public RelValues remove(int id) {
        return remove(Collections.singletonList(id));
    }

    /**
     * (2, id, _)
     * Removes the record of id from the set, then deletes it
     * (from the database). Can not be used in method create().
     *
     * @param ids record ids to remove
     * @return chaining method
     */
    public RelValues remove(List<Integer> ids) {
        List<Object> items = getCommandList(RelCommand.REMOVE);
        items.addAll(ids);
        commandsListHashMap.put(RelCommand.REMOVE, items);
        return this;
    }

    /**
     * (6, _, ids)
     * Replaces all existing records in the set by the ids list, equivalent to using the
     * command REMOVE_ALL followed by a command LINK for each id in ids
     *
     * @param ids record ids to replace with
     * @return chaining method
     */
    public RelValues replace(List<Integer> ids) {
        List<Object> items = getCommandList(RelCommand.REPLACE);
        items.addAll(ids);
        commandsListHashMap.put(RelCommand.REPLACE, items);
        return this;
    }

    private List<Object> getCommandList(RelCommand command) {
        if (commandsListHashMap.containsKey(command)) {
            return commandsListHashMap.get(command);
        }
        return new ArrayList<>();
    }

    /**
     * Create JSONArray object to support odoo format from Relation Values
     *
     * @return jsonArray with pairing of commands and values
     */
    public JSONArray getItems() {
        JSONArray items = new JSONArray();
        for (RelCommand command : commandsListHashMap.keySet()) {
            List<Object> values = getCommandList(command);
            switch (command) {
                case ADD: //(1, _, values)
                case UPDATE: //(1, id, values)
                    for (Object obj : values) {
                        OdooValues value = (OdooValues) obj;
                        Object id = 0;
                        if (value.getData().containsKey("_id")) {
                            id = value.getData().get("_id");
                        }
                        items.put(new JSONArray()
                                .put(command.getCommand())
                                .put(id)
                                .put(value.toJSON("_id")));
                    }
                    break;
                case REMOVE: //(2, id, _)
                case UNLINK: //(3, id, _)
                case LINK: //(4, id, _)
                    for (Object obj : values) {
                        items.put(new JSONArray()
                                .put(command.getCommand())
                                .put(obj)
                                .put(0));
                    }
                    break;
                case REMOVE_ALL: //(5, _, _)
                    items.put(new JSONArray().put(command.getCommand()).put(0).put(0));
                    break;
                case REPLACE: //(6, _, ids)
                    List<Integer> ids = new ArrayList<>();
                    for (Object item : getCommandList(command)) {
                        ids.add(Integer.valueOf(item.toString()));
                    }
                    items.put(new JSONArray().put(command.getCommand()).put(0)
                            .put(JSONUtils.arrayToJsonArray(ids.toArray(new Integer[0]))));
                    break;

            }
        }
        return items;
    }
}

