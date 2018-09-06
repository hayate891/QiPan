package io.nibby.qipan;

public class LeelazHandler implements Runnable {

    private QiPan application;
    private Process leelaz;
    private Thread leelaThread;

    private volatile boolean running = false;

    public LeelazHandler(QiPan application) {
        this.application = application;
    }

    public void start() {
        running = true;
        leelaThread = new Thread(this, "leelaz");
        leelaThread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {

    }
}
