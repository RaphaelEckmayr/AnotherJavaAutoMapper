package net.AJAM.Mapper.test.Utils;

import org.junit.jupiter.api.AfterAll;

public class Detail1 {
    private int id;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Detail1(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Detail1() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Detail1 detail1 = (Detail1) o;

        if (id != detail1.id) return false;
        return description != null ? description.equals(detail1.description) : detail1.description == null;
    }
}
