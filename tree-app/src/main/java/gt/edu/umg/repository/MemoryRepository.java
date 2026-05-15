package gt.edu.umg.repository;

import org.tree.engine.model.CategoryNode;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repo en memoria
 * * Esta clase simula nuestra base de datos para la Semana 1.
 * Permite que el Integrante C (Controladores) pueda probar el API 
 * sin esperar a que el Integrante B termine la conexión con PostgreSQL.
 */
@Repository
public class MemoryRepository {
    
    /**
     * Nuestra "tabla" temporal de categorías.
     * Se usa CategoryNode, que es el modelo definido en el módulo 'tree-engine'.
     */
    private final List<CategoryNode> storage = new ArrayList<>();

    /**
     * Guarda una nueva categoría en la lista.
     * @param node El objeto categoría (raíz o hijo) enviado desde el Controller.
     * @return El nodo guardado para confirmar la operación.
     */
    public CategoryNode save(CategoryNode node) {
        storage.add(node);
        return node;
    }

    /**
     * Retorna todos los nodos guardados hasta el momento.
     * Útil para que los motores de búsqueda (Custom/Collections) 
     * obtengan la lista plana y construyan el árbol.
     */
    public List<CategoryNode> findAll() {
        // Retornamos una copia de la lista para proteger la integridad de los datos originales
        return new ArrayList<>(storage);
    }

    /**
     * Busca una categoría específica por su ID único.
     * @param id El identificador de la categoría.
     * @return Un Optional que puede contener el nodo si existe.
     */
    public Optional<CategoryNode> findById(String id) {
        return storage.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }
}