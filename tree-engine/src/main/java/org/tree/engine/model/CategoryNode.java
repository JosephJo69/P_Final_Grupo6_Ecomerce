package org.tree.engine.model;

/**
 * Clase que representa una categoría en el sistema de E-commerce.
 * Es el modelo de datos básico para construir la estructura jerárquica.
 */
public class CategoryNode {
    private String id;
    private String name;
    private String description;
    private String parentId; // Referencia fundamental para la persistencia y reconstrucción del árbol

    // Constructor vacío (necesario para frameworks como Jackson/Spring)
    public CategoryNode() {
    }

    // Constructor con campos
    public CategoryNode(String id, String name, String parentId, String description) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.description = description;
    }

    // Getters y Setters. Estos son placeholders funcionales, si alguno de ustedes 
    //tiene una mejor idea de cómo estructurar esto, por favor siéntase libre de modificarlo y comentarlo en el grupo.
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CategoryNode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}