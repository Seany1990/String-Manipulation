# String-Manipulation
Simple GUI Application to demonstrate String Manipulation

Demonstrating some simple concepts:
-Building a Java.Swing GUI manually using code
-Using a north-to-south layout using nested panels with GridBagLayout
-Logical operation to take a string input, reverse it and display on screen
-Logical operation to take a string input and convert it to Pig Latin [1]
-Logical operation to take a string input and count the occurence of each vowel
-Ability to read in a .txt file (using BufferedStreamReader) and display it on screen (Dialog file Selector inc.)
-Ability to edit the text in this file and save it to a new file or overwrite (using BufferedStreamWriter)
-Possibility for syntax to be highlighted [2]

[1]Rules for pig latin: 
	if word begins with consonant and vowel: Move first letter to end and append "ay"
	if word begins with two consonants: Move both letters to end of the word and append "ay"
	if word begins with a vowel: append "-hay" to the end of the word
[2]The operations here are proof-of-Concept, syntax-highlighting from general IDE is not present

Still to be committed:
-Files saved with highlighted syntax need to have the highlighted text removed before file write.

Potential Improvements:
-Much more in-depth Syntax Highlighting using a cache of all terms (using an external source) with finer tuned matching
-Logical Operation to differentiate between vowel sounds and consonant sounds (eg: One, Europe)