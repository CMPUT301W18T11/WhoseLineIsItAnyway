package ca.ualberta.cs.w18t11.whoselineisitanyway.model;

import java.util.ArrayList;

/**
 * Manager is a generic class for managing an array of objects
 *
 * @param <T> The object type in the array
 */
public final class Manager<T>
{
    private final ArrayList<T> items = new ArrayList<>();

    /**
     * Get an item in the array
     *
     * @param index position of object in the array
     * @return object at the specified index
     */
    public T get(final int index)
    {
        return this.items.get(index);
    }

    /**
     * Add an object to the array
     *
     * @param item to be added
     */
    public void add(final T item)
    {
        this.items.add(item);
    }

    /**
     * Set the value of an object in the array
     *
     * @param index position of object in the array
     * @param value object to be set at index
     */
    public void set(final int index, final T value)
    {
        this.items.set(index, value);
    }

    /**
     * Remove an object from the array
     *
     * @param index position of object in the array
     */
    public void delete(final int index)
    {
        this.items.remove(index);
    }
}
