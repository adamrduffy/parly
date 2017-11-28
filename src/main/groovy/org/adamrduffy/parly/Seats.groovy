package org.adamrduffy.parly

import groovy.transform.Canonical

@Canonical
class Seats {
    List<Party> parties
    float nationalQuota
}
