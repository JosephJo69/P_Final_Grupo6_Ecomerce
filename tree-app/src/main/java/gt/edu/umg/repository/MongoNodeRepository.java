package gt.edu.umg.repository;

import gt.edu.umg.model.MongoNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoNodeRepository extends MongoRepository<MongoNode, String> {
    
    // 1. Busca todos los nodos hijos de una categoría específica
    List<MongoNode> findByParentId(String parentId);
    
    // 2. Busca la categoría principal o "Raíz" (la única que no tiene padre)
    Optional<MongoNode> findByParentIdIsNull();
}