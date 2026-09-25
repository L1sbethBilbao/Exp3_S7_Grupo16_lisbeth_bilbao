package cl.duoc.bancoxyz.ms.auditoria.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record AuditoriaRegistroDTO(
        String eventoId,
        Long cuentaId,
        String tipo,
        BigDecimal monto,
        BigDecimal saldoResultante,
        Instant ocurridoEn,
        String origen,
        int partition,
        long offset
) {
}
