package org.angryscan.common.constants

/**
 * A platform-agnostic container for storing and accessing card BIN (Bank Identification Number) patterns.
 * This is an expect/actual declaration to support different implementations across platforms.
 * 
 * The `cardBins` list contains patterns used to identify different card issuers based on the
 * first four digits of a payment card number.
 */
expect object CardBins {
    /**
     * A list of card BIN patterns used for card issuer identification.
     * Each string in the list represents a BIN pattern that can be matched against
     * the beginning of a card number to identify the card issuer.
     */
    val cardBins: List<String>
}
