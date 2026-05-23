package org.tree.engine;

import org.tree.engine.model.CategoryNode;
import org.tree.engine.model.TreeAlgorithmStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * IMPLEMENTACIÓN CUSTOM - Tarea Integrante A
 * Algoritmos lógicos puros y manuales para la gestión de la estructura del árbol.
 */
public class CustomTreeImpl implements TreeAlgorithmStrategy {

    // Lista que mantiene todos los nodos del árbol en una estructura plana.
    private List<CategoryNode> nodes = new ArrayList<>();

    @Override
    public void setNodes(List<CategoryNode> nodes) {
        this.nodes = (nodes != null) ? nodes : new ArrayList<>();
    }

    @Override
    public CategoryNode createRoot(CategoryNode node) {
        if (node != null) {
            node.setParentId(null); // La raíz principal no tiene padre
            this.nodes.add(node);
        }
        return node;
    }

    @Override
    public CategoryNode addChild(String parentId, CategoryNode child) {
        if (child == null || parentId == null) return null;

        // Validación Manual: Evitar que se inserten IDs duplicados en la lista plana
        for (CategoryNode n : nodes) {
            if (child.getId().equals(n.getId())) {
                System.out.println("Error de Inserción: El ID '" + child.getId() + "' ya existe.");
                return null;
            }
        }

        child.setParentId(parentId);
        this.nodes.add(child);
        return child;
    }

