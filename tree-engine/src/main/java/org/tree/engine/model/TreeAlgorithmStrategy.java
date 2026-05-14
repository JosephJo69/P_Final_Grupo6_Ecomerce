package org.tree.engine.model;

import org.tree.engine.model.CategoryNode;
import java.util.List;

/**
 * Interfaz obligatoria para el motor de algoritmos.
 * Define las 11 operaciones que el backend requiere.
 */
public interface TreeAlgorithmStrategy {

    // Método para cargar la lista plana de nodos desde la persistencia
    void setNodes(List<CategoryNode> nodes);

    // 1. Crear raíz
    CategoryNode createRoot(CategoryNode node);

    // 2. Agregar hijo
    CategoryNode addChild(String parentId, CategoryNode child);

    // 3. Obtener árbol completo (estructura jerárquica)
    List<CategoryNode> getFullTree();

    // 4. Obtener subárbol
    List<CategoryNode> getSubtree(String nodeId);

    // 5. Ruta desde raíz a un nodo
    List<CategoryNode> getPathFromRoot(String nodeId);

 // 6. Recorrido DFS: Explora cada rama hasta el final antes de pasar a la siguiente.
    
    List<CategoryNode> getDFS();

 // 7. Recorrido BFS: Explora el árbol nivel por nivel (primero todos los hijos, luego todos los nietos).
   
    List<CategoryNode> getBFS();

    // 8. Altura del árbol
    int getHeight();

    // 9. Profundidad de un nodo
    int getDepth(String nodeId);

    // 10. Ancestros de un nodo
    List<CategoryNode> getAncestors(String nodeId);

    // 11. Validar que no haya ciclos
    boolean validateNoCycles();
}