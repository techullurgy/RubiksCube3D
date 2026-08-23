package com.techullurgy.rubikscube.core.renderer

import com.techullurgy.gameengine.BufferBit
import com.techullurgy.gameengine.Camera
import com.techullurgy.gameengine.Feature
import com.techullurgy.gameengine.OpenGL
import com.techullurgy.gameengine.Transformation
import com.techullurgy.gameengine.getInstance
import com.techullurgy.gameengine.rotate2D
import com.techullurgy.rubikscube.core.models.Move
import com.techullurgy.rubikscube.core.shapes.Cube
import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.Float3
import kotlin.math.abs
import kotlin.properties.Delegates

abstract class AbstractRubiksCubeGameRenderer: RubiksCubeGameRenderer {
    private val cubes = mutableListOf<Cube>()

    private var width by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()

    private lateinit var camera: Camera

    private var isStarted = false

    private var worldRotationX = 0f
    private var worldRotationY = 0f
    private var worldRotationZ = 0f

    private var currentMove: Move? = null

    final override fun rotateCameraX(angle: Float) {
        if (isStarted) {
            worldRotationX = angle
        }
    }

    final override fun rotateCameraY(angle: Float) {
        if (isStarted) {
            worldRotationY = angle
        }
    }

    final override fun rotateCameraZ(angle: Float) {
        if (isStarted) {
            worldRotationZ = angle
        }
    }

    final override fun turnX(index: Int, dir: Int) {
        currentMove = null
        cubes
            .filter { it.x == index }
            .forEach { cube ->
                val new2DPosition = rotate2D(dir * 90f, Float2(cube.y.toFloat(), cube.z.toFloat()))
                cube.update(Float3(cube.x.toFloat(), new2DPosition.x, new2DPosition.y))
                cube.turnFacesX(dir)
            }
    }

    final override fun turnY(index: Int, dir: Int) {
        currentMove = null
        cubes
            .filter { it.y == index }
            .forEach { cube ->
                val new2DPosition = rotate2D(dir * 90f, Float2(cube.x.toFloat(), cube.z.toFloat()))
                cube.update(Float3(new2DPosition.x, cube.y.toFloat(), new2DPosition.y))
                cube.turnFacesY(dir)
            }
    }

    final override fun turnZ(index: Int, dir: Int) {
        currentMove = null
        cubes
            .filter { it.z == index }
            .forEach { cube ->
                val new2DPosition = rotate2D(dir * 90f, Float2(cube.x.toFloat(), cube.y.toFloat()))
                cube.update(Float3(new2DPosition.x, new2DPosition.y, cube.z.toFloat()))
                cube.turnFacesZ(dir)
            }
    }

    final override fun updateCurrentMove(
        move: Move,
        angle: Float
    ) {
        if (currentMove == null) {
            currentMove = move.copy()
        }
        currentMove!!.update(angle)
    }

    final override fun onSurfaceCreated() {
        isStarted = true

        OpenGL.glClearColor(0f, 0f, 0f, 1f)

        (-1..1).forEach { x ->
            (-1..1).forEach { y ->
                (-1..1).forEach { z ->
                    cubes.add(Cube(Float3(x.toFloat(), y.toFloat(), z.toFloat())))
                }
            }
        }
    }

    final override fun onSurfaceChanged(width: Int, height: Int) {
        OpenGL.glViewport(0, 0, width, height)
        OpenGL.glEnable(Feature.DEPTH_TEST)
        this.width = width
        this.height = height

        camera = Camera.getInstance(width, height)
    }

    final override fun onDrawFrame() {
        OpenGL.glClear(listOf(BufferBit.COLOR, BufferBit.DEPTH))
        render()
        Transformation.clearAll()
    }

    private fun render() {
        Transformation.push()
        Transformation.rotateX(worldRotationX)
        Transformation.rotateY(worldRotationY)
        Transformation.rotateZ(worldRotationZ)
        cubes
            .forEach { cube ->
                Transformation.push()
                currentMove?.let {
                    if (abs(cube.x) > 0 && cube.x == it.x) {
                        Transformation.rotateX(it.dir * it.angle)
                    } else if (abs(cube.y) > 0 && cube.y == it.y) {
                        Transformation.rotateY(-1f * it.dir * it.angle)
//                        Transformation.rotateY(it.dir * it.angle)
                    } else if (abs(cube.z) > 0 && cube.z == it.z) {
                        Transformation.rotateZ(it.dir * it.angle)
                    }
                }
                cube.draw()
                Transformation.pop()
            }
        Transformation.pop()
    }
}