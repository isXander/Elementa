package gg.essential.elementa.components

import gg.essential.elementa.state.BasicState
import gg.essential.elementa.state.State
import gg.essential.universal.UGraphics
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * Variant of [UIBlock] with two colours that fade into each other in a
 * gradient pattern.
 */
open class GradientComponent @JvmOverloads constructor(
    startColor: Color = Color.WHITE,
    endColor: Color = Color.WHITE,
    direction: GradientDirection = GradientDirection.TOP_TO_BOTTOM
) : UIBlock(Color(0, 0, 0, 0)) {
    private var startColorState: State<Color> = BasicState(startColor)
    private var endColorState: State<Color> = BasicState(endColor)
    private var directionState: State<GradientDirection> = BasicState(direction)

    fun getStartColor(): Color = startColorState.get()
    fun setStartColor(startColor: Color) = apply {
        startColorState.set(startColor)
    }
    fun bindStartColor(newStartColorState: State<Color>) = apply {
        startColorState = newStartColorState
    }

    fun getEndColor(): Color = endColorState.get()
    fun setEndColor(endColor: Color) = apply {
        endColorState.set(endColor)
    }
    fun bindEndColor(newEndColorState: State<Color>) = apply {
        endColorState = newEndColorState
    }

    fun getDirection(): GradientDirection = directionState.get()
    fun setDirection(direction: GradientDirection) = apply {
        directionState.set(direction)
    }
    fun bindDirection(newDirectionState: State<GradientDirection>) = apply {
        directionState = newDirectionState
    }

    override fun draw() {
        beforeDraw()

        val x = this.getLeft().toDouble()
        val y = this.getTop().toDouble()
        val x2 = this.getRight().toDouble()
        val y2 = this.getBottom().toDouble()

        UGraphics.pushMatrix()
        drawGradientBlock(
            x,
            y,
            x2,
            y2,
            startColorState.get(),
            endColorState.get(),
            directionState.get()
        )
        UGraphics.popMatrix()

        super.draw()
    }

    enum class GradientDirection {
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP,
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT;

        fun getGradientColors(startColor: Color, endColor: Color): GradientColors = when (this) {
            TOP_TO_BOTTOM -> GradientColors(startColor, startColor, endColor, endColor)
            BOTTOM_TO_TOP -> GradientColors(endColor, endColor, startColor, startColor)
            LEFT_TO_RIGHT -> GradientColors(startColor, endColor, startColor, endColor)
            RIGHT_TO_LEFT -> GradientColors(endColor, startColor, endColor, startColor)
        }
    }

    data class GradientColors(val topLeft: Color, val topRight: Color, val bottomLeft: Color, val bottomRight: Color)

    companion object {

        @Deprecated(
            "This method does not allow for gradients to be rendered at sub-pixel positions. Use the Double variant instead and do not cast to Int.",
            ReplaceWith("drawGradientBlock(x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble(), startColor, endColor, direction)")
        )
        fun drawGradientBlock(
            x1: Int,
            y1: Int,
            x2: Int,
            y2: Int,
            startColor: Color,
            endColor: Color,
            direction: GradientDirection
        ): Unit = drawGradientBlock(x1.toDouble(), y1.toDouble(), x2.toDouble(), y2.toDouble(), startColor, endColor, direction)

        /**
         * Draw a rectangle with a gradient effect.
         */
        fun drawGradientBlock(
            x1: Double,
            y1: Double,
            x2: Double,
            y2: Double,
            startColor: Color,
            endColor: Color,
            direction: GradientDirection
        ) {
            UGraphics.disableTexture2D()
            UGraphics.enableBlend()
            UGraphics.disableAlpha()
            UGraphics.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
            UGraphics.shadeModel(GL11.GL_SMOOTH)

            val colours = direction.getGradientColors(startColor, endColor)
            val tessellator = UGraphics.getFromTessellator()
            tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
            tessellator.pos(x2, y1, 0.0).color(colours.topRight).endVertex()
            tessellator.pos(x1, y1, 0.0).color(colours.topLeft).endVertex()
            tessellator.pos(x1, y2, 0.0).color(colours.bottomLeft).endVertex()
            tessellator.pos(x2, y2, 0.0).color(colours.bottomRight).endVertex()
            UGraphics.draw()

            UGraphics.shadeModel(GL11.GL_FLAT)
            UGraphics.disableBlend()
            UGraphics.enableAlpha()
            UGraphics.enableTexture2D()
        }
    }
}