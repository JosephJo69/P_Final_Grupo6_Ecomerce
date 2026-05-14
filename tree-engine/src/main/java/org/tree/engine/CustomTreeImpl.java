package org.tree.engine;

import org.tree.engine.model.CategoryNode;
import org.tree.engine.model.TreeAlgorithmStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * IMPLEMENTACIÓN CUSTOM (ESQUELETO) - Tarea Integrante A
 * Esta clase es el "motor" donde programaremos nuestros propios algoritmos.
 * Implementa la interfaz TreeAlgorithmStrategy para asegurar que cumplimos 
 * con los 11 métodos obligatorios del proyecto.
 */
public class CustomTreeImpl implements TreeAlgorithmStrategy {

    // Lista que mantiene todos los nodos del árbol en una estructura plana.
    // Se utiliza para facilitar la manipulación antes de construir la jerarquía.
    private List<CategoryNode> nodes = new ArrayList<>();

    /**
     * Inyecta una lista de nodos externa al motor.
     * Útil cuando recuperamos datos de la base de datos o el repo en memoria.
     */
    @Override
    public void setNodes(List<CategoryNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * Define el punto de partida del árbol.
     * En esta fase inicial, simplemente lo agrega a la lista de gestión.
     */
    @Override
    public CategoryNode createRoot(CategoryNode node) {
        // Lógica: se asume que este nodo no tiene parentId
        this.nodes.add(node);
        return node;
    }

    /**
     * Conecta un nuevo nodo (hijo) con un nodo existente (padre).
     * @param parentId ID del nodo superior.
     * @param child El nuevo nodo que se desea insertar.
     */
    @Override
    public CategoryNode addChild(String parentId, CategoryNode child) {
        // Asignamos la relación jerárquica mediante el ID del padre
        child.setParentId(parentId);
        this.nodes.add(child);
        return child;
    }

    /**
     * Retorna la lista completa de nodos. 
     * En fases posteriores, este método debería devolver la estructura 
     * ya armada visualmente (árbol jerárquico).
     */
    @Override
    public List<CategoryNode> getFullTree() {
        return nodes;
    }

   //Estos metodos creo que aun no los vamos a usar, pero los dejo implementados para que no haya errores de compilacion y que 
    //el esqueleto este completo.
    @Override public List<CategoryNode> getSubtree(String nodeId) { return new ArrayList<>(); }
    @Override public List<CategoryNode> getPathFromRoot(String nodeId) { return new ArrayList<>(); }
    @Override public List<CategoryNode> getDFS() { return nodes; }
    @Override public List<CategoryNode> getBFS() { return nodes; }
    @Override public int getHeight() { return 0; }
    @Override public int getDepth(String nodeId) { return 0; }
    @Override public List<CategoryNode> getAncestors(String nodeId) { return new ArrayList<>(); }
    @Override public boolean validateNoCycles() { return true; }
}