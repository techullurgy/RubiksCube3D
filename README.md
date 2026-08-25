# RubiksCube

## 1. Description
**RubiksCube** is a 3D interactive Rubik's Cube game and solver platform built with Kotlin Multiplatform (KMP) and Compose Multiplatform. Cross-compiling to Android, iOS, Desktop (JVM), and Web (Wasm/JS), the application renders a fully rotatable, interactive 3D Rubik's Cube featuring real-time animation of face turns, camera rotation, move histories, and custom rendering shaders.

---

## 2. Technical Special Aspects
- **Custom 3D Game Engine (`gameengine`)**: Built from raw graphics principles without heavy third-party game engines, featuring custom matrix transformation pipelines (`Transformation.kt`), projection & view camera math (`Camera.kt`), geometry buffers (`Square`, `Face`, `Cube`), and shader management (`Shader.kt`).
- **Multiplatform Graphics Acceleration Engine**: Target-native OpenGL/WebGL integration:
  - Android: OpenGL ES 2.0/3.0 via `AndroidView` GLSurfaceView
  - Desktop/JVM: LWJGL OpenGL context rendering
  - Web (Wasm/JS): WebGL context canvas bindings
- **State & Permutation Model**: High-performance internal representation of the 3x3x3 cube state (`RubiksCubeGame`), supporting standard Singmaster notation turns (F, B, U, D, L, R and prime moves) with smooth rotational interpolation.
- **Interactive Gesture Raycasting**: Camera controls and direct touch/drag cube layer rotation (`RubiksCubeGameRenderer`, `CameraGears`).

---

## 3. Technologies Used
- **Languages**: Kotlin (100% Shared Graphics Engine & Game Logic), Swift (iOS host)
- **Frameworks**: Compose Multiplatform, Kotlin Multiplatform
- **Graphics APIs**: OpenGL ES, WebGL, LWJGL
- **Mathematics**: Custom 3D Linear Algebra & Matrix Math Engine
- **Build System**: Gradle KTS Multi-Module (`gameengine`, `shared`, `androidApp`, `desktopApp`, `webApp`, `iosApp`)

---

## 4. Testing Technologies
- **Unit & Math Testing**: `kotlin.test`
- **Target Test Suites**:
  - `commonTest`: Cube state permutation invariants, face rotation correctness, and transformation matrix calculations
  - `jvmTest` & `iosTest`: Graphics pipeline initialization tests

---

## 5. Cloud Technologies
- N/A (Embedded cross-platform 3D graphics application).

---

## 6. ROADMAP
- [ ] Integrate two-phase Kociemba algorithm solver for step-by-step optimal solving guides.
- [ ] Support customizable puzzle sizes (2x2x2, 4x4x4, 5x5x5, Pyraminx).
- [ ] Implement WCA-compliant speedcubing timer with inspection countdowns and statistics.
- [ ] Add online real-time multiplayer speed-solving battles over WebSockets.
