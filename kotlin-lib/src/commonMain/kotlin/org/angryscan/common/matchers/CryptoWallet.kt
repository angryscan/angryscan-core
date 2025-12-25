package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object CryptoWallet : IHyperMatcher, IKotlinMatcher {
    override val name = "Crypto Wallet Address"

    override val javaPatterns = listOf(
        // Bitcoin
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d\w])[\(\)\[\]\{\}\"'=]|(?<![\d\w])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s|(?<=[=]))(?<![\d])[13][a-km-zA-HJ-NP-Z1-9]{25,41}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?![\w\d]))""",
        
        // Bitcoin
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)bc1[a-z0-9]{39,41}""",
        
        // Litecoin
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)ltc1[a-z0-9]{39,41}""",
        
        // Ethereum
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)0x[a-fA-F0-9]{40}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Litecoin Legacy
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26,41}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Ripple
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{24,41}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Bitcoin Cash
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)(?:bitcoincash:)?[qp][qpzry9x8gf2tvdw0s3jn54khce6mua7l]{41}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Dogecoin
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26,39}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Tron
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)T[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Dash
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{25,41}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Zcash
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)t[13][a-km-zA-HJ-NP-Z1-9]{33,41}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Terra
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)terra1[a-z0-9]{38}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // Tezos
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)(?:tz[123]|KT1)[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?=(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)(?!\w))""",
        
        // TON
        """(?:(?<![\w\d])(?:^|\s)|(?<![\d])[\(\)\[\]\{\}\"'=]|(?<![\d])[#=\-\(\)\[\]\{\}\"'.,;!?]\s|:\s)(?:EQC|UQB|EQD)[A-Z0-9a-z+/]{41}"""
    )
    
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        // Bitcoin
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{25}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{26}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{27}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{28}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{29}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{30}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{31}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{32}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|(?:[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s|\s)|[\(\)\[\]\{\}\"'=])[13][a-km-zA-HJ-NP-Z1-9]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{25}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{26}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{27}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{28}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{29}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{30}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{31}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{32}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """=[13][a-km-zA-HJ-NP-Z1-9]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Bitcoin
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)bc1[a-z0-9]{39}""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)bc1[a-z0-9]{40}""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)bc1[a-z0-9]{41}""",
        
        // Litecoin
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)ltc1[a-z0-9]{39}""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)ltc1[a-z0-9]{40}""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)ltc1[a-z0-9]{41}""",
        
        // Ethereum
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)0x[a-fA-F0-9]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Litecoin Legacy
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{27}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{28}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{29}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{30}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{31}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{32}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)[LM][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Ripple
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{24}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{25}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{27}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{28}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{29}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{30}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{31}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{32}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)r[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Bitcoin Cash
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)(?:bitcoincash:)?[qp][qpzry9x8gf2tvdw0s3jn54khce6mua7l]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Dogecoin
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{27}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{28}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{29}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{30}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{31}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{32}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)D[5-9A-HJ-NP-U][123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Tron
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)T[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Dash
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{25}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{26}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{27}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{28}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{29}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{30}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{31}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{32}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)X[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Zcash
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{34}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{35}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{36}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{37}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{39}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{40}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)t[13][a-km-zA-HJ-NP-Z1-9]{41}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Terra
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)terra1[a-z0-9]{38}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // Tezos
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)tz1[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)tz2[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)tz3[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)KT1[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{33}(?:\.\s|\.|;|\s|\r?\n|[\)\]\}\"']|$)""",
        
        // TON
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)EQC[A-Z0-9a-z+/]{41}""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)UQB[A-Z0-9a-z+/]{41}""",
        """(?:^|\s|[\(\)\[\]\{\}\"'=]|[#:=\-\(\)\[\]\{\}\"'.,;:!?]\s)EQD[A-Z0-9a-z+/]{41}"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Remove whitespace and special characters from start/end
        val trimmedValue = value.trim().trimStart('(', '[', '{', '"', '\'', '=', ':', '-', '#', '.', ',', ';', '!', '?')
            .trimEnd(')', ']', '}', '"', '\'', '=', ':', '-', '#', '.', ',', ';', '!', '?', '.', ';')
        
        // Check special formats first (Bech32, Terra, TON, Ethereum, Tezos)
        val specialFormats = listOf("bc1", "ltc1", "terra1", "0x", "EQC", "UQB", "EQD", "tz1", "tz2", "tz3", "KT1")
        val isSpecialFormat = specialFormats.any { trimmedValue.startsWith(it, ignoreCase = true) }
        
        // Filter strings shorter than minimum length (24 for Ripple, 25+ for others)
        if (!isSpecialFormat && trimmedValue.length < 24) {
            return false
        }
        
        // Filter hex-only strings without 0x prefix
        if (!trimmedValue.startsWith("0x", ignoreCase = true) && trimmedValue.length >= 25 && trimmedValue.length <= 41) {
            val hexAlphabet = "0123456789ABCDEFabcdef"
            val isOnlyHex = trimmedValue.all { it in hexAlphabet }
            if (isOnlyHex) {
                return false
            }
        }
        
        // Filter strings starting with lowercase 'd' (Dogecoin addresses start with 'D')
        if (!isSpecialFormat && trimmedValue.isNotEmpty() && trimmedValue[0] == 'd') {
            return false
        }
        
        // Filter strings starting with lowercase 'l' (Litecoin Legacy starts with 'L' or 'M', Bech32 with 'ltc1')
        if (!isSpecialFormat && trimmedValue.isNotEmpty() && trimmedValue[0] == 'l') {
            return false
        }
        
        // Filter strings starting with lowercase 'x' (Dash addresses start with 'X')
        if (!isSpecialFormat && trimmedValue.isNotEmpty() && trimmedValue[0] == 'x') {
            return false
        }
        
        // Filter strings starting with digits followed only by letters
        if (trimmedValue.isNotEmpty() && trimmedValue[0].isDigit() && trimmedValue.length >= 25) {
            var firstNonDigitIndex = 0
            while (firstNonDigitIndex < trimmedValue.length && trimmedValue[firstNonDigitIndex].isDigit()) {
                firstNonDigitIndex++
            }
            if (firstNonDigitIndex > 0 && firstNonDigitIndex < trimmedValue.length) {
                val restOfString = trimmedValue.substring(firstNonDigitIndex)
                val restOnlyLetters = restOfString.all { it.isLetter() }
                if (restOnlyLetters && restOfString.isNotEmpty()) {
                    return false
                }
            }
        }
        
        // Filter strings starting with 'L' or 'M' containing only letters (Litecoin Legacy has digits in Base58)
        if (trimmedValue.isNotEmpty() && (trimmedValue[0] == 'L' || trimmedValue[0] == 'M') && trimmedValue.length >= 25) {
            val hasAnyDigit = trimmedValue.any { it.isDigit() }
            val hasOnlyLetters = trimmedValue.all { it.isLetter() }
            if (hasOnlyLetters && !hasAnyDigit) {
                return false
            }
        }
        
        // Filter long strings (>41 chars) with only letters and punctuation
        if (trimmedValue.length > 41) {
            val hasAnyDigit = trimmedValue.any { it.isDigit() }
            val lettersAndPunctuation = trimmedValue.filter { it.isLetter() || it.isWhitespace() || it in "'\".,;:!?()[]{}" }
            val hasOnlyLettersAndPunctuation = lettersAndPunctuation.length == trimmedValue.length
            if (hasOnlyLettersAndPunctuation && !hasAnyDigit) {
                return false
            }
        }
        
        // For non-special formats with length 25-41, require at least one digit
        if (!isSpecialFormat && trimmedValue.length >= 25 && trimmedValue.length <= 41) {
            val hasAnyDigit = trimmedValue.any { it.isDigit() }
            if (!hasAnyDigit) {
                return false
            }
            
            // For Base58 addresses, require at least one digit 1-9
            val base58Alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
            val isBase58Format = trimmedValue.all { it in base58Alphabet }
            if (isBase58Format) {
                val hasDigit = trimmedValue.any { it in '1'..'9' }
                if (!hasDigit) {
                    return false
                }
            }
        }
        
        return true
    }

    override fun toString() = name
}

