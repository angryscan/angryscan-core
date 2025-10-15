package org.angryscan.common.extensions

import com.gliwka.hyperscan.wrapper.ExpressionFlag
import org.angryscan.common.engine.ExpressionOption

fun ExpressionOption.toExpressionFlag(): ExpressionFlag = when (this) {
    ExpressionOption.CASELESS -> ExpressionFlag.CASELESS
    ExpressionOption.DOTALL -> ExpressionFlag.DOTALL
    ExpressionOption.MULTILINE -> ExpressionFlag.MULTILINE
    ExpressionOption.UTF8 -> ExpressionFlag.UTF8
    ExpressionOption.SOM_LEFTMOST -> ExpressionFlag.SOM_LEFTMOST
    ExpressionOption.PREFILTER -> ExpressionFlag.PREFILTER
    ExpressionOption.SINGLE_MATCH -> ExpressionFlag.SINGLEMATCH
}