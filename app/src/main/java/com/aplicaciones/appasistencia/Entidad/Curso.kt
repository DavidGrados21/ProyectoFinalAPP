package com.aplicaciones.appasistencia.Entidad

data class Curso(
    val id: Int,
    val nombreCurso: String,
    val descripcion: String?,
    val codigoCurso: String,
    val profesorId: Int,
    val horaInicio: String,
    val horaFin: String,
    val diaSemana: String,
    var nombreProfesor: String? = null

)
