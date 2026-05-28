package gt.edu.umg.repository;

import org.tree.engine.model.CategoryNode;

import java.util.List;
import java.util.Optional;

public interface TreeStorageRepository {

    CategoryNode save(CategoryNode node);

    List<CategoryNode> findAll();

    Optional<CategoryNode> findById(String id);
}
