package org.adamrduffy.parly

import groovy.xml.MarkupBuilder

/**
 * Adapted from https://github.com/slashme/parliamentdiagram/blob/master/newarch.py
 */
class ParliamentArchDiagram {
    static final int[] TOTALS = [3, 15, 33, 61, 95, 138, 189, 247, 313, 388, 469, 559, 657, 762, 876, 997, 1126, 1263,
                                 1408, 1560, 1722, 1889, 2066, 2250, 2442, 2641, 2850, 3064, 3289, 3519, 3759, 4005,
                                 4261, 4522, 4794, 5071, 5358, 5652, 5953, 6263, 6581, 6906, 7239, 7581, 7929, 8287,
                                 8650, 9024, 9404, 9793, 10187, 10594, 11003, 11425, 11850, 12288, 12729, 13183, 13638,
                                 14109, 14580, 15066, 15553, 16055, 16557, 17075, 17592, 18126, 18660, 19208, 19758,
                                 20323, 20888, 21468, 22050, 22645, 23243, 23853, 24467, 25094, 25723, 26364, 27011,
                                 27667, 28329, 29001, 29679, 30367, 31061]

    static String generate(List<Parliamentarian> parliamentarians, int delegates, Map config = [:]) {
        String defaultPartyColour = config.get('defaultPartyColour', '#808080') as String
        int width = config.get('width', 360) as int
        int height = config.get('height', 185) as int
        String vacantSeatFillColour = config.get('vacantSeatFillColour', '#FFFFFF') as String
        String vacantSeatStrokeColour = config.get('vacantSeatStrokeColour', '#000000') as String
        int labelPositionX = config.get('labelPositionX', 175) as int
        int labelPositionY = config.get('labelPositionY', 175) as int
        String labelStyle = config.get('labelStyle', 'font-size:36px; font-weight:bold; text-align:center; text-anchor:middle; font-family:sans-serif') as String
        String label = config.get('label', "$delegates") as String

        List<String> parties = parliamentarians.collect { parliamentarian -> parliamentarian.party }.unique()

        int rows = 0
        for (; rows < TOTALS.length && delegates > TOTALS[rows]; rows++) {
            // do nothing
        }
        rows += 1

        double radius = 0.4 / rows
        List positionsList = generateSeatPositionsInDiagram(rows, delegates, radius)
        assert positionsList.size() == delegates : "# positions (" + positionsList.size() + ") != delegates (" + delegates + ")"

        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.svg(xmlns: 'http://www.w3.org/2000/svg', 'xmlns:svg': 'http://www.w3.org/2000/svg', version: '1.1', width: "$width", height: "$height") {
            g {
                text(x: "$labelPositionX", y: "$labelPositionY", style: "$labelStyle", "$label")
                int totalCounter = 0
                for (party in parties) {
                    String partyColour = parliamentarians.findResult { parliamentarian -> party.equalsIgnoreCase(parliamentarian.party) ? parliamentarian.partyColour : null }
                    String fillColour = partyColour == null ? "$defaultPartyColour" : partyColour
                    List<Parliamentarian> parliamentariansInParty = parliamentarians.findAll { parliamentarian -> party.equalsIgnoreCase(parliamentarian.party) }
                    List<String> parliamentarianNames = parliamentariansInParty.collect{ parliamentarian -> parliamentarian.name }
                    generatePartySeatGroup(xml, party, parliamentarianNames, totalCounter, parliamentariansInParty.size(), radius, positionsList, fillColour, fillColour)
                    totalCounter += parliamentariansInParty.size()
                }
                int vacantSeats = delegates - totalCounter
                generatePartySeatGroup(xml, null, null, totalCounter, vacantSeats, radius, positionsList, "$vacantSeatFillColour", "$vacantSeatStrokeColour")
            }
        }
        return writer.toString()
    }

    private static List generateSeatPositionsInDiagram(int rows, int delegates, double radius) {
        def positionList = []
        for (int i in 1..rows - 1) {
            int seatsInRow = delegates / TOTALS[rows - 1] * Math.PI / (2 * Math.asin(2.0 / (3.0 * rows + 4.0 * i - 2.0))) as int
            double R = (3.0 * rows + 4.0 * i - 2.0) / (4.0 * rows)
            generateSeatPositionsInRow(seatsInRow, positionList, R, radius)
        }

        // the last row with the left over seats
        int seatsInRow = delegates - positionList.size()
        def R = (7.0 * rows - 2.0) / (4.0 * rows)
        generateSeatPositionsInRow(seatsInRow, positionList, R, radius)
        // sort by position so that party seats can be grouped together
        return positionList.sort { a, b -> a[0] <=> b[0] }.reverse()
    }

    private static void generateSeatPositionsInRow(int seatsInRow, List positionList, double R, double radius) {
        if (seatsInRow == 1) {
            positionList << ([Math.PI / 2.0, 1.75 * R, R])
        } else {
            for (int j in 0..seatsInRow - 1) {
                double angle = j * (Math.PI - 2.0 * Math.sin(radius / R)) / (seatsInRow - 1.0) + Math.sin(radius / R)
                positionList << ([angle, R * Math.cos(angle) + 1.75, R * Math.sin(angle)])
            }
        }
    }

    private static void generatePartySeatGroup(MarkupBuilder xml, String party, List<String> parliamentarians,
                                         int offset, int seats = parliamentarians.size(), double radius,
                                         List positionList, String fillColour, String strokeColour) {
        xml.g(id: "$party", style: "fill:$fillColour; stroke:$strokeColour; stroke-width: 2;") {
            for (int counter = offset; counter < offset + seats; counter++) {
                def x = positionList[counter][1] * 100.0 + 5.0
                def y = 100.0 * (1.75 - (positionList[counter][2] as double)) + 5.0
                def r = radius * 100.0

                def candidate = parliamentarians == null || parliamentarians.empty ?
                        null :
                        parliamentarians[offset >= counter ? offset - counter : counter]
                def name = party == null ? "Vacant" : "PR - $party"
                def title = candidate == null ? name : "$candidate - $party"
                generatePartySeat(xml, title, x, y, r)
            }
        }
    }

    private static void generatePartySeat(def xml, String name, def x, def y, def r) {
        xml.circle(cx: x, cy: y, r: r) {
            title("$name")
        }
    }
}
