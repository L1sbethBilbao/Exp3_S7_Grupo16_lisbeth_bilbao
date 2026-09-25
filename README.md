# Banco XYZ - Semana 7 (Kafka + Resilience4j)

Entrega: `Exp3_S7_Grupo16_lisbeth_bilbao`

Continuidad de la Semana 6 (Config Server, Eureka, Resilience4j, Security) + **arquitectura orientada a eventos con Apache Kafka** en EC2.

## Arquitectura de eventos elegida

**Patron:** Publicacion / Suscripcion (Event-Driven / Pub-Sub)

- **Productor:** `ms-cuentas` publica un evento cuando se realiza un **retiro**.
- **Topico:** `bancoxyz.transacciones` (3 particiones, replication factor 2).
- **Consumidor:** `ms-auditoria` (consumer group `ms-auditoria`) lee el evento y lo registra para auditoria.
- **Resilience4j:** se mantiene en los 3 BFF (Circuit Breaker + Retry + Fallback).

```
Cliente / Postman
      |
      v
bff-cajero :8093  --HTTP-->  ms-cuentas :8090  --evento-->  Kafka (EC2)
   (Resilience4j)              (productor)                      |
                                                                v
                                                         ms-auditoria :8094
                                                            (consumidor)
```

Cluster Kafka (EC2 + Elastic IP `52.204.221.241`):
- Brokers: `29092`, `39092`, `49092`
- Kafka UI: http://52.204.221.241:8090
- Compose del cluster: `infra/kafka/docker-compose.yml` (ver `infra/kafka/README.md`)

## Proyectos

| Carpeta | Puerto | Rol |
| --- | --- | --- |
| `config-server` | 8888 | Configuracion central |
| `discovery-server` | 8761 | Eureka |
| `auth-server` | 9000 | OAuth2 Authorization Server |
| `ms-cuentas` | 8090 | Core + **productor Kafka** |
| `ms-auditoria` | 8094 | **Consumidor Kafka** (auditoria) |
| `bff-web` | 8091 | BFF Web + Resilience4j |
| `bff-movil` | 8092 | BFF Movil + Resilience4j |
| `bff-cajero` | 8093 | BFF Cajero + Resilience4j |
| `infra/kafka` | — | Docker Compose cluster Kafka en EC2 |

## Orden de arranque

1. Kafka en EC2 ya levantado (`docker compose up -d` con `infra/kafka/docker-compose.yml`, tipicamente en `~/kafka`).
2. En tu PC, terminales en este orden:

```powershell
cd config-server
mvn spring-boot:run
```

```powershell
cd discovery-server
mvn spring-boot:run
```

```powershell
cd auth-server
mvn spring-boot:run
```

```powershell
cd ms-cuentas
mvn spring-boot:run
```

```powershell
cd ms-auditoria
mvn spring-boot:run
```

```powershell
cd bff-cajero
mvn spring-boot:run
```

(Opcional: `bff-web` y `bff-movil` para evidencias Resilience4j / canales.)

Requisitos: JDK 17+, Maven, wallet Oracle (ruta en `ms-cuentas/application.properties`), cluster Kafka accesible.

## Como probar el flujo Kafka (evidencia S7)

1. Retiro via BFF cajero (Basic Auth `cajero` / `cajero123`):

```http
POST http://localhost:8093/bff/cajero/cuentas/101/retiro
Content-Type: application/json

{"monto": 100}
```

2. Ver mensaje en Kafka UI: http://52.204.221.241:8090 → Topics → `bancoxyz.transacciones` → Messages.

3. Ver que el consumidor lo proceso:

```http
GET http://localhost:8094/auditoria/eventos
```

Tambien revisa el log de `ms-auditoria` (`AUDITORIA OK ...`) y el de `ms-cuentas` (`Evento Kafka publicado ...`).

## Credenciales (igual Semana 6)

| Servicio | Usuario | Password |
| --- | --- | --- |
| bff-cajero | `cajero` | `cajero123` |
| bff-web | `web` | `web123` |
| bff-movil | `movil` | `movil123` |

## Criterios de la pauta (Semana 7)

1. Arquitectura de eventos definida (Pub/Sub) adecuada a transacciones bancarias.
2. Diagrama de topicos/mensajes/eventos (incluir en evidencias / diagrama del grupo).
3. Resilience4j en BFF (continuidad S6).
4. Kafka funcional: productor (`ms-cuentas`) + consumidor (`ms-auditoria`) con mensajes procesados.

## Version

- Java 17, Spring Boot 3.3.5, Spring Cloud 2023.0.3, spring-kafka
- Resilience4j 2.1.0
- Grupo 16 / Lisbeth Bilbao
