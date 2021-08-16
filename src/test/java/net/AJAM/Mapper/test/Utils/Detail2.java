package net.AJAM.Mapper.test.Utils;

public class Detail2 {
    private int id;
    private String description;

    public Detail2(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public Detail2() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Detail2 detail2 = (Detail2) o;

        if (id != detail2.id) return false;
        return description != null ? description.equals(detail2.description) : detail2.description == null;
    }
}
