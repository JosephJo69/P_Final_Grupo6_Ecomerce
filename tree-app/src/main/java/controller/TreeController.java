package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.tree.engine.model.TreeNode;
import org.tree.engine.service.impl.InMemoryTreeService;
import org.tree.engine.service.TreeService;

/**
 * CONTROLADOR PRINCIPAL DEL API - ESTRATEGIA DE WIRING HÍBRIDO
 * Centraliza las peticiones HTTP delegando la lógica de persistencia al componente
 * activo del ecosistema (Memoria nativa o Base de Datos NoSQL/Relacional).
 */
@RestController
@RequestMapping("/tree")
public class TreeController {

    // ==========================================
    // PERSISTENCIAS
    // ==========================================

    // Componente de acoplamiento dinámico (Manejo Integrante C). 
    // "required = false" previene fallos de inicialización si el sistema se ejecuta en memoria.
    @Autowired(required = false)
    private TreeService databaseService;

    // Instancia de persistencia volátil e independiente por defecto del proyecto.
    private final InMemoryTreeService memoryService = new InMemoryTreeService();

    /**
     * Enrutador lógico de persistencia.
     * Evalúa la presencia de un motor de base de datos inyectado dinámicamente;
     * en su ausencia, conmuta de forma segura hacia la memoria local.
     */
    private TreeService getActiveService() {
        if (databaseService != null) {
            return databaseService;
        }
        return memoryService;
    }

    // ==========================================
    // ENDPOINTS
    // ==========================================

    /*
     * Endpoint para crear la raíz.
     */
    @PostMapping("/root")
    public TreeNode createRoot(@RequestParam String value) {
        // Redirecciona la asignación del nodo principal al servicio activo del ecosistema
        return getActiveService().createRoot(value);
    }

    /*
     * Endpoint para obtener el árbol.
     */
    @GetMapping
    public TreeNode getTree() {
        // Redirecciona la extracción de la estructura jerárquica desde el servicio activo
        return getActiveService().getTree();
    }
}