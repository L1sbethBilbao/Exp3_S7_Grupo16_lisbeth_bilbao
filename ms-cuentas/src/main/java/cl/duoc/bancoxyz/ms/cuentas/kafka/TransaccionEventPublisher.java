package cl.duoc.bancoxyz.ms.cuentas.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import cl.duoc.bancoxyz.ms.cuentas.events.TransaccionEvento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Productor Kafka: publica eventos de transacciones al topico bancoxyz.transacciones.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionEventPublisher {

    private final KafkaTemplate<String, TransaccionEvento> kafkaTemplate;

    public void publicar(TransaccionEvento evento) {
        String key = String.valueOf(evento.cuentaId());
        kafkaTemplate.send(KafkaTopics.TRANSACCIONES, key, evento)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Fallo al publicar evento Kafka {} cuenta={}",
                                evento.eventoId(), evento.cuentaId(), ex);
                    } else {
                        log.info("Evento Kafka publicado topic={} partition={} offset={} key={} tipo={} monto={}",
                                KafkaTopics.TRANSACCIONES,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                key,
                                evento.tipo(),
                                evento.monto());
                    }
                });
    }
}
