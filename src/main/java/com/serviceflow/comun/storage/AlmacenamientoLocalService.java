package com.serviceflow.comun.storage;

import com.serviceflow.comun.exception.BusinessRuleException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
@Slf4j
public class AlmacenamientoLocalService implements AlmacenamientoService {

    @Value("${storage.location:uploads}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    public void inicializar() {
        try {
            this.rootLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
            Files.createDirectories(this.rootLocation);
            log.info("Directorio de almacenamiento local inicializado en: {}", this.rootLocation);
        } catch (IOException e) {
            throw new BusinessRuleException("No se pudo inicializar el directorio de almacenamiento: " + e.getMessage());
        }
    }

    @Override
    public String guardar(MultipartFile archivo, String subdirectorio) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BusinessRuleException("No se puede almacenar un archivo vacío.");
        }

        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null) {
            nombreOriginal = "archivo";
        }

        // Sanitizar el nombre original quitando caracteres peligrosos
        String nombreLimpio = nombreOriginal.replaceAll("[^a-zA-Z0-9._-]", "_");
        String nombreUnico = UUID.randomUUID() + "_" + nombreLimpio;

        try {
            Path carpetaDestino = this.rootLocation.resolve(subdirectorio).normalize();
            
            // Verificación de seguridad contra Path Traversal
            if (!carpetaDestino.startsWith(this.rootLocation)) {
                throw new BusinessRuleException("Intento de almacenamiento fuera del directorio permitido.");
            }

            Files.createDirectories(carpetaDestino);

            Path archivoDestino = carpetaDestino.resolve(nombreUnico).normalize();
            if (!archivoDestino.startsWith(carpetaDestino)) {
                throw new BusinessRuleException("Nombre de archivo inválido para almacenamiento.");
            }

            try (InputStream inputStream = archivo.getInputStream()) {
                Files.copy(inputStream, archivoDestino, StandardCopyOption.REPLACE_EXISTING);
            }

            log.info("Archivo almacenado correctamente: {}", archivoDestino);
            return subdirectorio + "/" + nombreUnico;
        } catch (IOException e) {
            throw new BusinessRuleException("Fallo al almacenar el archivo: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String rutaRelativa) {
        if (rutaRelativa == null || rutaRelativa.isBlank()) {
            return;
        }

        try {
            Path archivo = this.rootLocation.resolve(rutaRelativa).normalize();
            if (archivo.startsWith(this.rootLocation)) {
                Files.deleteIfExists(archivo);
                log.info("Archivo eliminado: {}", archivo);
            }
        } catch (IOException e) {
            log.warn("No se pudo eliminar el archivo {}: {}", rutaRelativa, e.getMessage());
        }
    }
}
