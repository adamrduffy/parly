package org.adamrduffy.parly

import groovy.transform.Canonical

@Canonical
class Seats {
    Date date
    List<Party> parties
    float nationalQuota
}
