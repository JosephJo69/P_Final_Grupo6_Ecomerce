package gt.edu.umg.service;

import gt.edu.umg.dto.CreateNodeRequest;
import gt.edu.umg.repository.TreeStorageRepository;
import org.springframework.stereotype.Service;
import org.tree.engine.model.CategoryNode;
import org.tree.engine.model.TreeAlgorithmStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryTreeService {

    private final TreeStorageRepository repository;
    private final TreeAlgorithmStrategy strategy;

    public CategoryTreeService(TreeStorageRepository repository, TreeAlgorithmStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;
    }

    public CategoryNode createRoot(CreateNodeRequest request) {
        CategoryNode node = buildNode(request, null);
        reloadStrategy();
        CategoryNode created = strategy.createRoot(node);
        return repository.save(created);
    }

    public CategoryNode addChild(String parentId, CreateNodeRequest request) {
        repository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent node not found: " + parentId));

        CategoryNode child = buildNode(request, parentId);
        reloadStrategy();
        CategoryNode created = strategy.addChild(parentId, child);
        if (created == null) {
            throw new IllegalArgumentException("Child node could not be created");
        }
        return repository.save(created);
    }

    public List<CategoryNode> getFullTree() {
        reloadStrategy();
        return strategy.getFullTree();
    }

    public List<CategoryNode> getSubtree(String nodeId) {
        reloadStrategy();
        return strategy.getSubtree(nodeId);
    }

    public List<CategoryNode> getPathFromRoot(String nodeId) {
        reloadStrategy();
        return strategy.getPathFromRoot(nodeId);
    }

    public List<CategoryNode> getDfs() {
        reloadStrategy();
        return strategy.getDFS();
    }

    public List<CategoryNode> getBfs() {
        reloadStrategy();
        return strategy.getBFS();
    }

    public int getHeight() {
        reloadStrategy();
        return strategy.getHeight();
    }

    public int getDepth(String nodeId) {
        reloadStrategy();
        return strategy.getDepth(nodeId);
    }

    public List<CategoryNode> getAncestors(String nodeId) {
        reloadStrategy();
        return strategy.getAncestors(nodeId);
    }

    public boolean validateNoCycles() {
        reloadStrategy();
        return strategy.validateNoCycles();
    }

    private void reloadStrategy() {
        strategy.setNodes(new ArrayList<>(repository.findAll()));
    }

    private CategoryNode buildNode(CreateNodeRequest request, String parentId) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Node name is required");
        }

        CategoryNode node = new CategoryNode();
        node.setId(UUID.randomUUID().toString());
        node.setName(request.getName());
        node.setDescription(request.getDescription());
        node.setParentId(parentId);
        return node;
    }
}
