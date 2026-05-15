package gt.edu.umg.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que representa la estructura documental de un nodo en MongoDB.
 * Implementa la estrategia de persistencia para el Integrante C.
 */
@Document(collection = "nodes")
public class MongoNode {
    
    @Id
    private String id; // Identificador único generado por MongoDB
    
    private String value; // Valor o nombre que contiene el nodo
    
    // Mapeo del ID padre para reconstruir la estructura jerárquica del árbol
    private String parentId; 

    // Constructores base para la persistencia
    public MongoNode() {}

    public MongoNode(String value, String parentId) {
        this.value = value;
        this.parentId = parentId;
    }

    // Métodos de acceso y modificación
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
}