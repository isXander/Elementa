package gg.essential.elementa.markdown.drawables

import gg.essential.elementa.markdown.DrawState
import gg.essential.elementa.markdown.MarkdownComponent
import gg.essential.elementa.markdown.MarkdownConfig
import gg.essential.elementa.markdown.selection.Cursor
import gg.essential.elementa.markdown.selection.TextCursor

/**
 * A soft break is one line break between lines of markdown text.
 */
class SoftBreakDrawable(md: MarkdownComponent) : Drawable(md) {
    override fun layoutImpl(x: Float, y: Float, width: Float): Layout {
        TODO("Not yet implemented")
    }

    override fun draw(state: DrawState) {
        TODO("Not yet implemented")
    }

    override fun cursorAt(mouseX: Float, mouseY: Float, dragged: Boolean, mouseButton: Int): Cursor<*> {
        TODO("Not yet implemented")
    }

    override fun cursorAtStart(): Cursor<*> {
        TODO("Not yet implemented")
    }

    override fun cursorAtEnd(): Cursor<*> {
        TODO("Not yet implemented")
    }

    override fun selectedText(asMarkdown: Boolean): String {
        TODO("Not yet implemented")
    }
}
