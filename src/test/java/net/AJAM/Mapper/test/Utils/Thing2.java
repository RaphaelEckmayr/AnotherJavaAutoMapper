package net.AJAM.Mapper.test.Utils;

import java.util.LinkedList;
import java.util.List;

public class Thing2
{
    private int Id;
    private String name;
    private LinkedList<Detail2> details;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Thing2() {
    }

    public LinkedList<Detail2> getDetails() {
        return details;
    }

    public void setDetails(LinkedList<Detail2> details) {
        this.details = details;
    }

    public Thing2(int id, String name, LinkedList<Detail2> details) {
        Id = id;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return "Thing2{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Thing2 thing2 = (Thing2) o;

        if (Id != thing2.Id) return false;
        if (name != null ? !name.equals(thing2.name) : thing2.name != null) return false;
        return details != null ? details.equals(thing2.details) : thing2.details == null;
    }
}
