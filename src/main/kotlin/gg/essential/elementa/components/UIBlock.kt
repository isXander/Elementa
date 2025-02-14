package gg.essential.elementa.components

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.ColorConstraint
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.state.State
import gg.essential.elementa.state.toConstraint
import gg.essential.universal.UGraphics
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * Extremely simple component that simply draws a colored rectangle.
 */
open class UIBlock(colorConstraint: ColorConstraint = Color.WHITE.toConstraint()) : UIComponent() {
    constructor(color: Color) : this(color.toConstraint())

    constructor(colorState: State<Color>) : this(colorState.toConstraint())

    init {
        setColor(colorConstraint)
    }

    override fun draw() {
        beforeDraw()

        val x = this.getLeft().toDouble()
        val y = this.getTop().toDouble()
        val x2 = this.getRight().toDouble()
        val y2 = this.getBottom().toDouble()

        val color = getColor()
        if (color.alpha == 0)
            return super.draw()

        UGraphics.pushMatrix()

        drawBlock(color, x, y, x2, y2)

        UGraphics.popMatrix()

        super.draw()
    }

    companion object {
        fun drawBlock(color: Color, x1: Double, y1: Double, x2: Double, y2: Double) {
            UGraphics.enableBlend()
            UGraphics.disableTexture2D()
            UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)

            val worldRenderer = UGraphics.getFromTessellator()

            val red = color.red.toFloat() / 255f
            val green = color.green.toFloat() / 255f
            val blue = color.blue.toFloat() / 255f
            val alpha = color.alpha.toFloat() / 255f

            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
            worldRenderer.pos(x1, y2, 0.0).color(red, green, blue, alpha).endVertex()
            worldRenderer.pos(x2, y2, 0.0).color(red, green, blue, alpha).endVertex()
            worldRenderer.pos(x2, y1, 0.0).color(red, green, blue, alpha).endVertex()
            worldRenderer.pos(x1, y1, 0.0).color(red, green, blue, alpha).endVertex()
            UGraphics.draw()


            UGraphics.enableTexture2D()
            UGraphics.disableBlend()
        }

        fun drawBlockSized(color: Color, x: Double, y: Double, width: Double, height: Double) {
            drawBlock(color, x, y, x + width, y + height)
        }
    }
}
