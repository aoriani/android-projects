Style
==================

Show to apply styles dynamically. 
Android delegates the parsing of styles and themes to the View`s constructor ( the attributeset)
That prevents a simple way of changing dynamically  the theme for an Android app.
So the style has to be parsed manually by a central method.

StyleProvider is a APK that provides the styling resources
Stylable is the APK that has the logic to dynamically apply the styles