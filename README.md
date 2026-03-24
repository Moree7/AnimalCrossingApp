# 🌿 Animal Crossing App 🌿

Aplicación Android desarrollada en Kotlin con Jetpack Compose, inspirada en el universo de Animal Crossing.

Esta app funciona como un compañero del museo, permitiendo llevar un control tipo checklist de todos los elementos coleccionables del juego.

## 🏝️ ¿Qué puedes hacer en la app?

- 🐟 Registrar peces
- 🤿 Registrar pesca submarina
- 🦋 Registrar bichos
- 🦴 Registrar fósiles
- 🎨 Registrar obras de arte
- ☑️ Marcar los elementos ya conseguidos
- 🔍 Consultar información detallada de cada elemento
- 🧭 Navegar entre pantallas de forma fluida y sencilla

## 📱 Pantallas de la aplicación (se implementarán más adelante)

- Login (pantalla inicial ficticia)
- Home con acceso a las categorías
- Listas con checklist interactiva
- Detalle con información del coleccionable

## 🎨 Estilo y diseño

Inspirado directamente en Animal Crossing:

- 🌿 Colores pastel y naturales
- 🪵 Tonos suaves y cálidos
- 🧩 Cards redondeadas
- ✨ Interfaz limpia y relajante
- 🖋️ Tipografías amigables

## 🧭 Arquitectura del proyecto
```
com.example.animalcrossingapp
├── MainActivity.kt
├── AnimalCrossingApp.kt
├── data
│ ├── local
│ ├── remote
│ └── repository
├── di
│ └── AppModule.kt
├── domain
│ └── CollectibleType.kt
├── navigation
│ ├── AppNavigation.kt
│ └── Screen.kt
├── ui
│ ├── common
│ ├── debug
│ ├── detail
│ │ └── DetailScreen.kt
│ ├── home
│ │ └── HomeScreen.kt
│ ├── list
│ │ ├── CollectibleItem.kt
│ │ └── ListScreen.kt
│ ├── login
│ │ └── LoginScreen.kt
│ ├── model
│ │ └── CollectibleUi.kt
│ └── theme
├── viewModel
└── ui.theme
```

## 🛠️ Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Navigation Compose
- Room

## 🚧 Estado del proyecto

### 📌 Proyecto en desarrollo
La aplicación cuenta con una base funcional completa y está preparada para seguir ampliándose.

## 👤 Autores

- Moree7
- ihateblonde

