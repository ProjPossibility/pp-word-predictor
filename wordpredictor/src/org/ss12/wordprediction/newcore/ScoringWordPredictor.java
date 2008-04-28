package org.ss12.wordprediction.newcore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.ss12.wordprediction.PredictionRequest;

/**
 * A {@link WordPredictor} implementation that
 * 
 * @author Michael Parker
 */
public class ScoringWordPredictor<T extends AnnotatedWord> implements
    WordPredictor {
  private final ImmutableLexicon defaultLexicon;
  private final CustomLexicon<T> customLexicon;
  private final AnnotationFactory<T> annotationFactory;

  private final HashMap<String, ImmutableLexicon> additionalLexicons;

  public ScoringWordPredictor(ImmutableLexicon defaultLexicon,
      CustomLexicon<T> customLexicon, AnnotationFactory<T> annotationFactory) {
    this.defaultLexicon = defaultLexicon;
    this.customLexicon = customLexicon;
    this.annotationFactory = annotationFactory;

    additionalLexicons = new HashMap<String, ImmutableLexicon>();
  }

  /**
   * Returns a new {@link WordReader} that allows new unigrams, bigrams, and
   * trigrams to be added to the lexicon.
   * 
   * @return a new word reader for adding to the lexicon
   */
  public WordReader getWordReader() {
    return new WordReader(customLexicon);
  }

  /**
   * @return the default, or baseline, lexicon that is set upon construction
   */
  public ImmutableLexicon getDefaultLexicon() {
    return defaultLexicon;
  }

  /**
   * Registers an additional immutable lexicon under the given name. Any lexicon
   * already registered under the given name is overwritten.
   * 
   * @param name the name of the lexicon
   * @param lexicon the immutable lexicon
   */
  public void addLexicon(String name, ImmutableLexicon lexicon) {
    additionalLexicons.put(name, lexicon);
  }

  /**
   * Returns the additional immutable lexicon registered under the given name.
   * 
   * @param name the name of the lexicon to return
   * @return the lexicon registered under the given name, or {@code null} if no
   *         such lexicon exists
   */
  public ImmutableLexicon getLexicon(String name) {
    return additionalLexicons.get(name);
  }

  /**
   * @return the set of additional immutable lexicons that have been registered
   */
  public Set<String> getLexiconNames() {
    if (additionalLexicons.isEmpty()) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(new TreeSet<String>(additionalLexicons
        .keySet()));
  }

  /**
   * Removes the additional immutable lexicon registered under the given name.
   * 
   * @param name the name of the lexicon to remove
   * @return the lexicon that is now removed, or {@code null} if no such lexicon
   *         exists
   */
  public ImmutableLexicon removeLexicon(String name) {
    return additionalLexicons.remove(name);
  }

  public List<String> getPredictions(PredictionRequest request) {
    Scorer<T> scorer = annotationFactory.newScorer();

    // Add the custom trigrams and bigrams, and then try to return immediately.
    if (request.getPrevPrevWord() != null) {
      scorer.addTrigrams(customLexicon.getTrigrams(request.getPrevPrevWord(),
          request.getPrevWord(), request.getIncompleteWord()));
    }
    if (request.getPrevWord() != null) {
      scorer.addBigrams(customLexicon.getBigrams(request.getPrevWord(),
          request.getIncompleteWord()));
      if (scorer.hasNumSuggestions(request.getNumPredictions())) {
        return scorer.getSuggestions();
      }
    }

    // Add the custom unigrams, and then try to return immediately.
    scorer.addUnigrams(customLexicon.getUnigrams(request.getIncompleteWord()));
    if (scorer.hasNumSuggestions(request.getNumPredictions())) {
      return scorer.getSuggestions();
    }

    // Add the additional lexicons, and then try to return immediately.
    String lexiconLowerBound = request.getIncompleteWord();
    String lexiconUpperBound = WordSequence.getUpperBound(lexiconLowerBound);
    if (!additionalLexicons.isEmpty()) {
      for (ImmutableLexicon lexicon : additionalLexicons.values()) {
        scorer.addAdditionalLexicon(lexicon.getSignificance(lexiconLowerBound,
            lexiconUpperBound));
      }
      if (scorer.hasNumSuggestions(request.getNumPredictions())) {
        return scorer.getSuggestions();
      }
    }

    // Add the default lexicon and then return.
    scorer.addDefaultLexicon(defaultLexicon.getSignificance(lexiconLowerBound,
        lexiconUpperBound));
    return scorer.getSuggestions();
  }
}
