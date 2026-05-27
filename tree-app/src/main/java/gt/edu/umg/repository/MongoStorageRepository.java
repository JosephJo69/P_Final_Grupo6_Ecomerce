package gt.edu.umg.repository;

import gt.edu.umg.model.MongoNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.tree.engine.model.CategoryNode;

import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoStorageRepository implements TreeStorageRepository {

    private final MongoNodeRepository mongoRepository;

    public MongoStorageRepository(MongoNodeRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public CategoryNode save(CategoryNode node) {
        MongoNode mongoNode = toMongoNode(node);
        MongoNode saved = mongoRepository.save(mongoNode);
        return toCategoryNode(saved);
    }

    @Override
    public List<CategoryNode> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toCategoryNode)
                .toList();
    }

    @Override
    public Optional<CategoryNode> findById(String id) {
        return mongoRepository.findById(id).map(this::toCategoryNode);
    }

    private MongoNode toMongoNode(CategoryNode node) {
        MongoNode mongoNode = new MongoNode();
        mongoNode.setId(node.getId());
        mongoNode.setName(node.getName());
        mongoNode.setDescription(node.getDescription());
        mongoNode.setParentId(node.getParentId());
        return mongoNode;
    }

    private CategoryNode toCategoryNode(MongoNode mongoNode) {
        CategoryNode node = new CategoryNode();
        node.setId(mongoNode.getId());
        node.setName(mongoNode.getName());
        node.setDescription(mongoNode.getDescription());
        node.setParentId(mongoNode.getParentId());
        return node;
    }
}
