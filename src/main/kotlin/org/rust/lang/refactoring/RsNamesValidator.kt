package org.rust.lang.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import org.rust.lang.core.lexer.RsLexer
import org.rust.lang.core.psi.RS_KEYWORDS
import org.rust.lang.core.psi.RsElementTypes.IDENTIFIER
import org.rust.lang.core.psi.RsElementTypes.QUOTE_IDENTIFIER

class RsNamesValidator : NamesValidator {
    override fun isKeyword(name: String, project: Project?): Boolean =
        isKeyword(name, project, true)

    fun isKeyword(name: String, @Suppress("UNUSED_PARAMETER") project: Project?, withPrimitives: Boolean): Boolean =
        getLexerType(name) in RS_KEYWORDS || (withPrimitives && name in PrimitiveTypes)

    override fun isIdentifier(name: String, project: Project?): Boolean =
        isIdentifier(name, project, true)

    fun isIdentifier(name: String, @Suppress("UNUSED_PARAMETER") project: Project?, withPrimitives: Boolean): Boolean =
        when (getLexerType(name)) {
            IDENTIFIER -> !withPrimitives || name !in PrimitiveTypes
            QUOTE_IDENTIFIER -> true
            else -> false
        }

    private fun getLexerType(text: String): IElementType? {
        val lexer = RsLexer()
        lexer.start(text)
        if (lexer.tokenEnd == text.length) {
            return lexer.tokenType
        } else {
            return null
        }
    }

    companion object {
        val PrimitiveTypes = arrayOf(
            "bool",
            "char",
            "i8",
            "i16",
            "i32",
            "i64",
            "u8",
            "u16",
            "u32",
            "u64",
            "isize",
            "usize",
            "f32",
            "f64",
            "str"
        )

        val PredefinedLifetimes = arrayOf("'static")
    }
}
