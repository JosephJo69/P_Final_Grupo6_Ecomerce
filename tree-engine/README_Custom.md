# Estrategia Custom — Árbol sin librerías externas

**Responsable:** Integrante A  
**Módulo:** `tree-engine`  
**Activación:** `app.tree-strategy=custom`

---

## Descripción

Implementación manual de los algoritmos del árbol genérico usando únicamente clases propias
y el JDK base de Java. No se usa ninguna librería externa ni estructura especializada del
framework para modelar el árbol — la jerarquía se construye con objetos `CategoryNode`
enlazados entre sí mediante listas de hijos simples (`ArrayList`).

---

## Clases principales

### `CategoryNode`
Modelo de nodo del árbol. Cada nodo contiene:

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `String` | Identificador único del nodo (UUID) |
| `name` | `String` | Nombre de la categoría |
| `description` | `String` | Descripción opcional |
| `parentId` | `String` | ID del nodo padre (`null` si es raíz) |
| `children` | `List<CategoryNode>` | Hijos directos (construido en memoria) |

### `CustomTreeImpl`
Implementación de `TreeAlgorithmStrategy`. Mantiene la lista plana de nodos en memoria
y construye la jerarquía de forma virtual cuando se necesita. No persiste nada — la
persistencia es responsabilidad del repositorio activo (`app.storage`).

---

## Operaciones implementadas (las 11 obligatorias)

| # | Operación | Método | Estrategia interna |
|---|---|---|---|
| 1 | Crear raíz | `createRoot(node)` | Asigna `parentId = null` e inserta en lista plana |
| 2 | Agregar hijo | `addChild(parentId, child)` | Valida ID único, asigna `parentId` e inserta |
| 3 | Árbol completo | `getFullTree()` | Reconstruye jerarquía enlazando hijos desde raíces |
| 4 | Subárbol | `getSubtree(nodeId)` | DFS recursivo desde el nodo objetivo |
| 5 | Ruta desde raíz | `getPathFromRoot(nodeId)` | Invierte la lista de ancestros |
| 6 | Recorrido DFS | `getDFS()` | Recursión directa (usa pila del sistema) |
| 7 | Recorrido BFS | `getBFS()` | Cola FIFO manual con `ArrayDeque` (JDK) |
| 8 | Altura del árbol | `getHeight()` | Recursión: máximo entre alturas de hijos + 1 |
| 9 | Profundidad de nodo | `getDepth(nodeId)` | Iteración hacia arriba por `parentId` |
| 10 | Ancestros | `getAncestors(nodeId)` | Iteración hacia arriba acumulando padres |
| 11 | Validar ciclos | `validateNoCycles()` | Rastreo hacia arriba por cada nodo; ciclo = ID ya visitado |

---

## Decisiones de diseño

**Lista plana como almacenamiento interno**
Los nodos se guardan en un `ArrayList<CategoryNode>` sin estructura jerárquica propia.
La jerarquía se *construye virtualmente* en cada llamada a `getFullTree()` enlazando
los `children` de cada nodo. Esto simplifica la persistencia: el repositorio solo
guarda y recupera la lista plana, y el motor reconstruye el árbol cuando se necesita.

**Sin estructuras especializadas externas**
Se evitan `TreeMap`, `TreeSet`, `LinkedList` y cualquier colección del JDK que implique
semántica de árbol. El único `ArrayDeque` usado en BFS es una cola genérica FIFO del JDK,
no una estructura de árbol.

**Validación de ciclos por rastreo hacia arriba**
En lugar de un algoritmo de coloreo (típico de grafos), se recorre hacia arriba desde
cada nodo siguiendo `parentId`. Si durante el ascenso se encuentra un ID ya visitado
en ese camino, hay ciclo. Funciona correctamente en árboles con raíz única.

**Recursión para DFS y altura**
DFS y el cálculo de altura usan la pila de llamadas del sistema (recursión) en lugar
de una pila explícita. Es la forma más natural de expresar el recorrido en profundidad
sin estructuras auxiliares.

---

## Compatibilidad con persistencias

Esta estrategia es agnóstica al repositorio activo. El flujo siempre es:

```
Repositorio.findAll() → CustomTreeImpl.setNodes(lista) → algoritmo()
```

Funciona igual con `app.storage=memory`, `app.storage=postgres` o `app.storage=mongo`.

---

## Tabla comparativa vs. Estrategia Collections

Las dos estrategias se probaron con el mismo árbol de demostración:

```
Tienda (raíz)
├── Electrónica
│   ├── Computadoras
│   │   └── Laptops
│   └── Celulares
└── Ropa
    └── Camisas
```

| Operación | Resultado Custom | Resultado Collections | ¿Equivalentes? |
|---|---|---|---|
| DFS | Tienda → Electrónica → Computadoras → Laptops → Celulares → Ropa → Camisas | Tienda → Electrónica → Computadoras → Laptops → Celulares → Ropa → Camisas | ✅ |
| BFS | Tienda → Electrónica → Ropa → Computadoras → Celulares → Camisas → Laptops | Tienda → Electrónica → Ropa → Computadoras → Celulares → Camisas → Laptops | ✅ |
| Altura | 3 | 3 | ✅ |
| Profundidad de "Laptops" | 3 | 3 | ✅ |
| Ancestros de "Laptops" | [Computadoras, Electrónica, Tienda] | [Computadoras, Electrónica, Tienda] | ✅ |
| Ruta a "Laptops" | [Tienda, Electrónica, Computadoras, Laptops] | [Tienda, Electrónica, Computadoras, Laptops] | ✅ |
| Validar ciclos (árbol válido) | `true` | `true` | ✅ |

> Los resultados del árbol de demostración son equivalentes en ambas estrategias.
> Ver colección de Postman en `/postman/` para pruebas end-to-end.