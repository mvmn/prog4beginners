import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class Lesson3_2 {
    public static void main(String[] args) throws Exception {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        MidiChannel[] channels = synth.getChannels();

        int channel = 0; // 0 is a piano
        int velocity = 127;

        int[][] noteOns = {
                {76, 72, 60},   // E5, C5, C4
                {74, 69},   // D5, A4
                {76, 72, 60},   // E5, C5, C4
                {74, 69},   // D5, A4
                {76, 71, 67},   // E5, B4, G4
                {74, 69},   // D5, A4
                {76, 72, 60},   // E5, C5, C4
                {74, 69},   // D5, A4
                {69, 64, 52},   // A4, E4, E3
                {},   // rest
                {69, 67, 55},   // A4, G4, G3
                {71, 69, 57},   // B4, A4, A3
                {74, 72, 60},   // D5, C5, C4
                {72, 69},   // C5, A4
                {76, 72, 60},   // E5, C5, C4
                {74, 69}    // D5, A4
        };

        int[][] noteOffs = {
                {},
                {76},
                {72, 60},
                {76},
                {71, 67},
                {74},
                {72, 60},
                {74},
                {64, 52},
                {},
                {67, 55},
                {69, 69, 57},
                {72, 60},
                {74, 69},
                {72, 60},
                {74, 69}
        };

        channels[channel].noteOn(0, 0);
        Thread.sleep(500);
        for (int i = 0; i < noteOns.length; i++) {
            int[] notes = noteOns[i];
            for (int n = 0; n < notes.length; n++) {
                channels[channel].noteOn(notes[n], velocity);
            }
            Thread.sleep(200);
            int[] offNotes = noteOffs[i];
            for (int n = 0; n < offNotes.length; n++) {
                channels[channel].noteOff(offNotes[n]);
            }
        }

        synth.close();
    }
}
