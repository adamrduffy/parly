package org.adamrduffy.parly

import groovy.transform.Canonical

@Canonical
class Candidate {
    String code
    String name
    String party
    int votes
    float share
    boolean elected
    boolean seated
}
