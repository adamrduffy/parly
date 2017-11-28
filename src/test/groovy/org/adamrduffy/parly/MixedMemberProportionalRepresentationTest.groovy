package org.adamrduffy.parly

import groovy.json.JsonSlurper
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class MixedMemberProportionalRepresentationTest {
    @Test
    void countByElections() {
        List<District> districts = loadDistricts()
        assertEquals 3, MixedMemberProportionalRepresentation.countByElections(districts.constituencies.flatten())
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
