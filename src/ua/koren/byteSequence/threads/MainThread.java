package ua.koren.byteSequence.threads;

import java.util.Scanner;

public class MainThread extends Thread {

    private Helper helper;
    private boolean running = true;
    private int currentLength;

    private String longRepSeq;

    public synchronized void setLongRepSeq(String longRepSeq) {
        this.longRepSeq = longRepSeq;
        notify();
    }

    public synchronized void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
        notify();
    }

    public String getFileName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file name:");
        String fileName = null;
        if(scanner.hasNextLine()) {
            fileName = scanner.nextLine();
        }
        if ("stop".equalsIgnoreCase(fileName)) {
            this.interrupt();
            running = false;
        }
        return fileName;
    }

    private void printCurrentResult() {
        if (longRepSeq == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Current length of sequence is: " + currentLength);
            synchronized (helper) {
                helper.notify();
            }
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        String fileName = getFileName();
        helper = new Helper(fileName, this);
        helper.setSearchInProcess(true);
        helper.setDaemon(true);
        helper.start();
        while(running) {
           if (helper.getState() == State.RUNNABLE) {
              printCurrentResult();
           } else {
               helper.setSearchInProcess(false);
               System.out.println("The longest repeating sequence is " + longRepSeq);
               longRepSeq = null;
               String newFileName = getFileName();
               helper.setFileName(newFileName);
           }

        }
    }
}
