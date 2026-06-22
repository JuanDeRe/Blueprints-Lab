package edu.eci.arsw.blueprints.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "blueprints")
@IdClass(BlueprintId.class)
public class BlueprintEntity {

    @Id
    @Column(nullable = false)
    private String author;

    @Id
    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "blueprint_author", referencedColumnName = "author"),
            @JoinColumn(name = "blueprint_name", referencedColumnName = "name")
    })
    @OrderColumn(name = "point_order")
    private List<PointEntity> points = new ArrayList<>();

    public BlueprintEntity() {
    }

    public BlueprintEntity(String author, String name) {
        this.author = author;
        this.name = name;
    }

    public BlueprintEntity(String author, String name, List<PointEntity> points) {
        this.author = author;
        this.name = name;
        if (points != null) {
            this.points.addAll(points);
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public List<PointEntity> getPoints() {
        return points;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(List<PointEntity> points) {
        this.points.clear();

        if (points != null) {
            this.points.addAll(points);
        }
    }

    public void addPoint(PointEntity point) {
        this.points.add(point);
    }

    public void removePoint(PointEntity point) {
        this.points.remove(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlueprintEntity)) return false;
        BlueprintEntity that = (BlueprintEntity) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }
}
