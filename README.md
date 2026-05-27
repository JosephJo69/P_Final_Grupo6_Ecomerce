# PF Grupo 6 E-commerce

Backend Java 17 con Spring Boot 3 para gestionar arboles de categorias de e-commerce.

## Tecnologias

- Java 17
- Spring Boot 3
- Maven
- Docker y Docker Compose
- PostgreSQL
- MongoDB

## Arquitectura

El proyecto esta dividido en dos modulos Maven:

- `tree-engine`: motor de algoritmos del arbol.
- `tree-app`: API REST Spring Boot e integracion con persistencias.

## Selectores

La aplicacion permite cambiar estrategia y persistencia sin modificar codigo:

- `app.tree-strategy=custom`
- `app.tree-strategy=collections`
- `app.storage=memory`
- `app.storage=postgres`
- `app.storage=mongo`

## Ejecutar con Docker

Los companeros no necesitan instalar MongoDB ni PostgreSQL localmente. Las bases se levantan como contenedores.

Modo memoria, solo la app:

```bash
docker compose --profile memory up --build
```

Modo MongoDB, app + Mongo en Docker:

```bash
docker compose --profile mongo up --build
```

Modo PostgreSQL, app + PostgreSQL en Docker:

```bash
docker compose --profile postgres up --build
```

Demo completa, app con MongoDB y ambas bases levantadas:

```bash
docker compose --profile full up --build
```

La API queda disponible en:

```text
http://localhost:8081
```

## Endpoints principales

Crear raiz:

```bash
curl -X POST http://localhost:8081/nodes/root \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Electronica\",\"description\":\"Categoria raiz\"}"
```

Agregar hijo:

```bash
curl -X POST http://localhost:8081/nodes/{parentId}/children \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Computadoras\",\"description\":\"Laptops y desktops\"}"
```

Consultar arbol completo:

```bash
curl http://localhost:8081/tree
```

Recorridos y metricas:

```bash
curl http://localhost:8081/tree/bfs
curl http://localhost:8081/tree/dfs
curl http://localhost:8081/tree/height
curl http://localhost:8081/tree/{nodeId}/depth
curl http://localhost:8081/tree/{nodeId}/ancestors
curl http://localhost:8081/tree/{nodeId}/path
curl http://localhost:8081/tree/{nodeId}/subtree
curl http://localhost:8081/tree/validate
```

## Entregables cubiertos

- Motor con estrategias `custom` y `collections`.
- Persistencias seleccionables: memoria, PostgreSQL y MongoDB.
- MongoDB disponible por Docker Compose, sin instalacion local.
- Wiring Spring con beans condicionales.
- API REST para las operaciones principales del arbol.
