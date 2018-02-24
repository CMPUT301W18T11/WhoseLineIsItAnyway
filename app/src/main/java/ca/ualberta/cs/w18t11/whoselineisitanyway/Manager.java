package ca.ualberta.cs.w18t11.whoselineisitanyway;

import java.util.ArrayList;

public class Manager<T>
{
    private ArrayList<T> items;

    Manager() { items = new ArrayList<T>(); }

    public T get(int index) { return this.items.get(index); }

    public void add(T item) { this.items.add(item); }

    public void set(int index, T value) { this.items.set(index, value); }

    public void delete(int index) { this.items.remove(index); }
}
