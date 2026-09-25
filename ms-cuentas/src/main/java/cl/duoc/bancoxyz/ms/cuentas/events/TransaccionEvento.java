package cl.duoc.bancoxyz.ms.cuentas.events;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Evento de dominio publicado a Kafka tras una transaccion (ej. retiro).
 * Patron: publicacion/suscripcion (arquitectura orientada a eventos).
 */
public record TransaccionEvento(
        String eventoId,
        Long cuentaId,
        String tipo,
        BigDecimal monto,
        BigDecimal saldoResultante,
        Instant ocurridoEn,
        String origen
) {
}
