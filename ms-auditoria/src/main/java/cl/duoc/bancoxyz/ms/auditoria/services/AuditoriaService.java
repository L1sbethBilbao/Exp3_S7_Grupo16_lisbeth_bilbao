package cl.duoc.bancoxyz.ms.auditoria.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import cl.duoc.bancoxyz.ms.auditoria.dtos.AuditoriaRegistroDTO;
import cl.duoc.bancoxyz.ms.auditoria.events.TransaccionEvento;
import lombok.extern.slf4j.Slf4j;

/**
 * Guarda en memoria los eventos auditados (suficiente para evidencia de S7).
 */
@Service
@Slf4j
public class AuditoriaService {

    private final List<AuditoriaRegistroDTO> registros = new CopyOnWriteArrayList<>();

    public void registrar(TransaccionEvento evento, int partition, long offset) {
        AuditoriaRegistroDTO registro = new AuditoriaRegistroDTO(
                evento.eventoId(),
                evento.cuentaId(),
                evento.tipo(),
                evento.monto(),
                evento.saldoResultante(),
                evento.ocurridoEn(),
                evento.origen(),
                partition,
                offset);
        registros.add(registro);
        log.info("AUDITORIA OK eventoId={} cuenta={} tipo={} monto={} saldoResultante={}",
                evento.eventoId(), evento.cuentaId(), evento.tipo(), evento.monto(), evento.saldoResultante());
    }

    public List<AuditoriaRegistroDTO> listar() {
        List<AuditoriaRegistroDTO> copia = new ArrayList<>(registros);
        Collections.reverse(copia);
        return copia;
    }
}
