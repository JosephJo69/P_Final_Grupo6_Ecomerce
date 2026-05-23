Tecnologías utilizadas
Java 17
Spring Boot 3
Maven
Docker
Docker Compose
PostgreSQL
MongoDB
Arquitectura del proyecto

El proyecto está dividido en módulos Maven:

PF_Grupo6_Ecomerce
│
├── tree-app
│   └── Aplicación Spring Boot
│
├── tree-engine
│   └── Motor de lógica del árbol
│
├── Dockerfile
├── docker-compose.yml
└── pom.xml
Requisitos previos

Antes de ejecutar el proyecto debes tener instalado:

Docker Desktop
Docker Compose
Git
Java 17
Maven
Variables de entorno

El sistema utiliza variables de entorno mediante Docker Compose.

Variables utilizadas:

SPRING_PROFILES_ACTIVE=docker
POSTGRES_DB=ecommerce
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123
Servicios del Docker Compose

El entorno automatiza los siguientes servicios:

Servicio	Puerto
Spring Boot	8081
PostgreSQL	5432
MongoDB	27017
Docker Compose
Levantar todo el entorno
docker compose up --build
Detener el entorno
docker compose down
Modo memoria

El sistema permite ejecutar únicamente la aplicación Spring Boot.

Ejemplo:

docker compose up app
Validación del sistema
Verificar contenedores activos
docker ps
Ver logs del sistema
docker logs -f springboot_app
Verificar endpoint principal
Crear raíz del árbol
curl -X POST "http://localhost:8081/tree/root?value=Raiz"

Respuesta esperada:

{
  "id":"819c9470-7daa-4251-9d54-cd95bff438f2",
  "value":"Raiz",
  "children":[]
}
Obtener árbol actual
curl http://localhost:8081/tree
Persistencia

El proyecto utiliza volúmenes Docker para mantener persistencia de datos.

Volúmenes utilizados:

postgres_data
mongo_data
Dockerfile

La aplicación se construye automáticamente desde el Dockerfile incluido en el repositorio.

Troubleshooting
Error de puerto ocupado
Bind for 0.0.0.0 failed: port is already allocated

Solución:

docker compose down
Error YAML
found a tab character that violates indentation

Solución:

Reemplazar tabulaciones por espacios en docker-compose.yml
Error Maven cyclic reference
The projects in the reactor contain a cyclic reference

Solución:

Eliminar dependencias circulares entre módulos Maven.
Estado final del proyecto

 Docker Compose funcionando
Spring Boot funcionando
MongoDB funcionando
PostgreSQL funcionando
Endpoints REST funcionando
Proyecto multi-módulo funcionando
Variables de entorno configuradas
Automatización completa del entorno