package com.aplicaciones.appasistencia.Entidad

data class AlumnoPorCurso(
    val alumnoPorCursoID: Int,  // ID único de la relación Alumno-Curso
    val alumnoID: Int,          // ID del alumno (relacionado con Alumno)
    val cursoID: Int,           // ID del curso (relacionado con Curso)
    val fechaInscripcion: String // Fecha de inscripción al curso
)