    /**
     * ALGORITMO JEDIS: Construye la estructura jerárquica virtual enlazando los hijos recursivamente.
     */
    @Override
    public List<CategoryNode> getFullTree() {
        List<CategoryNode> roots = new ArrayList<>();

        // 1. Limpiar listas de hijos previas para evitar duplicidad si se refresca la vista
        for (CategoryNode n : nodes) {
            n.setChildren(new ArrayList<>());
        }

        // 2. Buscar las raíces principales (parentId nulo o vacío) y armar sus ramificaciones
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null || n.getParentId().isEmpty()) {
                roots.add(n);
                buildSubtreeTreeRecursive(n);
            }
        }
        return roots;
    }

    private void buildSubtreeTreeRecursive(CategoryNode parent) {
        for (CategoryNode n : nodes) {
            if (parent.getId().equals(n.getParentId())) {
                parent.addChildNode(n);
                buildSubtreeTreeRecursive(n); // Llamada recursiva para seguir bajando en la jerarquía
            }
        }
    }

    // --- MÉTODOS DE RECORRIDO DE ÁRBOLES ---

    @Override
    public List<CategoryNode> getBFS() {
        List<CategoryNode> result = new ArrayList<>();
        if (nodes.isEmpty()) return result;

        // 1. Encontrar el nodo raíz de forma manual
        CategoryNode root = null;
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null) { root = n; break; }
        }
        if (root == null) return result;

        // 2. Implementación de Cola (FIFO) usando un ArrayList básico
        List<CategoryNode> queue = new ArrayList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            CategoryNode current = queue.remove(0); // Saca el primer elemento de la lista (Poll)
            result.add(current);

            // Buscar y encolar los hijos del nodo actual en orden de aparición
            for (CategoryNode n : nodes) {
                if (current.getId().equals(n.getParentId())) {
                    queue.add(n);
                }
            }
        }
        return result;
    }

    @Override
    public List<CategoryNode> getDFS() {
        List<CategoryNode> result = new ArrayList<>();
        CategoryNode root = null;
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null) { root = n; break; }
        }
        if (root == null) return result;

        // Ejecución de DFS mediante tracking recursivo (usa la pila interna del sistema)
        runDFSRecursive(root, result);
        return result;
    }

    private void runDFSRecursive(CategoryNode current, List<CategoryNode> result) {
        result.add(current);
        for (CategoryNode n : nodes) {
            if (current.getId().equals(n.getParentId())) {
                runDFSRecursive(n, result);
            }
        }
    }

    // --- MÉTODOS MÉTRICOS Y MÁXIMOS ---

    @Override
    public int getHeight() {
        CategoryNode root = null;
        for (CategoryNode n : nodes) {
            if (n.getParentId() == null) { root = n; break; }
        }
        if (root == null) return 0;
        return calculateHeightRecursive(root);
    }

    private int calculateHeightRecursive(CategoryNode node) {
        int maxHeight = 0;
        for (CategoryNode n : nodes) {
            if (node.getId().equals(n.getParentId())) {
                int currentHeight = calculateHeightRecursive(n) + 1;
                if (currentHeight > maxHeight) {
                    maxHeight = currentHeight;
                }
            }
        }
        return maxHeight;
    }

    @Override
    public int getDepth(String nodeId) {
        int depth = 0;
        String currentId = nodeId;
        
        while (currentId != null) {
            String tempParentId = null;
            for (CategoryNode n : nodes) {
                if (n.getId().equals(currentId)) {
                    tempParentId = n.getParentId();
                    break;
                }
            }
            if (tempParentId != null) {
                depth++;
                currentId = tempParentId; // Subimos un nivel en el árbol
            } else {
                break;
            }
        }
        return depth;
    }

    // --- MÉTODOS DE RASTREO Y CAMINOS ---

    @Override
    public List<CategoryNode> getAncestors(String nodeId) {
        List<CategoryNode> ancestors = new ArrayList<>();
        String currentParentId = null;

        // Encontrar el ID del padre del nodo inicial
        for (CategoryNode n : nodes) {
            if (n.getId().equals(nodeId)) {
                currentParentId = n.getParentId();
                break;
            }
        }

        // Subir iterativamente recolectando los nodos padre hacia arriba
        while (currentParentId != null) {
            CategoryNode parentNode = null;
            for (CategoryNode n : nodes) {
                if (n.getId().equals(currentParentId)) {
                    parentNode = n;
                    break;
                }
            }
            if (parentNode != null) {
                ancestors.add(parentNode);
                currentParentId = parentNode.getParentId();
            } else {
                break;
            }
        }
        return ancestors;
    }

    @Override
    public List<CategoryNode> getPathFromRoot(String nodeId) {
        List<CategoryNode> path = new ArrayList<>();
        List<CategoryNode> ancestors = getAncestors(nodeId);
        
        // Invertir el orden manualmente (de atrás hacia adelante) para ir de Raíz -> Nodo
        for (int i = ancestors.size() - 1; i >= 0; i--) {
            path.add(ancestors.get(i));
        }
        
        // Añadir el nodo consultado al final del camino
        for (CategoryNode n : nodes) {
            if (n.getId().equals(nodeId)) {
                path.add(n);
                break;
            }
        }
        return path;
    }

    @Override
    public List<CategoryNode> getSubtree(String nodeId) {
        List<CategoryNode> subtree = new ArrayList<>();
        CategoryNode targetNode = null;
        for (CategoryNode n : nodes) {
            if (n.getId().equals(nodeId)) { targetNode = n; break; }
        }
        if (targetNode == null) return subtree;

        // Reutilizamos el recorrido DFS recursivo desde el nodo objetivo para recuperar su subárbol completo
        runDFSRecursive(targetNode, subtree);
        return subtree;
    }

    @Override
    public boolean validateNoCycles() {
        for (CategoryNode n : nodes) {
            List<String> visitedInPath = new ArrayList<>();
            String currentId = n.getId();

            // Rastrear el camino hacia arriba de cada nodo para verificar que no apunte a sí mismo
            while (currentId != null) {
                if (visitedInPath.contains(currentId)) {
                    return false; // Ciclo infinito detectado de forma lógica manual
                }
                visitedInPath.add(currentId);

                String nextParentId = null;
                for (CategoryNode node : nodes) {
                    if (node.getId().equals(currentId)) {
                        nextParentId = node.getParentId();
                        break;
                    }
                }
                currentId = nextParentId;
            }
        }
        return true;
    }
}