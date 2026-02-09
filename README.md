<div align="center">

<h1> Animal Crossing App </h1>

<p>
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white"/>
  <img src="https://img.shields.io/badge/Status-Active-2EA44F?style=flat-square"/>
</p>

</div>

<hr/>

## ¿Qué es esta app y para qué sirve?

Esta app es una guía personal de coleccionables inspirada en Animal Crossing.
En el juego, los jugadores pueden encontrar y donar distintos objetos a un museo.
La app te ayuda a llevar el control de esos objetos, aunque no juegues ni conozcas el juego.

> Piensa en ella como una lista de colecciones organizada y visual.

---

## ¿Qué tipo de cosas hay en la app?

La app está dividida en categorías, cada una con su propio color:

<div align="center">

🐟 **Peces** • 🐞 **Bichos (insectos)** • 🌊 **Criaturas marinas** • 🦴 **Fósiles** • 🖼️ **Obras de arte**

</div>

---

Cada elemento de la lista es un objeto coleccionable con:

- Un nombre
- A veces un subtítulo o descripción
- Un estado: donado o no donado

---

# Pantalla principal (Inicio)

Desde la pantalla de inicio puedes:

- Entrar en cualquier categoría (por ejemplo: Peces)
- Ver cuántos objetos llevas completados en cada una

No necesitas saber qué es cada cosa: la app te guía visualmente.

---

# Pantalla de lista (una categoría)

Aquí ves todos los objetos de una categoría.

Cada tarjeta muestra:

- Un checkbox → marca si ya lo has "donado"
- Un icono de información → abre los detalles
- Un icono de editar → cambia nombre o descripción

---

# Organización inteligente

Los objetos NO donados aparecen arriba  
Los donados se agrupan abajo en una sección plegable  
Así siempre ves primero lo que te falta  

Progreso
````
Progreso: 12 / 88 (15%)
````
Eso significa cuántos llevas completados.

Si tocas ese indicador, la app puede llevar directamente a la sección de donados.

<details>
<summary><b>🏅 Marcar un objeto como donado</b></summary>
Cuando marcas un objeto como donado, se mueve automáticamente a la sección de donados y tu progreso se actualiza.
</details>

<details>
<summary><b>📄 Pantalla de detalles</b></summary>
Aquí puedes ver toda la información del objeto: nombre, descripción, categoría y estado.
</details>

<details>
<summary><b>💡 Compartir por WhatsApp</b></summary>
Comparte información de objetos con otros jugadores directamente por WhatsApp.
</details>

<details>
<summary><b>🌐 Sin conexión y actualización automática</b></summary>
La app funciona sin internet y actualiza automáticamente cuando vuelves a tener conexión.
</details>

---
## Arquitectura
```
com.example.animalcrossingapp
├── MainActivity.kt                ## Actividad principal de la aplicación.
│                                  ## Es el punto de entrada y contiene el setContent
│                                  ## donde se inicializa Jetpack Compose y la navegación.

├── AnimalCrossingApp.kt           ## Clase Application.
│                                  ## Se encarga de la inicialización global de la app,
│                                  ## como la configuración de dependencias (Hilt).

├── data                           ## Capa de datos.
│ ├── local                        ## Gestión de datos locales (base de datos, cache).
│ ├── remote                       ## Gestión de datos remotos (API, servicios externos).
│ └── repository                   ## Repositorios que unifican el acceso a datos locales
│                                  ## y remotos y los exponen al dominio y a los ViewModels.

├── di                             ## Inyección de dependencias.
│ └── AppModule.kt                 ## Módulo donde se definen las dependencias principales
│                                  ## de la aplicación usando Hilt.

├── domain                         ## Capa de dominio.
│ └── CollectibleType.kt           ## Definición de tipos o modelos de negocio,
│                                  ## como las categorías de coleccionables.

├── navigation                     ## Gestión de la navegación.
│ ├── AppNavigation.kt             ## Define el NavHost y las rutas de navegación entre pantallas
│                              
│ └── Screen.kt                    ## Enum o sealed class que representa cada pantalla y sus rutas.
│                             
├── ui                             ## Capa de interfaz de usuario (Jetpack Compose).
│ ├── common                       ## Componentes reutilizables comunes a varias pantallas.
│ ├── debug                        ## Componentes o pantallas de depuración (si existen).
│ ├── detail
│ │ └── DetailScreen.kt            ## Pantalla de detalle de un coleccionable.
│                                  ## Muestra toda la información del objeto seleccionado.
│ ├── home
│ │ └── HomeScreen.kt              ## Pantalla principal (inicio).
│                                  ## Muestra las categorías y el progreso general.
│ ├── list
│ │ ├── CollectibleItem.kt         ## Composable que representa un elemento de la lista
│ │                            
│ │ └── ListScreen.kt              ## Pantalla de lista de una categoría concreta.
│                                  ## Permite marcar objetos como donados o ver detalles.
│ ├── login
│ │ └── LoginScreen.kt             ## Pantalla de inicio de sesión (si aplica).
│ ├── model
│ │ └── CollectibleUi.kt           ## Modelo de datos específico para la UI, adaptado para ser mostrado en pantalla.
│                              
│ └── theme                      
├── viewModel                      ## Gestionan el estado de la UI y la lógica de presentación siguiendo el patrón MVVM.                                                           
└── ui.theme                       

````
## En resumen

Esta app sirve para:

**📋 Llevar listas de coleccionables**

**✅ Marcar lo que ya tienes**

**🎯 Ver tu progreso claramente**

**📤 Compartir información fácilmente**

**🧠 Usarla sin saber nada de Animal Crossing**

*Es como un álbum de cromos digital, pero organizado, bonito y automático.*


## 👤 Autores
- Moree7
- ihateblonde

