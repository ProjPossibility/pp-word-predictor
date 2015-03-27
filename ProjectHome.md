**_Right now this is an experimental project as we investigate supporting development of our projects_**

## Overview ##

This goal of this software is to allow users with limited mobility to be able to more accurately and quickly input text into a computer. This software is intelligent enough to offer suggestions for the user based on context and the characters inputted so far.

For implementing the word level prediction, the application reads input text files, that can be all the documents in the users local machine, or all his emails. Any text that can help the application to initiate the word level contextual prediction.

The application maintains 3 sorted maps for storing as keys:

  1. A unigram map: This map stores individual words in the input text
  1. A bigram map: This map stores consecutive words in the input text
  1. A trigram map: This map stores 3 consecutive words in the input text.

Initially the user enters some input text (a word or two). As the user enters the letters of the words, the application presents him with some predictions. After the user has entered a word or two, the user can call the application to make predictions based on the usage context. The application crawls over the maps to present a set of predictions based on the bigrams and the trigrams that the user has provided as input text. The user can accept or reject the suggestions. In both the cases, the users input is reflected back to all the maps so that the context bases prediction becomes more refined.

## Features ##

  * A demonstration of a state of the art word prediction technology which can be applied to other applications in the future

## Current Issues ##

  * Slow startup/initialization time

## Future Plans ##

  * Code cleanup
  * Integrate with non-OS specific "on-screen keyboards"
  * Ability to import impure text formats such as .doc and .html
  * GUI for importing files
  * Offer word predictor as a XML-RPC service

## References ##

> ### Research ###
    * [Intelligent Word Prediction Project](http://www.asel.udel.edu/natlang/nlp/wpredict.html)
    * [Disambiguation Research](http://www.assistech.org.uk/doku.php/research:disambiguation)
    * [Wordnet: A Lexical Database for the English Language](http://wordnet.princeton.edu/)
    * [Constructing a Database for Word Prediction (PDF)](http://www.speech.kth.se/prod/publications/files/qpsr/1996/1996_37_2_101-104.pdf)
> ### Software ###
    * [Dasher (Commercial)](http://www.ace-centre.org.uk/index.cfm?pageid=E79ED3AB-D613-62F1-CD947B4D353)
    * [Dasher (Open Source)](http://www.inference.phy.cam.ac.uk/dasher/)
    * [ClickNType (Freeware)](http://www.lakefolks.org/cnt/)
    * [Word Aid (Commercial)](http://www.ace-centre.org.uk/index.cfm?pageid=F73BD7C3-3048-7290-FE98DF7787334D21)
    * [LetMeType (Open Source)](http://www.clasohm.com/lmt/en/)