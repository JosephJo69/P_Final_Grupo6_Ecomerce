package org.tree.engine;

import org.tree.engine.model.CategoryNode;
import org.tree.engine.model.TreeAlgorithmStrategy;
import java.util.*;

/**
 * IMPLEMENTACIÓN COLLECTIONS - Tarea Integrante B
 * Usa estructuras del JDK: HashMap, ArrayDeque, etc.
 */
public class CollectionsTreeStrategy implements TreeAlgorithmStrategy {

    private List<CategoryNode> nodes = new ArrayList<>();

    @Override
    public void setNodes(List<CategoryNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public CategoryNode createRoot(CategoryNode node) {
        this.nodes.add(node);
        return node;
    }

    @Override
    public CategoryNode addChild(String parentId, CategoryNode child) {
        child.setParentId(parentId);
        this.nodes.add(child);
        return child;
    }

    @Override
    public List<CategoryNode> getFullTree() {
        return new ArrayList<>(nodes);
    }

    // BFS usando ArrayDeque como cola
    @Override
    public List<CategoryNode> getBFS() {
        List<CategoryNode> result = new ArrayList<>();
        if (nodes.isEmpty()) return result;

        // Construimos mapa de id -> nodo
        Map<String, CategoryNode> map = new HashMap<>();
        for (CategoryNode n : nodes) map.put(n.getId(), n);

        // Buscamos la raíz (el que no tiene parentId)
        CategoryNode root = null;
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null) { root = n; break; }
        }
        if (root == null) return result;

        // Construimos mapa de parentId -> hijos
        Map<String, List<CategoryNode>> children = new HashMap<>();
        for (CategoryNode n : nodes) {
            String pid = n.getParentId();
            if (pid != null) {
                children.computeIfAbsent(pid, k -> new ArrayList<>()).add(n);
            }
        }

        // BFS con cola
        ArrayDeque<CategoryNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            CategoryNode current = queue.poll();
            result.add(current);
            List<CategoryNode> hijos = children.getOrDefault(current.getId(), new ArrayList<>());
            queue.addAll(hijos);
        }
        return result;
    }

    // DFS usando ArrayDeque como pila
    @Override
    public List<CategoryNode> getDFS() {
        List<CategoryNode> result = new ArrayList<>();
        if (nodes.isEmpty()) return result;

        // Construimos mapa de parentId -> hijos
        Map<String, List<CategoryNode>> children = new HashMap<>();
        CategoryNode root = null;
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null) root = n;
            else children.computeIfAbsent(n.getParentId(), k -> new ArrayList<>()).add(n);
        }
        if (root == null) return result;

        // DFS con pila
        ArrayDeque<CategoryNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            CategoryNode current = stack.pop();
            result.add(current);
            List<CategoryNode> hijos = children.getOrDefault(current.getId(), new ArrayList<>());
            // Invertimos para mantener orden izquierda-derecha
            ListIterator<CategoryNode> it = hijos.listIterator(hijos.size());
            while (it.hasPrevious()) stack.push(it.previous());
        }
        return result;
    }

    @Override
    public int getHeight() {
        if (nodes.isEmpty()) return 0;
        Map<String, List<CategoryNode>> children = new HashMap<>();
        CategoryNode root = null;
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null) root = n;
            else children.computeIfAbsent(n.getParentId(), k -> new ArrayList<>()).add(n);
        }
        if (root == null) return 0;
        return calcHeight(root, children);
    }

    private int calcHeight(CategoryNode node, Map<String, List<CategoryNode>> children) {
        List<CategoryNode> hijos = children.getOrDefault(node.getId(), new ArrayList<>());
        if (hijos.isEmpty()) return 0;
        int max = 0;
        for (CategoryNode hijo : hijos) {
            max = Math.max(max, calcHeight(hijo, children));
        }
        return max + 1;
    }

    @Override
    public int getDepth(String nodeId) {
        Map<String, String> parentMap = new HashMap<>();
        for (CategoryNode n : nodes) {
            if (n.getParentId() != null) parentMap.put(n.getId(), n.getParentId());
        }
        int depth = 0;
        String current = nodeId;
        while (parentMap.containsKey(current)) {
            current = parentMap.get(current);
            depth++;
        }
        return depth;
    }

    @Override
    public List<CategoryNode> getAncestors(String nodeId) {
        Map<String, CategoryNode> map = new HashMap<>();
        for (CategoryNode n : nodes) map.put(n.getId(), n);
        List<CategoryNode> ancestors = new ArrayList<>();
        String currentId = nodeId;
        CategoryNode current = map.get(currentId);
        while (current != null && current.getParentId() != null) {
            current = map.get(current.getParentId());
            if (current != null) ancestors.add(current);
        }
        return ancestors;
    }

    @Override
    public List<CategoryNode> getPathFromRoot(String nodeId) {
        List<CategoryNode> ancestors = getAncestors(nodeId);
        Collections.reverse(ancestors);
        Map<String, CategoryNode> map = new HashMap<>();
        for (CategoryNode n : nodes) map.put(n.getId(), n);
        if (map.containsKey(nodeId)) ancestors.add(map.get(nodeId));
        return ancestors;
    }

    @Override
    public List<CategoryNode> getSubtree(String nodeId) {
        Map<String, List<CategoryNode>> children = new HashMap<>();
        for (CategoryNode n : nodes) {
            if (n.getParentId() != null)
                children.computeIfAbsent(n.getParentId(), k -> new ArrayList<>()).add(n);
        }
        Map<String, CategoryNode> map = new HashMap<>();
        for (CategoryNode n : nodes) map.put(n.getId(), n);
        List<CategoryNode> result = new ArrayList<>();
        if (!map.containsKey(nodeId)) return result;
        ArrayDeque<CategoryNode> queue = new ArrayDeque<>();
        queue.add(map.get(nodeId));
        while (!queue.isEmpty()) {
            CategoryNode current = queue.poll();
            result.add(current);
            queue.addAll(children.getOrDefault(current.getId(), new ArrayList<>()));
        }
        return result;
    }

    @Override
    public boolean validateNoCycles() {
        Map<String, String> parentMap = new HashMap<>();
        for (CategoryNode n : nodes) {
            if (n.getParentId() != null) parentMap.put(n.getId(), n.getParentId());
        }
        for (CategoryNode n : nodes) {
            Set<String> visited = new HashSet<>();
            String current = n.getId();
            while (parentMap.containsKey(current)) {
                if (visited.contains(current)) return false;
                visited.add(current);
                current = parentMap.get(current);
            }
        }
        return true;
    }
}