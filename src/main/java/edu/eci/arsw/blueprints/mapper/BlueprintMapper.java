package edu.eci.arsw.blueprints.mapper;

import edu.eci.arsw.blueprints.entity.BlueprintEntity;
import edu.eci.arsw.blueprints.entity.PointEntity;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlueprintMapper {

    public static BlueprintEntity toEntity(Blueprint blueprint) {
        if (blueprint == null) {
            return null;
        }

        List<PointEntity> pointEntities = blueprint.getPoints()
                .stream()
                .map(BlueprintMapper::toEntity)
                .collect(Collectors.toList());

        return new BlueprintEntity(
                blueprint.getAuthor(),
                blueprint.getName(),
                pointEntities
        );
    }

    public static Blueprint toDomain(BlueprintEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Point> points = entity.getPoints()
                .stream()
                .map(BlueprintMapper::toDomain)
                .collect(Collectors.toList());

        return new Blueprint(
                entity.getAuthor(),
                entity.getName(),
                points
        );
    }

    public static PointEntity toEntity(Point point) {
        if (point == null) {
            return null;
        }

        return new PointEntity(
                point.x(),
                point.y()
        );
    }

    public static Point toDomain(PointEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Point(
                entity.getX(),
                entity.getY()
        );
    }

    public static List<BlueprintEntity> toEntityList(List<Blueprint> blueprints) {
        if (blueprints == null) {
            return List.of();
        }

        return blueprints.stream()
                .map(BlueprintMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static List<Blueprint> toDomainList(List<BlueprintEntity> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(BlueprintMapper::toDomain)
                .collect(Collectors.toList());
    }
}