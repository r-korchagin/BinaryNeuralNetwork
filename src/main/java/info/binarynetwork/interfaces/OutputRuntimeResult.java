package info.binarynetwork.interfaces;

/**
 * 
 * @author Roman.Korchagin Interface for output runtime data during learning
 */
public interface OutputRuntimeResult {

    /**
     * @param result
     *            - Array of check result each element
     * @param startTime
     *            - Time of start current cycle.
     * @param cycleCount
     *            - Total cycle count
     * @param countIteration
     *            - Count cycle then result the same.
     */
    void Printout(float[] result, long startTime, int cycleCount, int countIteration);

}
