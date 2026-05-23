package org.tree.engine.model;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class TreeNode {

	private String id;
	private String value; //valor o  nombre del nodo
	@JsonIgnore
	private TreeNode parent;
	private List<TreeNode> children = new ArrayList<>();// Lsita d hijos del nodo actual, se inicializa vacía automaticamente
public TreeNode() {
	//permite crear un nodo sin especificar id o valor, se pueden establecer posteriormente usando los setters
}
// Al crear un nodo, se asigna un id y un valor, el id es único para cada nodo y el valor puede ser cualquier información relevante para ese nodo
public TreeNode(String id, String value) {
	this.id = id;
	this.value = value;
}
// Agrega un hijo al nodo actual, establece la referencia al padre del hijo y lo añade a la lista de hijos del nodo actual
public void addChild(TreeNode child) {
	child.setParent(this);
	children.add(child);
}
// Elimina un hijo del nodo actual, elimina la referencia al padre del hijo y lo elimina de la lista de hijos del nodo actual
public String getId() {
	return id;
}
// Permite establecer el id del nodo, el id es una cadena única que identifica al nodo dentro del árbol
public void setId(String id) {
	this.id = id;

}
// Permite obtener el valor del nodo, el valor es una cadena que puede contener cualquier información relevante para ese nodo
public String getValue() {
	return value;
}
// Permite establecer el valor del nodo, el valor es una cadena que puede contener cualquier información relevante para ese nodo
public void setValue(String value) {
	this.value = value;

}
// Permite obtener el nodo padre del nodo actual, devuelve una referencia al nodo padre o null si el nodo es la raíz del árbol
public TreeNode getParent() {
	return parent;


}
// Permite establecer el nodo padre del nodo actual, asigna una referencia al nodo padre, esto es útil para mantener la estructura del árbol y facilitar la navegación entre nodos
public void setParent(TreeNode parent) {
	this.parent = parent;


}
// Permite obtener la lista de hijos del nodo actual, devuelve una lista de nodos hijos o una lista vacía si el nodo no tiene hijos
public List<TreeNode> getChildren() {
	return children;
}
// Permite establecer la lista de hijos del nodo actual, asigna una lista de nodos hijos, esto es útil para mantener la estructura del árbol y facilitar la navegación entre nodos
public void setChildren(List<TreeNode> children) {
	this.children = children;
}
}
