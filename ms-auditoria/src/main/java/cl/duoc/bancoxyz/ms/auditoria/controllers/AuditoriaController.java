package cl.duoc.bancoxyz.ms.auditoria.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.bancoxyz.ms.auditoria.dtos.AuditoriaRegistroDTO;
import cl.duoc.bancoxyz.ms.auditoria.services.AuditoriaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping("/eventos")
    public List<AuditoriaRegistroDTO> listarEventos() {
        return auditoriaService.listar();
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
                "servicio", "ms-auditoria",
                "rol", "consumidor-kafka",
                "topic", "bancoxyz.transacciones",
                "semana", "7");
    }
}
