package cl.duoc.bancoxyz.ms.auditoria.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import cl.duoc.bancoxyz.ms.auditoria.events.TransaccionEvento;
import cl.duoc.bancoxyz.ms.auditoria.services.AuditoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Consumidor Kafka (consumer group: ms-auditoria).
 * Lee eventos de transacciones publicados por ms-cuentas.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TransaccionEventListener {

    private final AuditoriaService auditoriaService;

    @KafkaListener(topics = KafkaTopics.TRANSACCIONES, groupId = "ms-auditoria")
    public void onTransaccion(
            @Payload TransaccionEvento evento,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {

        log.info("Evento recibido topic={} partition={} offset={} key={} eventoId={} tipo={} cuenta={} monto={}",
                KafkaTopics.TRANSACCIONES, partition, offset, key,
                evento.eventoId(), evento.tipo(), evento.cuentaId(), evento.monto());

        auditoriaService.registrar(evento, partition, offset);
    }
}
