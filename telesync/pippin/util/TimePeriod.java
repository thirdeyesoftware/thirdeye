////
// //
// PippinSoft
//
//

package pippin.util;



import java.util.*;
import java.io.Serializable;



/**
* Describes a relative time period to millisecond precision.
*/
public class TimePeriod extends Moment {

	/**
	* @param s
	* hh:mm:ss.ms of the absolute value of the duration of this TimePeriod.
	*/
	public TimePeriod(String s) throws NumberFormatException {
		super(stringToMillis(s));
	}


	/**
	* @param seconds
	* Seconds since the 1970 epoch.
	*/
	public TimePeriod(int seconds) {
		super(seconds);
	}


	/**
	* @param milliseconds
	* Milliseconds since the 1970 epoch.
	*/
	public TimePeriod(long milliseconds) {
		super(milliseconds);
	}


	public String toString() {
		return toDurationString();
	}
}
