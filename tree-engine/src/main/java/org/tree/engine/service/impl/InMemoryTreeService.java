package org.tree.engine.service.impl;


import java.util.UUID;
import org.tree.engine.model.TreeNode;
import org.tree.engine.service.TreeService;

/*
 * Implementación del servicio del árbol en memoria.
 * 
 * Esta clase permite:
 * - Crear la raíz del árbol.
 * - Obtener el árbol actual.
 * 
 * Los datos se almacenan temporalmente en memoria,
 * por lo que se pierden al detener la aplicación.
 */
public class InMemoryTreeService implements TreeService {

    /*
     * Variable que almacena la raíz del árbol.
     */
    private TreeNode root;

    /*
     * Método para crear la raíz del árbol.
     * 
     * Recibe un valor y genera automáticamente
     * un identificador único usando UUID.
     */
    @Override
    public TreeNode createRoot(String value) {

        /*
         * Se crea un nuevo nodo raíz con:
         * - Un ID único.
         * - El valor recibido.
         */
        root = new TreeNode(
                UUID.randomUUID().toString(),
                value
        );

        /*
         * Retorna el nodo raíz creado.
         */
        return root;
    }

    /*
     * Método para obtener el árbol actual.
     * 
     * Retorna la raíz almacenada en memoria.
     */
    @Override
    public TreeNode getTree() {

        return root;
    }
}