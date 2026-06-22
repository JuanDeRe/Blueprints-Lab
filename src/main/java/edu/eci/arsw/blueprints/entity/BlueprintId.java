package edu.eci.arsw.blueprints.entity;


import java.io.Serializable;
import java.util.Objects;

public class BlueprintId implements Serializable {

    private String author;
    private String name;

    public BlueprintId() {
    }

    public BlueprintId(String author, String name) {
        this.author = author;
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlueprintId)) return false;
        BlueprintId that = (BlueprintId) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }
}
