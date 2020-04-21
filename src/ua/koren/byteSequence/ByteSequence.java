package ua.koren.byteSequence;

import ua.koren.byteSequence.threads.MainThread;

public class ByteSequence {

    public static void main(String[] args) {
        MainThread mainThread = new MainThread();
        mainThread.start();
    }
}
