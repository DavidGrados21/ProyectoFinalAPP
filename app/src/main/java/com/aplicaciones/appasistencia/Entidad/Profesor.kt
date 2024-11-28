package com.aplicaciones.appasistencia.Entidad

data class Profesor (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contraseña: String,
    val telefono: String?,
    val foto: String?,
    val fechaRegistro: String
)