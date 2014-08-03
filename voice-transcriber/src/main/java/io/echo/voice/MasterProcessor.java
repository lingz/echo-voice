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

    private static final int RECORDER_SAMPLERATE = 16000;
    private static final int RECORDER_BPP = 16;
    private static final int RECORDER_SAMPLERATE =


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


    private byte[] generateWaveFileHeader(long totalAudioLen) {

        long longSampleRate = RECORDER_SAMPLERATE;
        long totalDataLen = totalAudioLen + 44;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * RECORDER_CHANNELS_NUM / 8;

        byte[] header = new byte[44];

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        return header;

    }

}
