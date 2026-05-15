package gt.edu.umg.repository;

import gt.edu.umg.model.MongoNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoNodeRepository extends MongoRepository<MongoNode, String> {
    // Spring Data Mongo nos da los métodos básicos sin escribir código extra
}