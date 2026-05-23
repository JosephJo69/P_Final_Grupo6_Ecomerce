package org.tree.engine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una categoría en el sistema de E-commerce.
 * Es el modelo de datos básico para construir la estructura jerárquica.
 */
public class CategoryNode {
    private String id;
    private String name;
    private String description;
    private String parentId; // Referencia fundamental para la persistencia y reconstrucción del árbol

    // ATRIBUTO AGREGADO: Lista interna para almacenar las referencias a sus nodos hijos
    private List<CategoryNode> children = new ArrayList<>();

    // Constructor vacío (necesario para frameworks como Jackson/Spring)
    public CategoryNode() {
        this.children = new ArrayList<>();
    }

    // Constructor con campos
    public CategoryNode(String id, String name, String parentId, String description) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.description = description;
        this.children = new ArrayList<>();
    }

    // --- MÉTODOS DE LA SEMANA 2: Gestión de nodos hijos ---

    public List<CategoryNode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<CategoryNode> children) {
        this.children = (children != null) ? children : new ArrayList<>();
    }

    public void addChildNode(CategoryNode child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    // --- Getters y Setters Estándar ---

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