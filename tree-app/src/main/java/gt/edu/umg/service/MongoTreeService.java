package gt.edu.umg.service;

import gt.edu.umg.model.MongoNode;
import gt.edu.umg.repository.MongoNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.tree.engine.model.TreeNode;
import org.tree.engine.service.TreeService;

import java.util.List;

/**
 * SERVICIO DE PERSISTENCIA MONGODB - TRABAJO INTEGRANTE C
 * Implementa la interfaz genérica 'TreeService' de los compañeros del motor matemático,
 * permitiendo una integración y desacoplamiento completo de la base de datos NoSQL.
 */
@Service
// CONFIGURACIÓN DINÁMICA DE WIRING: Esta clase de servicio solo se cargará en el contexto
// de Spring Boot si la propiedad 'app.storage' en application.properties es igual a 'mongo'
@ConditionalOnProperty(name = "app.storage", havingValue = "mongo")
public class MongoTreeService implements TreeService {

    @Autowired
    private MongoNodeRepository repository;

    /**
     * Inicializa una nueva jerarquía de árbol en MongoDB estableciendo el nodo raíz.
     * CUMPLE CON LA REGLA: Los datos se guardan de forma plana.
     */
    @Override
    public TreeNode createRoot(String value) {
        // Limpieza preventiva de la colección para asegurar un nuevo estado del árbol
        repository.deleteAll();

        // Persistencia del nodo en estructura plana NoSQL (sin identificador de padre)
        MongoNode mongoRoot = new MongoNode(value, null);
        MongoNode savedMongoNode = repository.save(mongoRoot);

        // Mapeo y retorno al objeto de transferencia 'TreeNode' que utiliza el controlador
        TreeNode treeNodeRoot = new TreeNode();
        treeNodeRoot.setValue(savedMongoNode.getValue());

        return treeNodeRoot;
    }

    /**
     * Recupera el estado plano del árbol desde MongoDB.
     * CUMPLE CON LA REGLA: La base de datos no calcula ni procesa jerarquías de árbol;
     * únicamente provee la colección plana para que el motor matemático la reconstruya.
     */
    @Override
    public TreeNode getTree() {
        // Extracción de la lista secuencial y plana de todos los documentos indexados
        List<MongoNode> allNodes = repository.findAll();

        // Instanciación del nodo contenedor para el ecosistema del API
        TreeNode rootTemplate = new TreeNode();
        if (!allNodes.isEmpty()) {
            // Nota de Integración: Aquí se acoplará la estrategia de algoritmos
            // compañeros para inyectar la lista plana y estructurar los sub-nodos (hijos)
            rootTemplate.setValue(allNodes.get(0).getValue());
        }
        
        return rootTemplate;
    }
}