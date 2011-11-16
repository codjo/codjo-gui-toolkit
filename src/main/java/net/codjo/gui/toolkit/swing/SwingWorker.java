package net.codjo.gui.toolkit.swing;
import javax.swing.SwingUtilities;

public abstract class SwingWorker {
    private Object value;
    private ThreadVar threadVar;


    protected SwingWorker() {
        final Runnable doFinished =
              new Runnable() {
                  public void run() {
                      finished();
                  }
              };

        Runnable doConstruct =
              new Runnable() {
                  public void run() {
                      try {
                          setValue(construct());
                      }
                      finally {
                          threadVar.clear();
                      }

                      SwingUtilities.invokeLater(doFinished);
                  }
              };

        Thread thread = new Thread(doConstruct);

        //thread.setPriority(Thread.MIN_PRIORITY);
        threadVar = new ThreadVar(thread);
    }


    public Object get() {
        while (true) {
            Thread thread = threadVar.get();
            if (thread == null) {
                return getValue();
            }
            try {
                thread.join();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                // propagate
                return null;
            }
        }
    }


    public abstract Object construct();


    public void finished() {
    }


    public void interrupt() {
        Thread thread = threadVar.get();
        if (thread != null) {
            thread.interrupt();
        }
        threadVar.clear();
    }


    public void start() {
        Thread thread = threadVar.get();
        if (thread != null) {
            thread.start();
        }
    }


    protected synchronized Object getValue() {
        return value;
    }


    private synchronized void setValue(Object x) {
        value = x;
    }


    /**
     * Class to maintain reference to current worker thread under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;


        ThreadVar(Thread thread) {
            this.thread = thread;
        }


        synchronized Thread get() {
            return thread;
        }


        synchronized void clear() {
            thread = null;
        }
    }
}
