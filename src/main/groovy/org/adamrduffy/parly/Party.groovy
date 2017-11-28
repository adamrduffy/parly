package org.adamrduffy.parly

import groovy.transform.Canonical

@Canonical
class Party {
    String code
    int votes
    List<Candidate> candidates = new ArrayList<>()
    float voteShare
    float partyQuota
    int remainderPrSeats

    int getSeats() {
        return candidates.count { candidate -> candidate.elected }
    }

    float getPrSeats() {
        return Math.max(0, partyQuota - getSeats())
    }

    int getPrSeatsRoundDown() {
        return Math.floor(getPrSeats()) as int
    }

    float getPartyQuotaRemainder() {
        return partyQuota - Math.floor(partyQuota)
    }

    int getTotalSeats() {
        return getSeats() + getPrSeatsRoundDown() + remainderPrSeats
    }

    int getVotes() {
        return candidates.sum { it.votes } as int
    }
}
