# 🚀 Hacker News Android App - Prueba Técnica Raven


**Aplicación Android nativa que muestra artículos de Hacker News con arquitectura MVVM limpia**

[Características](#-características) • [Arquitectura](#️-arquitectura) • [Tecnologías](#-tecnologías) • [Instalación](#-instalación) • [Testing](#-testing)
 

---

## 📱 Características Principales

### ✅ Requisitos Funcionales Implementados

| # | Funcionalidad | Estado | Detalles |
|---|---------------|--------|----------|
| 1️⃣ | **Listado de Artículos** | ✅ COMPLETO | Conexión a API de Hacker News, Pull-to-refresh, Soporte offline |
| 2️⃣ | **Eliminar Artículos** | ✅ COMPLETO | Swipe-to-delete, Persistencia en BD, No reaparecen al refrescar |
| 3️⃣ | **Ver Detalle** | ✅ COMPLETO | WebView integrado, Navegación fluida, Carga de artículo completo |

### 🎨 Características Adicionales

- ✨ **UI Moderna** - Material Design 3 con paleta Hacker News
- 🔄 **Estados de Carga** - Loading, Error, Empty states profesionales
- 📴 **Modo Offline** - Cache local con Room Database
- 🎯 **Animaciones** - Transiciones suaves y feedback visual
- 🧭 **Navigation Component** - Navegación type-safe
- 💉 **Dependency Injection** - Hilt para gestión de dependencias

---

## 🏗️ Arquitectura MVVM

### 📐 Diagrama de Arquitectura
```
┌─────────────────────────────────────────────┐
│              UI Layer (View)                │
│  • Fragments (observan)                     │
│  • Adapters (presentan datos)              │
│  • Navigation (flujo de pantallas)         │
└──────────────────┬──────────────────────────┘
                   │ observa StateFlow
┌──────────────────▼──────────────────────────┐
│         Presentation Layer                  │
│  • ViewModels (manejan UI state)           │
│  • UI States (data classes inmutables)     │
└──────────────────┬──────────────────────────┘
                   │ invoca Use Cases
┌──────────────────▼──────────────────────────┐
│          Domain Layer                       │
│  • Use Cases (lógica de negocio)           │
│  • Mappers (conversiones DTO↔Model)        │
└──────────────────┬──────────────────────────┘
                   │ llama Repository
┌──────────────────▼──────────────────────────┐
│           Data Layer                        │
│  • Repository (Single Source of Truth)     │
│  • Data Sources:                            │
│    - Remote (Retrofit + OkHttp)            │
│    - Local (Room Database)                 │
│  • Models: DTO, Entity, Domain             │
└─────────────────────────────────────────────┘
```

### 🎯 Principios Aplicados

- ✅ **Separación de Responsabilidades** - Cada capa tiene una función clara
- ✅ **Single Source of Truth** - Repository como única fuente de verdad
- ✅ **Unidirectional Data Flow** - Datos fluyen en una dirección
- ✅ **Dependency Inversion** - Abstracciones mediante interfaces
- ✅ **Clean Architecture** - Capas independientes y testables

---

## 🛠️ Tecnologías Utilizadas

### 📦 Stack Técnico

| Categoría | Tecnología | Versión | Propósito |
|-----------|------------|---------|-----------|
| **Lenguaje** | Kotlin | 2.0.21 | Lenguaje principal |
| **UI** | Material Design 3 | Latest | Componentes UI modernos |
| **Arquitectura** | MVVM + Clean Architecture | - | Separación de capas |
| **DI** | Hilt | 2.52 | Inyección de dependencias |
| **Networking** | Retrofit + OkHttp | 2.11.0 / 4.12.0 | Cliente REST |
| **Database** | Room | 2.6.1 | Persistencia local |
| **Async** | Coroutines + Flow | 1.10.1 | Programación asíncrona |
| **Navigation** | Navigation Component | 2.8.5 | Navegación type-safe |
| **Testing** | JUnit + Mockito-Kotlin | 4.13.2 / 5.4.0 | Pruebas unitarias |
| **Build** | KSP | 2.0.21-1.0.29 | Procesamiento de anotaciones |

### 📂 Estructura del Proyecto
```
app/src/main/java/com/example/pruebatecnicaraven/
├── 📁 data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── ArticleDAO.kt
│   │   ├── database/
│   │   │   └── ArticleDataBase.kt
│   │   └── entity/
│   │       ├── ArticleEntity.kt
│   │       └── DeletedArticleEntity.kt
│   ├── model/
│   │   ├── Article.kt
│   │   └── ArticleState.kt
│   ├── remote/
│   │   ├── api/
│   │   │   └── HackerNewsApi.kt
│   │   │   
│   │   └── dto/
│   │       ├── ArticleDTO.kt
│   │       └── ArticleResponse.kt
│   │        
│   └── repository/
│       ├── ArticleRepository.kt
│       └── ArticleRepositoryImpl.kt
├── 📁 di/
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
├── 📁 domain/
│   ├── mapper/
│   │   ├── ArticleEntityMapper.kt
│   │   └── ArticleMapper.kt
│   └── usecase/
│       ├── DeleteArticleUseCase.kt
│       ├── GetArticlesUseCase.kt
│       └── RefreshArticleUseCase.kt
├── 📁 ui/
│   ├── articles/
│   │   ├── adapter/
│   │   │   ├── ArticlesAdapter.kt
│   │   │   └── SwipeToDeleteCallback.kt
│   │   ├── ArticleDetailFragment.kt
│   │   ├── ArticlesFragment.kt
│   │   ├── ArticleState.kt
│   │   └── ArticlesViewModel.kt
│   ├── base/
│   ├── details/
│   │   ├── ArticleDetailState.kt
│   │   └── ArticleDetailViewModel.kt
│   └── main/
│       └── MainActivity.kt
├── 📁 util/
│   ├── Constants.kt
│   ├── DateUtils.kt
│   ├── Extensions.kt
│   ├── NetworkHelper.kt
│   └── Resource.kt
└── App.kt

app/src/test/java/com/example/pruebatecnicaraven/
└── domain/
    ├── mapper/
    │   ├── ArticleEntityMapperTest.kt
    │   └── ArticleMapperTest.kt
    └── usecase/
    ├── DeleteArticleUseCaseTest.kt
    ├── GetArticlesUseCaseTest.kt
    └── RefreshArticlesUseCaseTest.kt 
```

---

### 🎯 Uso de la Aplicación

1. **Ver Artículos** - La app carga automáticamente artículos sobre Android
2. **Refrescar** - Desliza hacia abajo para actualizar (Pull-to-refresh)
3. **Eliminar** - Desliza un artículo hacia la izquierda para eliminarlo
4. **Ver Detalle** - Toca un artículo para ver el contenido completo en WebView
5. **Modo Offline** - Sin internet, la app muestra artículos guardados en cache

---

## 🧪 Testing

### ✅ Pruebas Unitarias Implementadas

La aplicación cuenta con **cobertura de pruebas unitarias** para las capas críticas:
```tree
app/src/test/java/
├── domain/usecase/
│   ├── GetArticlesUseCaseTest.kt       // Obtención de artículos
│   └── DeleteArticleUseCaseTest.kt     // Eliminación
└── ui/articles/
    └── ArticlesViewModelTest.kt        // Lógica de presentación
```

### 📊 Herramientas de Testing

- **JUnit 4** - Framework de pruebas
- **Mockito-Kotlin** - Mocking de dependencias
- **Coroutines Test** - Testing de código asíncrono
- **Arch Core Testing** - Testing de LiveData/StateFlow

---

## 🎨 Capturas de Pantalla

### 📱 Pantalla Principal
- Lista de artículos con diseño moderno
- Cards con elevación y sombras
- Indicadores visuales (autor, fecha)
- Pull-to-refresh funcional

### 🗑️ Swipe to Delete
- Animación suave al deslizar
- Fondo rojo con icono de eliminación
- Feedback visual inmediato

### 🌐 Detalle del Artículo
- WebView integrado en la app
- Barra de navegación con título
- Loading indicator mientras carga

### 📴 Estado Offline
- Icono y mensaje informativo
- Datos en cache disponibles
- Sin errores ni crashes

---

## 🔧 Configuración Avanzada

### 🌐 API Endpoint

La aplicación consume la API de Hacker News:
```
https://hn.algolia.com/api/v1/search_by_date?query=android
```

### 🗄️ Base de Datos Local

Room Database con 2 tablas:
- `articles` - Cache de artículos
- `deleted_articles` - IDs de artículos eliminados

---

## 👨‍💻 Autor

**Desarrollado para Prueba Técnica - Raven Inc.**

- Kevin Hernandez - Android Developer
---
 