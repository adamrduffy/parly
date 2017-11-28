package org.adamrduffy.parly

import groovy.transform.Canonical

@Canonical
class Constituency {
    String code
    String name
    List<Candidate> candidates
    boolean byelection
}
