package gt.edu.umg.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "nodes")
public class MongoNode {
    
    @Id
    private String id;
    private String value;
    private String parentId; // Clave para armar el árbol en Mongo

    // Constructores
    public MongoNode() {}

    public MongoNode(String value, String parentId) {
        this.value = value;
        this.parentId = parentId;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
}