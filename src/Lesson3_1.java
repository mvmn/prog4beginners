import javax.sound.midi.*;

public class Lesson3_1 {
    public static void main(String[] args) throws Exception {

        int[] notes = {
                76, 75,
                76, 75, 76, 71, 74, 72,
                69, 69, 69, 60, 64, 69,
                71, 71, 71, 64, 68, 71,
                72, 72, 72, 69, 76, 75,
                76, 75, 76, 71, 74, 72,
                69, 69, 69, 60, 64, 69,
                71, 71, 71, 64, 72, 71,
                69, 69, 69};

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        MidiChannel[] channels = synth.getChannels();

        int channel = 0;
        channels[channel].noteOn(0, 0);
        Thread.sleep(500);
        for (int i = 0; i < notes.length; i++) {
            int note = notes[i];
            channels[channel].noteOn(note - 12, 127);
            channels[channel].noteOn(note + 12, 127);
            Thread.sleep(200);
            channels[channel].noteOff(note - 12);
            channels[channel].noteOff(note + 12);
        }
        Thread.sleep(500);
        synth.close();
    }
}
