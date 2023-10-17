package com.kabos.coin.flip.presentation

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test


class CoinTest {
    @Test
    fun `check_isFront`() {
        assertTrue(isFront(0f))
        assertFalse(isFront(100f))
        assertTrue(isFront(270f))
        assertTrue(isFront(370f))
    }
}
