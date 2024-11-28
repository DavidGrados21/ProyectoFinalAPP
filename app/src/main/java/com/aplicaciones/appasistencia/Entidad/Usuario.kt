package com.aplicaciones.appasistencia.Entidad

data class Usuario (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val telefono: String?,
    val foto: String?,
    val rol: String
)
