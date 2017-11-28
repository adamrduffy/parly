package org.adamrduffy.parly

import groovy.json.JsonSlurper
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class MixedMemberProportionalRepresentationTest {
    List<Constituency> constituencies

    @Before
    void setUp() {
        constituencies = loadDistricts().constituencies.flatten() as List<Constituency>
    }

    @Test
    void countByElections() {
        assertEquals 3, MixedMemberProportionalRepresentation.countByElections(constituencies)
    }

    @Test
    void determineElectedCandidate() {
        MixedMemberProportionalRepresentation.determineElectedCandidate(constituencies)
        for (constituency in constituencies) {
            assertEquals 1, constituency.candidates.findAll { candidate -> candidate.elected }.size()
        }
    }

    @Test
    void determineParties() {
        def parties = MixedMemberProportionalRepresentation.determineParties(constituencies)
        assertEquals 31, parties.size()
        assertTrue parties.containsKey("ABC")
        assertTrue parties.containsKey("AD")
        assertTrue parties.containsKey("DC")
        assertTrue parties.containsKey("LCD")
    }

    @Test
    void calculateSeats() {
        def parties = MixedMemberProportionalRepresentation.determineParties(constituencies)
        def byElections = MixedMemberProportionalRepresentation.countByElections(constituencies)
        def seats = MixedMemberProportionalRepresentation.calculateSeats(parties.values() as List<Party>, byElections, 120)
        assertNotNull seats
    }

    static List<District> loadDistricts() {
        def url = this.getClass().getResource("/votes.json")
        def file = new File(url.getFile())
        assertTrue file.exists()
        return loadDistricts(file)
    }

    static List<District> loadDistricts(File file) {
        return (List<District>) new JsonSlurper().parse(file)
    }
}
