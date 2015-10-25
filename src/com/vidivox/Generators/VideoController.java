package com.vidivox.Generators;

import com.vidivox.controller.CurrentDirectory;
import com.vidivox.view.WarningDialogue;
import org.omg.CORBA.Current;

import java.io.*;
import java.nio.file.Files;

/**
 * @author Matthew Canham
 * Commented by Jayden Cooke.
 * Handles the ffmpeg calls for VIDIVOX.
 */
public class VideoController {
    //The current video.
    File videoFile;

    /**
     * Sets the current video when the object is instantiated.
     *
     * @param videoFile - The video file.
     */
    public VideoController(File videoFile) {
        this.videoFile = videoFile;
    }

    /**
     * Merges the audio file specified to the current video.
     *
     * @param audioFile    - the audio file to merge.
     * @param oldVideoFile - the current video.
     */
    public File mergeAudio(File audioFile, File oldVideoFile) {
        try {
            File newVideoFile = new File(CurrentDirectory.getDirectory().getAbsolutePath()+System.getProperty("file.separator")+"output.mp4");
            //Calls ffmpeg for the actual merging, and creates the new video file.
            //FFMPEG from http://superuser.com/questions/712862/ffmpeg-add-background-audio-to-video-but-not-completely-muting-the-original-audi
            String command = "ffmpeg -i "+audioFile.getName()+" -i "+oldVideoFile.getName()+" -filter_complex \"[0:a][1:a]amerge,pan=stereo:c0<c0+c2:c1<c1+c3[out]\" -map 1:v -map \"[out]\" -c:v copy -c:a libfdk_aac -shortest output.mp4 > /dev/null 2>&1 < /dev/null";
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", command);
            pb.redirectErrorStream();
            pb.directory(CurrentDirectory.getDirectory());
            Process process = pb.start();
            process.waitFor();
            Files.delete(audioFile.toPath());
            return newVideoFile;
        } catch (IOException | InterruptedException e) {
            WarningDialogue.genericError(e.getMessage());
            return oldVideoFile;
        }
    }

    /**
     * Adds silence to the start of the audio, so that the sound the user has added appears at the point they specified.
     * @param timeToAdd - The amount of seconds before the audio starts
     * @param audioFile - The file to be extended
     */
    public File padAudio(int timeToAdd, File audioFile, int videoLength) {
        try {
            //Creates an object representing the file the ffmpeg calls will make.
            File padded = new File(CurrentDirectory.getDirectory().getAbsolutePath() + System.getProperty("file.separator") + "pad" + timeToAdd + audioFile.getName());
            //FFMPEG call from http://superuser.com/questions/579008/add-1-second-of-silence-to-audio-through-ffmpeg
            String commands0 = "ffmpeg -f lavfi -i aevalsrc=0:0:0:0:0:0::duration="+videoLength+" trailingSilence.mp3 > /dev/null 2>&1 < /dev/null";
            String commands1 = "ffmpeg -f lavfi -i aevalsrc=0:0:0:0:0:0::duration="+timeToAdd+" leadingSilence.mp3 > /dev/null 2>&1 < /dev/null";
            //Concept from http://stackoverflow.com/questions/7333232/concatenate-two-mp4-files-using-ffmpeg
            String commands2 = "touch list.txt";
            String commands3 = "echo \"file '"+CurrentDirectory.getDirectory().getAbsolutePath() + System.getProperty("file.separator")+"leadingSilence.mp3'\" >> list.txt";
            String commands4 = "echo \"file '"+audioFile.getAbsolutePath()+"'\" >> list.txt";
            String commands5 = "echo \"file '"+CurrentDirectory.getDirectory().getAbsolutePath() + System.getProperty("file.separator")+"trailingSilence.mp3'\" >> list.txt";
            String commands6 = "ffmpeg -f concat -i list.txt -c copy "+padded.getName() +" > /dev/null 2>&1 < /dev/null";

            ProcessBuilder pb0 = new ProcessBuilder("/bin/sh", "-c", commands0);
            pb0.directory(CurrentDirectory.getDirectory());
            Process process0 = pb0.start();
            process0.waitFor();

            ProcessBuilder pb1 = new ProcessBuilder("/bin/sh", "-c", commands1);
            pb1.directory(CurrentDirectory.getDirectory());
            Process process1 = pb1.start();
            process1.waitFor();

            ProcessBuilder pb2 = new ProcessBuilder("/bin/sh", "-c", commands2);
            pb2.directory(CurrentDirectory.getDirectory());
            Process process2 = pb2.start();
            process2.waitFor();

            ProcessBuilder pb3 = new ProcessBuilder("/bin/sh", "-c", commands3);
            pb3.directory(CurrentDirectory.getDirectory());
            Process process3 = pb3.start();
            process3.waitFor();

            ProcessBuilder pb4 = new ProcessBuilder("/bin/sh", "-c", commands4);
            pb4.directory(CurrentDirectory.getDirectory());
            Process process4 = pb4.start();
            process4.waitFor();

            ProcessBuilder pb5 = new ProcessBuilder("/bin/sh", "-c", commands5);
            pb5.directory(CurrentDirectory.getDirectory());
            Process process5 = pb5.start();
            process5.waitFor();

            ProcessBuilder pb6 = new ProcessBuilder("/bin/sh", "-c", commands6);
            pb6.directory(CurrentDirectory.getDirectory());
            Process process6 = pb6.start();
            process6.waitFor();

            //Delete temporary files made during the combining process.
            File leadingSilence = new File(CurrentDirectory.getDirectory()+System.getProperty("file.separator")+"leadingSilence.mp3");
            Files.delete(leadingSilence.toPath());
            File trailingSilence = new File(CurrentDirectory.getDirectory()+System.getProperty("file.separator")+"trailingSilence.mp3");
            Files.delete(trailingSilence.toPath());
            File list = new File(CurrentDirectory.getDirectory()+System.getProperty("file.separator")+"list.txt");
            Files.delete(list.toPath());

            return padded;

        } catch (IOException | InterruptedException e) {
            //Occurs when invalid files are passed in. There is no way for the user to actually do this through the GUI.
            WarningDialogue.genericError(e.getMessage());
            //Returning the original file unaltered will cause erroneous output, but won't terminate the program.
            return audioFile;
        }
    }
}
