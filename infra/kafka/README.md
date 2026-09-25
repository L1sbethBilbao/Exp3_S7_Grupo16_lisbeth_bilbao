# Infra Kafka (EC2 + Docker Compose)

Cluster Apache Kafka usado en Semana 7 para la arquitectura Pub/Sub de Banco XYZ.

## Componentes

- 3 ZooKeeper (`zookeeper-1` … `zookeeper-3`)
- 3 Brokers Kafka (`kafka-1` … `kafka-3`) — puertos externos `29092`, `39092`, `49092`
- Kafka UI (`kafka-ui`) — puerto `8090`

## Despliegue en EC2

1. Copiar este `docker-compose.yml` a la instancia (ej. `~/kafka/`).
2. Si la Elastic IP cambia, actualizar `KAFKA_ADVERTISED_LISTENERS` (hoy: `52.204.221.241`).
3. Abrir en el Security Group: `29092`, `39092`, `49092`, `8090` (y SSH `22`).
4. Levantar:

```bash
cd ~/kafka
docker compose up -d
docker ps
```

5. Crear el topico (una vez):

```bash
docker exec kafka-1 kafka-topics --bootstrap-server kafka-1:9092 \
  --create --if-not-exists \
  --topic bancoxyz.transacciones \
  --partitions 3 \
  --replication-factor 2
```

6. Kafka UI: `http://<EIP>:8090`

## Relacion con los microservicios

Los servicios Spring usan bootstrap:

`52.204.221.241:29092,52.204.221.241:39092,52.204.221.241:49092`

(config en `config-server` → `ms-cuentas.yml` / `ms-auditoria.yml`).
