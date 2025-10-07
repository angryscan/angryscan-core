package info.downdetector.bigdatascanner.common.extensions

import com.gliwka.hyperscan.wrapper.ExpressionFlag
import info.downdetector.bigdatascanner.common.engine.ExpressionOption

fun ExpressionOption.toExpressionFlag(): ExpressionFlag = when (this) {
    ExpressionOption.CASELESS -> ExpressionFlag.CASELESS
    ExpressionOption.DOTALL -> ExpressionFlag.DOTALL
    ExpressionOption.MULTILINE -> ExpressionFlag.MULTILINE
    ExpressionOption.UTF8 -> ExpressionFlag.UTF8
    ExpressionOption.SOM_LEFTMOST -> ExpressionFlag.SOM_LEFTMOST
    ExpressionOption.PREFILTER -> ExpressionFlag.PREFILTER
}