package ua.koren.byteSequence.threads;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Helper extends Thread {

    private String longestRepeatedSeq;
    private String content;
    private String fileName;
    private MainThread mainThread;
    private boolean searchInProcess;

    public Helper(String file, MainThread mainThread) {
        this.fileName = file;
        this.mainThread = mainThread;
    }

    public boolean isSearchInProcess() {
        return searchInProcess;
    }

    public void setSearchInProcess(boolean searchInProcess) {
        this.searchInProcess = searchInProcess;
    }

    public synchronized void setFileName(String fileName) {
        this.notify();
        this.searchInProcess = true;
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public String getLongestRepeatedSeq() {
        return longestRepeatedSeq;
    }

    public void setLongestRepeatedSeq(String longestRepeatedSeq) {
        this.longestRepeatedSeq = longestRepeatedSeq;
    }

    private static byte[] readByteFromFile(File name) throws IOException {
        return Files.readAllBytes(name.toPath());
    }

    private String largestCommonPrefix(String s, String t) {
        int n = Math.min(s.length(), t.length());
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != t.charAt(i)) {
                return s.substring(0, i);
            }
        }
        return s.substring(0, n);
    }

    private String longestRepeatingSequence(String string, MainThread mainThread) {
        String lrs = "";
        int n = string.length();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                String tmpPrefix = largestCommonPrefix(string.substring(i, n), string.substring(j, n));
                if (tmpPrefix.length() > lrs.length()) {
                    lrs = tmpPrefix;
                    mainThread.setCurrentLength(lrs.length());
                    System.out.println("Helper thread: lrs = " + lrs);
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return lrs;
    }

    @Override
    public void run() {
        while (true) {
            if (searchInProcess) {
                File file = new File(fileName);
                longestRepeatedSeq = "";
                try {
                    content = new String(readByteFromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                longestRepeatedSeq = longestRepeatingSequence(content, mainThread);
                mainThread.setLongRepSeq(longestRepeatedSeq);
            } else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
