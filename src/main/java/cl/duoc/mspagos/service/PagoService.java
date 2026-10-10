package cl.duoc.mspagos.service;

import cl.duoc.mspagos.exception.RecursoNoEncontradoException;
import cl.duoc.mspagos.model.Pago;
import cl.duoc.mspagos.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public Pago crear(Pago pago) {
        pago.setId(null);
        pago.setFecha(LocalDateTime.now());
        if (pago.getEstado() == null || pago.getEstado().isBlank()) {
            pago.setEstado("PENDIENTE");
        }
        return pagoRepository.save(pago);
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    public List<Pago> listarPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }

    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con id " + id));
    }

    public Pago actualizar(Long id, Pago datos) {
        Pago existente = buscarPorId(id);
        existente.setPedidoId(datos.getPedidoId());
        existente.setMonto(datos.getMonto());
        existente.setMetodo(datos.getMetodo());
        if (datos.getEstado() != null && !datos.getEstado().isBlank()) {
            existente.setEstado(datos.getEstado());
        }
        return pagoRepository.save(existente);
    }

    public void eliminar(Long id) {
        Pago existente = buscarPorId(id);
        pagoRepository.delete(existente);
    }
}