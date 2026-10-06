package com.serviceflow.clientes.controller;

import com.serviceflow.clientes.dto.ActualizarClienteRequest;
import com.serviceflow.clientes.dto.ClienteResponse;
import com.serviceflow.clientes.dto.CrearClienteRequest;
import com.serviceflow.clientes.entity.Cliente;
import com.serviceflow.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> obtenerTodos() {
        List<ClienteResponse> clientes = clienteService.obtenerTodos().stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(mapToResponse(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crearCliente(@Valid @RequestBody CrearClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setEmail(request.email());
        cliente.setDireccion(request.direccion());
        cliente.setNotas(request.notas());
        cliente.setActivo(true);

        Cliente creado = clienteService.crearCliente(cliente);
        return new ResponseEntity<>(mapToResponse(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarClienteRequest request) {
        
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setNombre(request.nombre());
        clienteActualizado.setTelefono(request.telefono());
        clienteActualizado.setEmail(request.email());
        clienteActualizado.setDireccion(request.direccion());
        clienteActualizado.setNotas(request.notas());

        Cliente guardado = clienteService.actualizarCliente(id, clienteActualizado);
        return ResponseEntity.ok(mapToResponse(guardado));
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getDireccion(),
                cliente.getNotas(),
                cliente.getActivo(),
                cliente.getCreadoEn(),
                cliente.getActualizadoEn()
        );
    }
}
