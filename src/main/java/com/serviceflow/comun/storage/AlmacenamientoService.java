package com.serviceflow.comun.storage;

import org.springframework.web.multipart.MultipartFile;

public interface AlmacenamientoService {

    /**
     * Guarda un archivo en el almacenamiento y retorna la ruta relativa accesible.
     * @param archivo El archivo multipart enviado por el cliente.
     * @param subdirectorio Subcarpeta donde se organizará (ej. "evidencias").
     * @return La ruta relativa del archivo guardado (ej. "evidencias/abc-123.jpg").
     */
    String guardar(MultipartFile archivo, String subdirectorio);

    /**
     * Elimina un archivo del almacenamiento.
     * @param rutaRelativa La ruta relativa del archivo a eliminar.
     */
    void eliminar(String rutaRelativa);
}
