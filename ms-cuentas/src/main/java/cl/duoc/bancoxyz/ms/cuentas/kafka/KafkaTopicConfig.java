package cl.duoc.bancoxyz.ms.cuentas.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Crea el topico del proyecto en el cluster (3 particiones, RF 2),
 * alineado a la arquitectura minima de Kafka de la clase.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicoTransacciones() {
        return TopicBuilder.name(KafkaTopics.TRANSACCIONES)
                .partitions(3)
                .replicas(2)
                .config("min.insync.replicas", "2")
                .build();
    }
}
