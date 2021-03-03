package club.sk1er.elementa.markdown.drawables

import club.sk1er.elementa.components.UIBlock
import club.sk1er.elementa.markdown.DrawState
import club.sk1er.elementa.markdown.MarkdownConfig

class HeaderDrawable(
    config: MarkdownConfig,
    private val level: Int,
    private val paragraph: ParagraphDrawable
) : Drawable(config) {
    override val children: List<Drawable> get() = listOf(paragraph)

    init {
        paragraph.parent = this
    }

    private val headerConfig = when (level) {
        1 -> config.headerConfig.level1
        2 -> config.headerConfig.level2
        3 -> config.headerConfig.level3
        4 -> config.headerConfig.level4
        5 -> config.headerConfig.level5
        6 -> config.headerConfig.level6
        else -> throw IllegalStateException()
    }

    init {
        paragraph.scaleModifier = headerConfig.textScale
        trim(paragraph)
    }

    override fun layoutImpl(x: Float, y: Float, width: Float): Layout {
        val spaceBefore = if (insertSpaceBefore) headerConfig.verticalSpaceBefore else 0f
        val spaceAfter = if (insertSpaceAfter) headerConfig.verticalSpaceAfter else 0f
        paragraph.layout(x, y + spaceBefore, width)

        val height = spaceBefore + paragraph.height + spaceAfter + if (headerConfig.hasDivider) {
            headerConfig.spaceBeforeDivider + headerConfig.dividerWidth
        } else 0f

        return Layout(
            x,
            y,
            width,
            height,
            Margin(0f, spaceBefore, 0f, spaceAfter)
        )
    }

    override fun draw(state: DrawState) {
        paragraph.draw(state)

        if (headerConfig.hasDivider) {
            val y = layout.bottom - layout.margin.bottom - headerConfig.dividerWidth
            UIBlock.drawBlockSized(
                headerConfig.dividerColor, 
                (x + state.xShift).toDouble(),
                (y + state.yShift).toDouble(),
                width.toDouble(),
                headerConfig.dividerWidth.toDouble()
            )
        }
    }

    override fun cursorAt(mouseX: Float, mouseY: Float) = paragraph.cursorAt(mouseX, mouseY)
    override fun cursorAtStart() = paragraph.cursorAtStart()
    override fun cursorAtEnd() = paragraph.cursorAtEnd()
}
