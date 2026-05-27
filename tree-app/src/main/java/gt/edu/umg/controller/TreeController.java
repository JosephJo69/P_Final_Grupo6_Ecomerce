package gt.edu.umg.controller;

import gt.edu.umg.dto.CreateNodeRequest;
import gt.edu.umg.service.CategoryTreeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.tree.engine.model.CategoryNode;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class TreeController {

    private final CategoryTreeService treeService;

    public TreeController(CategoryTreeService treeService) {
        this.treeService = treeService;
    }

    @PostMapping("/nodes/root")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryNode createRoot(@RequestBody CreateNodeRequest request) {
        return treeService.createRoot(request);
    }

    @PostMapping("/nodes/{parentId}/children")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryNode addChild(
            @PathVariable String parentId,
            @RequestBody CreateNodeRequest request
    ) {
        return treeService.addChild(parentId, request);
    }

    @GetMapping("/tree")
    public List<CategoryNode> getTree() {
        return treeService.getFullTree();
    }

    @GetMapping("/tree/{nodeId}/subtree")
    public List<CategoryNode> getSubtree(@PathVariable String nodeId) {
        return treeService.getSubtree(nodeId);
    }

    @GetMapping("/tree/{nodeId}/path")
    public List<CategoryNode> getPathFromRoot(@PathVariable String nodeId) {
        return treeService.getPathFromRoot(nodeId);
    }

    @GetMapping("/tree/dfs")
    public List<CategoryNode> getDfs() {
        return treeService.getDfs();
    }

    @GetMapping("/tree/bfs")
    public List<CategoryNode> getBfs() {
        return treeService.getBfs();
    }

    @GetMapping("/tree/height")
    public Map<String, Integer> getHeight() {
        return Map.of("height", treeService.getHeight());
    }

    @GetMapping("/tree/{nodeId}/depth")
    public Map<String, Integer> getDepth(@PathVariable String nodeId) {
        return Map.of("depth", treeService.getDepth(nodeId));
    }

    @GetMapping("/tree/{nodeId}/ancestors")
    public List<CategoryNode> getAncestors(@PathVariable String nodeId) {
        return treeService.getAncestors(nodeId);
    }

    @GetMapping("/tree/validate")
    public Map<String, Boolean> validateNoCycles() {
        return Map.of("valid", treeService.validateNoCycles());
    }
}
