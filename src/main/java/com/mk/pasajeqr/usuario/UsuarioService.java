package com.mk.pasajeqr.usuario;

import java.util.List;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);
    Usuario obtenerPorId(Long id);
    List<Usuario> listarUsuarios();
    void eliminarUsuario(Long id);
    Usuario buscarPorEmail(String email);
}
