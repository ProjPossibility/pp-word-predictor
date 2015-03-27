# pp-word-predictor
Software that allows users with limited mobility to be able to more accurately and quickly input text into a computer.

Right now this is an experimental project as we investigate supporting development of our projects

Overview
This goal of this software is to allow users with limited mobility to be able to more accurately and quickly input text into a computer. This software is intelligent enough to offer suggestions for the user based on context and the characters inputted so far.

For implementing the word level prediction, the application reads input text files, that can be all the documents in the users local machine, or all his emails. Any text that can help the application to initiate the word level contextual prediction.

The application maintains 3 sorted maps for storing as keys:

-A unigram map: This map stores individual words in the input text

-A bigram map: This map stores consecutive words in the input text

-A trigram map: This map stores 3 consecutive words in the input text.

Initially the user enters some input text (a word or two). As the user enters the letters of the words, the application presents him with some predictions. After the user has entered a word or two, the user can call the application to make predictions based on the usage context. The application crawls over the maps to present a set of predictions based on the bigrams and the trigrams that the user has provided as input text. The user can accept or reject the suggestions. In both the cases, the users input is reflected back to all the maps so that the context bases prediction becomes more refined.


Features
-A demonstration of a state of the art word prediction technology which can be applied to other applications in the future

Current Issues

-Slow startup/initialization time

Future Plans
-Code cleanup

-Integrate with non-OS specific "on-screen keyboards"

-Ability to import impure text formats such as .doc and .html

-GUI for importing files

-Offer word predictor as a XML-RPC service

References

Research

Intelligent Word Prediction Project

Disambiguation Research

Wordnet: A Lexical Database for the English Language

Constructing a Database for Word Prediction (PDF)

Software

Dasher (Commercial)

Dasher (Open Source)

ClickNType (Freeware)

Word Aid (Commercial)

LetMeType (Open Source)
