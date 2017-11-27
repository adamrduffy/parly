# parly
Parliament Arch Diagram

Adapted from https://github.com/slashme/parliamentdiagram/blob/master/newarch.py

A single method is used to return a string containing svg that may be embedded to a web page

```
List<Parliamentarian> parliamentarians = new LinkedList<>()
parliamentarians.add(new Parliamentarian("Alice", "ABC", "#CCCC00"))
parliamentarians.add(new Parliamentarian("Bob", "ABC", "#CCCC00"))
parliamentarians.add(new Parliamentarian(null, "ABC", "#CCCC00"))
parliamentarians.add(new Parliamentarian(null, "ABC", "#CCCC00"))
parliamentarians.add(new Parliamentarian("Carol", "DEF", "#009900"))
parliamentarians.add(new Parliamentarian("Chuck", "DEF", "#009900"))
parliamentarians.add(new Parliamentarian("Dan", "DEF", "#009900"))
parliamentarians.add(new Parliamentarian(null, "DEF", "#009900"))
parliamentarians.add(new Parliamentarian("Frank", "GHI", "#66FF66"))
String svg = ParliamentArchDiagram.generate(parliamentarians, 16)
```

Options may be included in a map as the third parameter to the function
* defaultPartyColour - rgb value for parties without a party colour, typically used for independents and smaller parties, defaults to #808080
* width - width in px of svg, defaults to 360
* height - height in px of svg, defaults to 185
* vacantSeatFillColour - rgb value for the fill colour for vacant seats, e.g. seats awaiting by-elections, defaults to #FFFFFF
* vacantSeatStrokeColour - rgb value for the stroke (circle edge) colour for vacant seats, defaults to #000000
* labelPositionX - label position, defaults to 175
* labelPositionY - label position, defaults to 175
* labelStyle - css styling for label text, defaults to font-size:36px; font-weight:bold; text-align:center; text-anchor:middle; font-family:sans-serif
* label - label to be displayed within the arch diagram, defaults to delegates parameter value, e.g. second parameter to method
