package net.AJAM.Mapper.test.Utils;

import java.util.LinkedList;
import java.util.List;

public class Thing1 {
    private int Id;
    private String name;
    private LinkedList<Detail1> details;

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

    public LinkedList<Detail1> getDetails() {
        return details;
    }

    public void setDetails(LinkedList<Detail1> details) {
        this.details = details;
    }

    public Thing1() {
    }

    public Thing1(int id, String name, LinkedList<Detail1> details) {
        Id = id;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return "Thing1{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Thing1 thing1 = (Thing1) o;

        if (Id != thing1.Id) return false;
        if (name != null ? !name.equals(thing1.name) : thing1.name != null) return false;
        return details != null ? details.equals(thing1.details) : thing1.details == null;
    }
}
