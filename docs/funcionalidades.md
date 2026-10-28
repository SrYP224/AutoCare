# Funcionalidades - AutoCare

Este documento describe qué hace la aplicación. Los campos de cada entidad (vehículo y mantenimiento) están definidos una sola vez en [Información almacenada](../README.md#información-almacenada), y el diseño de las pantallas en [ui.md](ui.md).

## 1. Gestión del vehículo

La aplicación permitirá registrar y consultar la información básica del vehículo del usuario (datos en [Información almacenada](../README.md#información-almacenada)).

### Funciones

- Registrar vehículo.
- Consultar información.
- Editar información.
- Actualizar kilometraje.

---

## 2. Panel de inicio

La pantalla de inicio mostrará un resumen general del vehículo y su mantenimiento.

### Información mostrada

- Marca y modelo del vehículo.
- Placa.
- Kilometraje actual.
- Último mantenimiento registrado.
- Próximo mantenimiento.
- Total gastado en mantenimientos.

### Objetivo

Permitir que el usuario pueda conocer rápidamente el estado general de su vehículo sin tener que revisar todo el historial.

---

## 3. Registro de mantenimiento

El usuario podrá registrar cada mantenimiento realizado al vehículo (datos en [Información almacenada](../README.md#información-almacenada)).

### Ejemplos de mantenimiento

- Cambio de aceite.
- Cambio de filtros.
- Revisión de frenos.
- Cambio de batería.
- Cambio de llantas.
- Alineación.
- Revisión general.
- Otro.

---

## 4. Historial de mantenimiento

La aplicación permitirá consultar los mantenimientos registrados anteriormente.

Cada registro mostrará los datos del mantenimiento.

El historial estará ordenado desde el mantenimiento más reciente hasta el más antiguo.

---

## 5. Edición de mantenimientos

El usuario podrá modificar la información de un mantenimiento registrado.

Se podrán modificar todos los datos del mantenimiento.

---

## 6. Eliminación de mantenimientos

El usuario podrá eliminar un registro de mantenimiento.

Antes de eliminarlo, la aplicación mostrará una ventana de confirmación para evitar eliminaciones accidentales.

---

## 7. Control de gastos

AutoCare calculará el total de dinero gastado en los mantenimientos registrados.

### Ejemplo

Mantenimiento 1: $180.000  
Mantenimiento 2: $250.000  
Mantenimiento 3: $120.000

**Total: $550.000**

El total será mostrado en la pantalla de inicio.

---

## 8. Seguimiento del próximo mantenimiento

Cada mantenimiento podrá tener asociado un kilometraje recomendado para el siguiente servicio.

### Ejemplo

Último cambio de aceite:

65.000 km

Próximo cambio recomendado:

70.000 km

Si el kilometraje actual es:

68.000 km

La aplicación mostrará:

**Faltan 2.000 km para el próximo mantenimiento.**

Si el kilometraje actual supera el kilometraje recomendado:

**Mantenimiento pendiente.**

---

## 9. Validación de información

La aplicación validará los datos ingresados por el usuario.

### Ejemplos

- La marca no puede estar vacía.
- El modelo no puede estar vacío.
- La placa debe ser ingresada.
- El kilometraje debe ser un número válido.
- El costo no puede ser negativo.
- La fecha debe ser válida.
- El tipo de mantenimiento debe ser seleccionado.
