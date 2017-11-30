package org.adamrduffy.parly

import groovy.transform.Canonical

@Canonical
class Election {
    Date date
    float nationalQuota
    int seats
    List<Party> parties
}
