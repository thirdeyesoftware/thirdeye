////
//
// Telesync 5320 Project
//
//

package pippin.binder;


public interface ErrorRateCalculator {

	public double calculateErrorRate(int numOfTribs, long time, long errorCount);

}
