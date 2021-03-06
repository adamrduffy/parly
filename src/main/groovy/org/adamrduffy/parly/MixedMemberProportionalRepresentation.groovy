package org.adamrduffy.parly

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class MixedMemberProportionalRepresentation {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixedMemberProportionalRepresentation.class)

    static Election calculateSeats(List<Party> parties, int byelections, int totalSeats) {
        int nationalTotalVotes = parties.sum { it.votes } as int
        float nationalQuota = nationalTotalVotes / (totalSeats - byelections)
        LOGGER.debug "$nationalTotalVotes $nationalQuota"
        parties.sort(new PartyCodeComparator())
        parties.each { party ->
            party.partyQuota = party.votes / nationalQuota
            party.voteShare = (party.votes / nationalTotalVotes) * 100
            LOGGER.debug "$party.code Seats: $party.seats + $party.prSeatsRoundDown ($party.votes $party.partyQuota % $party.voteShare %)"
        }
        int seatsAllocated = parties.sum { it.getTotalSeats() } as int
        LOGGER.debug "Total Seats Allocated: $seatsAllocated"
        int remaining = totalSeats - seatsAllocated - byelections
        LOGGER.debug "Allocating Remaining $remaining on Highest Remainder"
        parties.sort(new PartyPrRemainderComparator())
        parties.take(remaining).each { party ->
            LOGGER.debug "$party.code PR Remainder $party.partyQuotaRemainder"
            party.remainderPrSeats += 1
        }
        parties.sort(new PartySeatComparator())
        parties.each { party ->
            LOGGER.debug "$party.code Seats: $party.totalSeats"
        }
        return new Election(parties: parties, nationalQuota: nationalQuota as float)
    }

    static Map<String, Party> determineParties(List<Constituency> constituencies) {
        Map<String, Party> parties = new HashMap<>()
        constituencies.each { constituency ->
            constituency.candidates.each { candidate ->
                Party party = parties.containsKey(candidate.party) ? parties.get(candidate.party) : new Party(code: candidate.party)
                party.candidates.add(candidate)
                parties.put(party.code, party)
            }
        }
        return parties
    }

    static void determineElectedCandidate(List<Constituency> constituencies) {
        constituencies.each { constituency ->
            def candidateWithMostVotes = constituency.candidates.max { it.votes }
            candidateWithMostVotes.elected = true
            candidateWithMostVotes.seated = !constituency.byelection
        }
    }

    static int countByElections(List<Constituency> constituencies) {
        return constituencies.flatten().sum { it.byelection ? 1 : 0 } as int
    }

    private static class PartyCodeComparator implements Comparator<Party> {
        @Override
        int compare(Party o1, Party o2) {
            return o1.code <=> o2.code
        }
    }

    private static class PartyPrRemainderComparator implements Comparator<Party> {
        @Override
        int compare(Party o1, Party o2) {
            return o2.partyQuotaRemainder <=> o1.partyQuotaRemainder
        }
    }

    private static class PartySeatComparator implements Comparator<Party> {
        @Override
        int compare(Party o1, Party o2) {
            return o2.totalSeats <=> o1.totalSeats
        }
    }
}
