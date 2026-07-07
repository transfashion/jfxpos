package jfxpos.util;

import javax.sound.sampled.*;

public class SoundUtil {
    public static void playAlert() {
        new Thread(() -> {
            try {
                // Synthesize a sharp double-beep "teet-teet" sound
                // 8000 Hz, 8-bit, Mono, Signed
                int sampleRate = 8000;
                byte[] buf = new byte[4000]; // 500 ms total duration

                for (int i = 0; i < buf.length; i++) {
                    // Beep 1: 0 - 200 ms (0 - 1600 samples)
                    // Silence: 200 - 300 ms (1600 - 2400 samples)
                    // Beep 2: 300 - 500 ms (2400 - 4000 samples)
                    if ((i >= 0 && i < 1600) || (i >= 2400 && i < 4000)) {
                        double angle = i / (8000.0 / 600.0) * 2.0 * Math.PI;
                        // Use square wave (Math.signum) for a sharp, high-visibility warning sound
                        buf[i] = (byte) (Math.signum(Math.sin(angle)) * 30); // Moderate volume
                    } else {
                        buf[i] = 0;
                    }
                }

                AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();
                sdl.write(buf, 0, buf.length);
                sdl.drain();
                sdl.close();
            } catch (Exception e) {
                // Fallback to OS beep if audio device is unavailable
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }
}
