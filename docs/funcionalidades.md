# Funcionalidades - AutoCare

## 1. Gestión del vehículo

La aplicación permitirá registrar y consultar la información básica del vehículo del usuario.

### Datos

- Marca
- Modelo
- Año
- Placa
- Kilometraje actual

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

El usuario podrá registrar cada mantenimiento realizado al vehículo.

### Información

- Tipo de mantenimiento.
- Fecha.
- Kilometraje.
- Costo.
- Descripción.
- Próximo kilometraje recomendado.

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

Cada registro mostrará:

- Tipo de mantenimiento.
- Fecha.
- Kilometraje.
- Costo.
- Descripción.

El historial estará ordenado desde el mantenimiento más reciente hasta el más antiguo.

---

## 5. Edición de mantenimientos

El usuario podrá modificar la información de un mantenimiento registrado.

Se podrán modificar:

- Tipo.
- Fecha.
- Kilometraje.
- Costo.
- Descripción.
- Próximo kilometraje.

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

---

# Pantallas principales

AutoCare contará con tres pantallas principales.

## Pantalla 1 - Inicio

### Función

Mostrar un resumen general del vehículo.

### Contenido

- Información resumida del vehículo.
- Kilometraje actual.
- Próximo mantenimiento.
- Último mantenimiento.
- Total gastado.

---

## Pantalla 2 - Mi vehículo

### Función

Permitir administrar la información básica del vehículo.

### Contenido

- Marca.
- Modelo.
- Año.
- Placa.
- Kilometraje.

### Acciones

- Guardar.
- Editar.
- Actualizar kilometraje.

---

## Pantalla 3 - Mantenimientos

### Función

Administrar el historial de mantenimientos.

### Contenido

- Lista de mantenimientos.
- Fecha.
- Tipo.
- Kilometraje.
- Costo.

### Acciones

- Agregar mantenimiento.
- Editar mantenimiento.
- Eliminar mantenimiento.
- Consultar detalle.

Las acciones de agregar y editar utilizarán diálogos para evitar crear pantallas adicionales.

---

# Alcance inicial

La primera versión de AutoCare se limitará a las funcionalidades necesarias para demostrar el funcionamiento principal de la aplicación.

No se implementarán inicialmente:

- Login.
- Registro de usuarios.
- GPS.
- Mapas.
- Pagos.
- Conexión con talleres.
- Integración con sensores del vehículo.
- Servicios en la nube.
- Notificaciones push.
- Gestión de flotas.

Estas funcionalidades podrían considerarse como posibles ampliaciones futuras.
