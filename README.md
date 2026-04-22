# Programacion de Dispositivos Moviles para Android

Repositorio academico con practicas y proyectos desarrollados durante la materia de desarrollo Android. Aqui se integran ejercicios enfocados en interfaces con Jetpack Compose, manejo de estados, navegacion entre pantallas y persistencia local de datos.

## Proyectos incluidos

### 1. 506E15 Lista del Mandado

Aplicacion Android desarrollada con Jetpack Compose para gestionar una lista de compras por categorias.

**Funciones principales**
- Visualizacion de productos por categoria.
- Seleccion de articulos mediante casillas.
- Agregado de nuevos productos desde la interfaz.
- Eliminacion de productos de la lista.
- Envio de productos seleccionados a una segunda pantalla.

**Objetivo practico**
- Reforzar el uso de Compose.
- Trabajar con listas dinamicas y estado en memoria.
- Practicar la comunicacion entre pantallas con `Intent`.

Ruta del proyecto: [506E15Listadelmandado](C:/PROG-DISP-MOV-PARA-ANDROID/506E15Listadelmandado)

### 2. E4.1 CardexRoom

Aplicacion Android para administrar un cardex academico, registrar materias cursadas y visualizar un resumen del avance escolar.

**Funciones principales**
- Registro de materias cursadas.
- Edicion y eliminacion de registros.
- Consulta de materias faltantes.
- Filtro de informacion por semestre o por materias pendientes.
- Resumen academico con creditos y promedio.
- Persistencia local de datos con Room.

**Tecnologias utilizadas**
- Kotlin
- Jetpack Compose
- Material 3
- ViewModel
- StateFlow
- Room
- Coroutines

Ruta del proyecto: [E4.1CardexRoom](C:/PROG-DISP-MOV-PARA-ANDROID/E4.1CardexRoom)

## Actualizacion reciente: A4.3 Cambios a CardexRoom

Como parte de la actividad **A4.3 Cambios a CardexRoom**, se mejoro la relacion entre los filtros aplicados y la informacion mostrada en el resumen academico.

**Cambio implementado**
- El total de creditos mostrado en pantalla ahora cambia dinamicamente segun el filtro activo.

**Comportamiento actual**
- En `Todos`, se muestran los creditos aprobados generales.
- En `Por semestre`, se suman solo los creditos de las materias visibles en el semestre filtrado.
- En `Faltantes`, se muestran unicamente los creditos pendientes por cursar.
- La etiqueta del resumen tambien cambia para mantener coherencia con el filtro seleccionado.

**Resultado**
- La lista filtrada y el resumen academico ahora muestran informacion consistente en tiempo real.
- La implementacion se realizo con `StateFlow` y `combine(...)` dentro del `ViewModel`, manteniendo una UI reactiva en Compose.

## Estructura del repositorio

```text
PROG-DISP-MOV-PARA-ANDROID/
|- 506E15Listadelmandado/
|- E4.1CardexRoom/
`- README.md
```

## Como ejecutar los proyectos

1. Abrir el repositorio en Android Studio.
2. Elegir el proyecto que se desea revisar.
3. Sincronizar Gradle.
4. Ejecutar la app en un emulador o dispositivo Android compatible.

## Proposito academico

Este repositorio funciona como evidencia de aprendizaje de la materia, mostrando avances en:
- construccion de interfaces modernas en Android;
- manejo de estados y logica de presentacion;
- persistencia local con bases de datos;
- organizacion de proyectos moviles por actividades.

## Autor

- **Raul Chavira Narvaez**
- Grupo: `S8A`
- Materia: `Programacion de Dispositivos Moviles para Android`

