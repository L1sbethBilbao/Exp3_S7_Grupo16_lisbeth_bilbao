package cl.duoc.bancoxyz.ms.auditoria.events;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Mismo contrato de evento que publica ms-cuentas en bancoxyz.transacciones.
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
