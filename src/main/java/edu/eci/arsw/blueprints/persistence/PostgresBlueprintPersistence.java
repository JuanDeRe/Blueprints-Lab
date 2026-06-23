package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.entity.BlueprintEntity;
import edu.eci.arsw.blueprints.entity.BlueprintId;
import edu.eci.arsw.blueprints.entity.PointEntity;
import edu.eci.arsw.blueprints.mapper.BlueprintMapper;
import edu.eci.arsw.blueprints.model.Blueprint;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface PostgresBlueprintPersistence extends JpaRepository<BlueprintEntity, BlueprintId>, BlueprintPersistence {

    @Override
    @Transactional
    default void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        String author = bp.getAuthor();
        String name = bp.getName();

        BlueprintId id = new BlueprintId(author, name);

        if (existsById(id)) {
            throw new BlueprintPersistenceException("Blueprint already exists: " + author + ":" + name);
        }

        try {
            save(BlueprintMapper.toEntity(bp));
        } catch (DataIntegrityViolationException e) {
            throw new BlueprintPersistenceException("Blueprint already exists or violates database constraints: " + author + ":" + name);
        } catch (RuntimeException e) {
            throw new BlueprintPersistenceException("Error saving blueprint: " + author + ":" + name);
        }
    }

    @Override
    default Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        BlueprintEntity entity = findById(new BlueprintId(author, name))
                .orElseThrow(() -> new BlueprintNotFoundException(
                        "Blueprint not found: %s/%s".formatted(author, name)
                ));

        return BlueprintMapper.toDomain(entity);
    }

    @Override
    default Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        List<BlueprintEntity> entities = findAllByAuthor(author);

        if (entities.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints for author: " + author);
        }

        return Set.copyOf(BlueprintMapper.toDomainList(entities));
    }

    @Override
    default Set<Blueprint> getAllBlueprints() {
        return Set.copyOf(BlueprintMapper.toDomainList(findAll()));
    }

    @Override
    @Transactional
    default void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        BlueprintEntity entity = findById(new BlueprintId(author, name))
                .orElseThrow(() -> new BlueprintNotFoundException(
                        "Blueprint not found: %s/%s".formatted(author, name)
                ));

        PointEntity point = new PointEntity(x, y);
        entity.addPoint(point);

        save(entity);
    }

    List<BlueprintEntity> findAllByAuthor(String author);

    BlueprintEntity findByAuthorAndName(String author, String bpname);

}