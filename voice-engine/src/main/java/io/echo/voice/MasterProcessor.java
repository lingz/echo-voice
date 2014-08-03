package io.echo.voice;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ling on 8/2/14.
 */
public class MasterProcessor {
    public static void main(String[] args) throws IOException {

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/models/acoustic/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/acoustic/wsj/dict/cmudict.0.6d");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/language/en-us.lm.dmp");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);

        InputStream stream = MasterProcessor.class.getResourceAsStream("/voice-test-samples/hello-world4.wav");

        recognizer.startRecognition(stream);

        SpeechResult result;
        System.out.println("STARTING recognition");

        while ((result = recognizer.getResult()) != null) {
            System.out.println(result.getHypothesis());

        }


        recognizer.stopRecognition();


    }
}
