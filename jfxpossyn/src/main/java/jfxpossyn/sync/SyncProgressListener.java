package jfxpossyn.sync;

@FunctionalInterface
public interface SyncProgressListener {
    /**
     * Called to report progress.
     *
     * @param progress A value between 0.0 and 1.0 representing progress.
     * @param message  A description of the current action.
     */
    void onProgress(double progress, String message);
}
