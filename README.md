# Rubik's Cube 3D Simulator

**Rubik's Cube 3D Simulator** is a native Android application that recreates the classic puzzle with smooth, interactive **3D graphics** powered by **OpenGL ES 3.0**. Rather than relying on an existing game framework, the project features a **custom-built rendering and game engine**, providing full control over graphics rendering, animations, input handling, and cube mechanics.

The project demonstrates low-level graphics programming, mathematical transformations, and real-time rendering while integrating a modern Jetpack Compose interface for application controls and user interaction.

---

## Highlights

- **Interactive 3D Rubik's Cube**
  - Fully rendered 3D cube with smooth animations.
  - Interactive camera controls for viewing the cube from any angle.
  - Realistic face rotations and cube manipulation.
  - Accurate Rubik's Cube movement mechanics.

- **Custom Game Engine**
  - Built entirely from scratch without external game engines.
  - Custom rendering pipeline.
  - Scene and object management.
  - Input processing and animation system.

- **Modern Android UI**
  - Jetpack Compose-based interface.
  - Seamless integration between Compose UI and OpenGL rendering.
  - Responsive controls for gameplay and configuration.

---

## Architecture

The project separates rendering, game logic, and UI into independent modules, making the engine reusable and easier to maintain.

### Core Technologies

- **OpenGL ES 3.0+**
  - Hardware-accelerated 3D rendering.
  - Vertex and fragment shaders.
  - Efficient GPU-based rendering pipeline.

- **Custom Game Engine**
  - Scene graph and object management.
  - Camera system.
  - Animation framework.
  - Input handling.
  - Rendering loop.

- **Jetpack Compose**
  - Declarative Android UI.
  - State-driven controls and settings.
  - Integration with the OpenGL rendering surface.

---

## Technical Focus

The project explores the fundamentals of real-time graphics programming and game engine architecture on Android.

Key areas include:

- OpenGL ES rendering pipeline.
- Matrix and vector mathematics.
- 3D transformations and camera systems.
- Shader programming.
- Real-time animation.
- Touch gesture handling for 3D interaction.
- Efficient rendering and GPU optimization.
- Synchronization between Compose UI and the rendering engine.

---

## Skills Demonstrated

- Android Development
- OpenGL ES 3.0+
- Real-Time 3D Graphics
- Custom Game Engine Development
- Shader Programming (GLSL)
- Matrix & Vector Mathematics
- Camera Systems
- Animation Systems
- Rendering Pipeline Design
- Touch Input Processing
- Jetpack Compose
- Performance Optimization
- Clean Architecture
- Graphics Programming