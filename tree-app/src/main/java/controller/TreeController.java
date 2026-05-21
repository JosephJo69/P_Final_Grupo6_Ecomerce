package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.tree.engine.model.TreeNode;
import org.tree.engine.service.impl.InMemoryTreeService;

/*
 * Controlador REST para manejar
 * las operaciones del árbol.
 */
@RestController
@RequestMapping("/tree")
public class TreeController {

    // Servicio del árbol en memoria
    private final InMemoryTreeService treeService =
            new InMemoryTreeService();

    /*
     * Endpoint para crear la raíz.
     */
    @PostMapping("/root")
    public TreeNode createRoot(
            @RequestParam String value
    ) {

        return treeService.createRoot(value);
    }

    /*
     * Endpoint para obtener el árbol.
     */
    @GetMapping
    public TreeNode getTree() {

        return treeService.getTree();
    }
}