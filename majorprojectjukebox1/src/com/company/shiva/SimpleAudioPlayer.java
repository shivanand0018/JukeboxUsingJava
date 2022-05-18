package com.company.shiva;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

    public class SimpleAudioPlayer
    {
        Long currentFrame;
        Clip clip;
        String status;

        AudioInputStream audioInputStream;
        static String filepath;

        public SimpleAudioPlayer()
                throws UnsupportedAudioFileException,
                IOException, LineUnavailableException
        {

            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        public static void play(String file)
        {
            try
            {
                filepath=file;
                SimpleAudioPlayer audioPlayer =
                        new SimpleAudioPlayer();

                audioPlayer.play();
                Scanner sc = new Scanner(System.in);

                while (true)
                {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    int c = sc.nextInt();
                    audioPlayer.gotoChoice(c);
                    if (c == 4)
                        break;
                }
            }

            catch (Exception ex)
            {
                System.out.println("Error with playing sound.");
                ex.printStackTrace();

            }
        }



        private void gotoChoice(int c)
                throws IOException, LineUnavailableException, UnsupportedAudioFileException
        {
            switch (c)
            {
                case 1:
                    pause();
                    break;
                case 2:
                    resumeAudio();
                    break;
                case 3:
                    restart();
                    break;
                case 4:
                    stop();
                    break;

            }

        }

        public void play()
        {
            clip.start();
            status = "play";
        }


        public void pause()
        {
            if (status.equals("paused"))
            {
                System.out.println("audio is already paused");
                return;
            }
            this.currentFrame = this.clip.getMicrosecondPosition();
            clip.stop();
            status = "paused";
        }


        public void resumeAudio() throws UnsupportedAudioFileException,
                IOException, LineUnavailableException
        {
            if (status.equals("play"))
            {
                System.out.println("Audio is already "+
                        "being played");
                return;
            }
            clip.setMicrosecondPosition(currentFrame);
            this.play();
        }

        public void restart() throws IOException, LineUnavailableException,
                UnsupportedAudioFileException
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = 0L;
            clip.setMicrosecondPosition(0);
            this.play();
        }

        public void stop() throws UnsupportedAudioFileException,
                IOException, LineUnavailableException
        {
            currentFrame = 0L;
            clip.stop();
            clip.close();
        }

        public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
                LineUnavailableException
        {
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File(filepath).getAbsoluteFile());
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }
