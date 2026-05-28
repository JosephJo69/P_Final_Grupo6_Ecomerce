# Estrategia Collections — Integrante B

## ¿Qué es?
Implementación del motor de algoritmos del árbol usando 
las colecciones del JDK de Java.

## Colecciones usadas

| Operación | Colección | Por qué |
|-----------|-----------|---------|
| BFS | ArrayDeque como cola | Procesa nodos nivel por nivel |
| DFS | ArrayDeque como pila | Baja por rama hasta el fondo |
| getHeight | HashMap | Mapea parentId a hijos |
| getDepth | HashMap | Mapea id a parentId |
| getAncestors | HashMap | Búsqueda rápida por id |
| validateNoCycles | HashSet | Detecta nodos ya visitados |

## Diferencia con Custom
Custom construye sus propias estructuras desde cero.
Collections usa las herramientas que ya trae Java.
Ambas dan los mismos resultados.

## Validación cruzada
BFS custom vs collections → mismo orden ✅
Height custom vs collections → mismo resultado ✅


## Datos de prueba

Árbol de categorías e-commerce con 3 niveles:
### Endpoints probados
- POST /nodes/root → crea Electrónica
- POST /nodes/{id}/children → agrega Computadoras y Celulares
- GET /tree/bfs → recorre nivel por nivel
- GET /tree/dfs → recorre por ramas
- GET /tree/height → devuelve 2