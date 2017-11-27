package org.adamrduffy.parly

import org.junit.Assert
import org.junit.Test

import static org.junit.Assert.assertNotNull

class ParliamentArchDiagramTest {
    @Test
    void generate() {
        List<Parliamentarian> parliamentarians = new LinkedList<>()
        parliamentarians.add(new Parliamentarian("Alice", "ABC", "#CCCC00"))
        parliamentarians.add(new Parliamentarian("Bob", "ABC", "#CCCC00"))
        parliamentarians.add(new Parliamentarian(null, "ABC", "#CCCC00"))
        parliamentarians.add(new Parliamentarian(null, "ABC", "#CCCC00"))
        parliamentarians.add(new Parliamentarian("Carol", "DEF", "#009900"))
        parliamentarians.add(new Parliamentarian("Chuck", "DEF", "#009900"))
        parliamentarians.add(new Parliamentarian("Dan", "DEF", "#009900"))
        parliamentarians.add(new Parliamentarian(null, "DEF", "#009900"))
        parliamentarians.add(new Parliamentarian("Frank", "GHI", "#66FF66"))
        assertNotNull ParliamentArchDiagram.generate(parliamentarians, 16)
    }
}
