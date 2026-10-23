# ✈️ AeroVía Perú
### Aplicación móvil para la gestión de vuelos y reservas

**AeroVía Perú** es una aplicación móvil Android desarrollada con Kotlin y XML, orientada a facilitar la consulta de vuelos y la gestión de reservas de pasajeros mediante una interfaz intuitiva y organizada.

El proyecto incorpora una pantalla de bienvenida con identidad visual propia, navegación hacia la pantalla principal y funcionalidades para registrar, consultar y administrar reservas de viaje.

---

## 📌 Tabla de contenidos

- [Descripción del proyecto](#-descripción-del-proyecto)
- [Objetivos](#-objetivos)
- [Funcionalidades](#-funcionalidades)
- [Tecnologías utilizadas](#-tecnologías-utilizadas)
- [Arquitectura y organización](#-arquitectura-y-organización)
- [Requisitos previos](#-requisitos-previos)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Flujo de navegación](#-flujo-de-navegación)
- [Participantes](#-participantes)
- [Estado del proyecto](#-estado-del-proyecto)

---

## 📖 Descripción del proyecto

AeroVía Perú es una propuesta de aplicación móvil que busca organizar las principales operaciones relacionadas con los vuelos y las reservas de pasajeros.

La aplicación presenta una pantalla de bienvenida que introduce al usuario a las funcionalidades disponibles. Desde allí, puede ingresar a la pantalla principal y navegar por las opciones de consulta de vuelos y administración de reservas.

El desarrollo utiliza componentes nativos de Android para construir las interfaces y una base de datos SQLite para gestionar la información local de las reservas.

## 🎯 Objetivos

### Objetivo general

Desarrollar una aplicación móvil Android que permita organizar la consulta de vuelos y la administración de reservas de pasajeros mediante una interfaz clara, funcional y de fácil navegación.

### Objetivos específicos

- Diseñar una interfaz de bienvenida con identidad visual para AeroVía Perú.
- Implementar la navegación entre las pantallas de la aplicación.
- Facilitar el registro y la consulta de reservas.
- Permitir la visualización y edición de los datos de una reserva.
- Organizar la información mediante una base de datos SQLite.
- Aplicar buenas prácticas de organización del código y desarrollo de aplicaciones Android.

## 🚀 Funcionalidades

| Funcionalidad | Descripción |
|---|---|
| Pantalla de bienvenida | Presenta la marca AeroVía Perú y el botón para comenzar. |
| Pantalla principal | Centraliza el acceso a las opciones de la aplicación. |
| Consulta de vuelos | Permite acceder al módulo destinado a la consulta de vuelos. |
| Registro de reservas | Permite ingresar la información necesaria para registrar una reserva. |
| Mis reservas | Facilita el acceso a las reservas registradas. |
| Detalle de reserva | Presenta la información correspondiente a una reserva seleccionada. |
| Edición de reservas | Permite modificar los datos de una reserva. |
| Persistencia local | Utiliza SQLite para almacenar la información de las reservas. |

*Nota: las funcionalidades concretas disponibles pueden variar según la implementación actual del código fuente.*

## 🛠️ Tecnologías utilizadas

| Tecnología | Aplicación en el proyecto |
|---|---|
| Kotlin | Lenguaje principal para la lógica de la aplicación. |
| XML | Definición de las interfaces visuales. |
| Android Studio | Entorno de desarrollo integrado (IDE). |
| SQLite | Gestión de información local de las reservas. |
| Gradle | Configuración y compilación del proyecto Android. |
| Git | Control de versiones del código fuente. |
| GitHub | Alojamiento y seguimiento del repositorio. |

## 🏗️ Arquitectura y organización

El proyecto se organiza en módulos de interfaz, lógica de navegación y acceso a datos.

### Componentes principales

- **Activities:** administran las diferentes pantallas y sus interacciones.
- **Layouts XML:** definen la estructura visual de cada pantalla.
- **Modelos de datos:** representan la información de las reservas.
- **SQLite:** proporciona almacenamiento local.
- **Gradle:** gestiona la configuración y las dependencias del proyecto.

### Estructura general

```text
AeroViaPeru/
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/aeroviaperu/
│           │       ├── SplashActivity.kt
│           │       ├── MainActivity.kt
│           │       ├── VuelosActivity.kt
│           │       ├── CrearReservaActivity.kt
│           │       ├── MisReservasActivity.kt
│           │       ├── DetalleReservaActivity.kt
│           │       ├── EditarReservaActivity.kt
│           │       ├── Reserva.kt
│           │       └── ReservaDbHelper.kt
│           ├── res/
│           │   ├── drawable/
│           │   ├── layout/
│           │   └── values/
│           └── AndroidManifest.xml
├── gradle/
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── .gitignore
```

*La estructura es referencial. Los nombres y las ubicaciones deben coincidir con los archivos existentes en el repositorio.*

## 💻 Requisitos previos

Antes de ejecutar el proyecto, se recomienda contar con:

- Android Studio instalado.
- JDK compatible con la versión de Gradle del proyecto.
- Android SDK requerido por la configuración del proyecto.
- Emulador Android o dispositivo físico compatible.
- Git, si se desea clonar el repositorio desde la terminal.

## ⚙️ Instalación y ejecución

### 1. Clonar el repositorio

Abre una terminal y ejecuta:

```bash
git clone https://github.com/santiman0408-sudo/AeroViaPeru.git
```

### 2. Abrir el proyecto

1. Inicia Android Studio.
2. Selecciona **Open**.
3. Busca la carpeta `AeroViaPeru` que se descargó.
4. Espera a que Gradle termine de sincronizar el proyecto.

### 3. Ejecutar la aplicación

1. Selecciona un emulador Android o conecta un dispositivo compatible.
2. Espera a que finalice la compilación.
3. Pulsa **Run ▶** en Android Studio.
4. Comprueba que aparezca la pantalla de bienvenida.
5. Pulsa **Comenzar** para ingresar a la pantalla principal.

### 4. Ejecutar desde Windows

También puedes utilizar la terminal de PowerShell:

```powershell
cd AeroViaPeru
.\gradlew.bat assembleDebug
```

Si la compilación finaliza correctamente, el APK de depuración normalmente se genera en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

La ubicación exacta puede variar según la configuración del proyecto.

## 🔄 Flujo de navegación

El flujo general de la aplicación comienza con la pantalla de bienvenida y continúa hacia la pantalla principal.

```text
        AeroVía Perú
              |
              v
     Pantalla de bienvenida
              |
          Comenzar
              |
              v
      Pantalla principal
              |
       +------+------+
       |             |
       v             v
  Consultar       Gestionar
    vuelos        reservas
                     |
              +------+------+
              |             |
              v             v
          Ver detalle     Editar reserva
```

Este diagrama representa el flujo conceptual de navegación. Las opciones y transiciones definitivas dependen de la implementación del proyecto.

## 👥 Participantes

El proyecto cuenta con los siguientes integrantes:

1. **Siri Vergara, Maria Alejandra**
2. **Villasante Contreras, Jean Paul**
3. **Santisteban Manrique, Adrian Daniel**

## 📌 Estado del proyecto

**Estado:** En desarrollo.

La aplicación cuenta con una pantalla de bienvenida funcional y módulos destinados a la consulta de vuelos y gestión de reservas. El proyecto puede continuar evolucionando mediante mejoras de diseño, validaciones de datos, pruebas funcionales y optimización de la experiencia del usuario.

## 🔐 Buenas prácticas de seguridad

Para mantener el repositorio organizado y seguro:

- No publicar contraseñas, tokens ni claves API.
- No subir archivos locales como `local.properties`.
- No incluir certificados privados ni archivos de firma de aplicaciones.
- Mantener un `.gitignore` actualizado.
- Revisar los archivos antes de realizar cada commit.
- Verificar los permisos y la configuración de la aplicación antes de distribuirla.

## 📂 Repositorio

Código fuente disponible en GitHub:

**[AeroViaPeru — GitHub](https://github.com/santiman0408-sudo/AeroViaPeru)**

---

**AeroVía Perú**  
*Tu próxima aventura comienza aquí.* ✈️
