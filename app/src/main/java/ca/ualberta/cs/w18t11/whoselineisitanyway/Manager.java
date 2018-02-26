package ca.ualberta.cs.w18t11.whoselineisitanyway;

import java.util.ArrayList;

final class Manager<T>
{
    private final ArrayList<T> items = new ArrayList<>();

    T get(final int index)
    {
        return this.items.get(index);
    }

    void add(final T item)
    {
        this.items.add(item);
    }

    void set(final int index, final T value)
    {
        this.items.set(index, value);
    }

    void delete(final int index)
    {
        this.items.remove(index);
    }
}
