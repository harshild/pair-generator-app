package sdc.vw.com.pairgenerator

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by asid on 28/09/18.
 */
class MainActivityTest() {
    private var mainActivity = MainActivity()

    @Test
    fun generatePair_itShouldGenerateRandomPairs_evenPeople() {

        var inputList = mutableListOf("A", "B", "C", "D")

        var actualList = mainActivity.generatePairs(inputList)


        assertEquals(2, actualList.size)
        val flatMap = actualList.flatMap { item -> listOf(item.first, item.second) }

        for (item in actualList)
            assertFalse(item.first == item.second)

        assertEquals(inputList.size, flatMap.size)
        assertTrue(inputList.containsAll(flatMap))
    }

    @Test
    fun generatePair_itShouldGenerateRandomPairs_oddPeople() {

        var inputList = mutableListOf("A", "B", "C", "D", "E")

        var actualList = mainActivity.generatePairs(inputList)


        assertEquals(3, actualList.size)
        val flatMap = actualList.flatMap { item -> listOf(item.first, item.second) }

        for (item in actualList)
            assertFalse(item.first == item.second)

        assertEquals(inputList.size + 1, flatMap.size)
        assertTrue(flatMap.containsAll(inputList))
        assertTrue(flatMap.contains("No Pair"))
    }

}