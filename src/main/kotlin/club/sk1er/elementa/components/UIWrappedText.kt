package club.sk1er.elementa.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.constraints.PixelConstraint
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager

/**
 * Simple text component that draws its given [text] at the scale determined by
 * this component's width & height constrains.
 */
open class UIWrappedText @JvmOverloads constructor(private var text: String = "", private var shadow: Boolean = true) : UIComponent() {

    private var textWidth: Float = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text).toFloat()

    init {
        setWidth(PixelConstraint(textWidth))
    }

    fun getText() = text
    fun setText(text: String) = apply {
        this.text = text
        textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text).toFloat()
    }

    fun getShadow() = shadow
    fun setShadow(shadow: Boolean) = apply { this.shadow = shadow }

    /**
     * Returns the text width if no scale is applied to the text
     */
    fun getTextWidth() = textWidth

    override fun getHeight(): Float {
        val width = getWidth() / getTextScale()
        val lines = Minecraft.getMinecraft().fontRendererObj.listFormattedStringToWidth(text, width.toInt())

        return lines.size * 9f * getTextScale()
    }

    override fun getBottom(): Float {
        return getTop() + getHeight()
    }

    override fun draw() {
        beforeDraw()

        val x = getLeft() / getTextScale()
        val y = getTop() / getTextScale()
        val width = getWidth() / getTextScale()
        val color = getColor()

        GlStateManager.enableBlend()

        GlStateManager.scale(getTextScale().toDouble(), getTextScale().toDouble(), 1.0)

        Minecraft.getMinecraft().fontRendererObj.drawSplitString(text, x.toInt(), y.toInt(), width.toInt(), color.rgb)

        GlStateManager.scale(1 / getTextScale().toDouble(), 1 / getTextScale().toDouble(), 1.0)

        super.draw()
    }
}