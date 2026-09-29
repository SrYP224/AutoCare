#  AutoCare

Aplicación móvil para el control y seguimiento del mantenimiento de vehículos.

##  Descripción

AutoCare es una aplicación móvil desarrollada para ayudar a los propietarios de vehículos a llevar un control sencillo y organizado de los mantenimientos realizados a su vehículo.

La aplicación permite registrar información básica del vehículo, almacenar el historial de mantenimientos, consultar los gastos asociados y realizar un seguimiento de los próximos servicios de mantenimiento.

El proyecto está desarrollado como parte de la asignatura **Computación Móvil 2026-2** de la Universidad Antonio Nariño.

---

## Objetivo

Desarrollar una aplicación móvil que facilite la gestión del mantenimiento de un vehículo mediante el registro y consulta de información relacionada con servicios, reparaciones, kilometraje y costos.

---

## Problema

Los propietarios de vehículos pueden olvidar las fechas o el kilometraje en el que realizaron determinados mantenimientos, así como los gastos asociados a reparaciones y servicios.

AutoCare busca solucionar este problema proporcionando una herramienta sencilla que permita centralizar esta información en un solo lugar.

---

Para conocer el detalle de cada funcionalidad, consultar:

[`docs/funcionalidades.md`](docs/funcionalidades.md)

---

## Tecnologías

- Kotlin
- Jetpack Compose
- Android Studio
- Room
- SQLite
- Material 3
- Android SDK
- Firebase Authentication
- Cloud Firestore

---

## Arquitectura

El proyecto utilizará una arquitectura basada en **MVVM (Model - View - ViewModel)**.

### Componentes principales

- **UI:** Jetpack Compose.
- **ViewModel:** manejo del estado y lógica de presentación.
- **Repository:** intermediario entre la aplicación y la base de datos.
- **Room:** persistencia de información.
- **SQLite:** almacenamiento local.

### Flujo general

UI → ViewModel → Repository → Room → SQLite

---

## Información almacenada

### Vehículo

- ID
- Marca
- Modelo
- Año
- Placa
- Kilometraje

### Mantenimiento

- ID
- Tipo de mantenimiento
- Fecha
- Kilometraje
- Costo
- Descripción
- Próximo kilometraje recomendado

---

Autor(es): 

- Yeison David Parra Mosquera
- Samuel Murillo Vargas 
- Michael David Portilla Montilla


## Referencias

- [Ideas iniciales de proyecto](docs/ideas.md)
- [Funcionalidades de la aplicación](docs/funcionalidades.md)
- [Diseño de la interfaz de usuario](docs/ui.md)

p1.0.1
