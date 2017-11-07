/* Compare Interface */
/* RMI Remote Interface for CompareEngine */

package telesync.qov.rmi;

import java.rmi.*;
import java.rmi.RemoteException;
import java.io.*;
import telesync.qov.Results;


public interface Compare extends Remote {
	Results compareSamples(Long type, DataSample reference, DataSample degraded) throws RemoteException;
  Results compareSamples(DataSample reference, DataSample degraded) throws RemoteException;
  Results compareSamples(File reference, File degraded) throws RemoteException;

}
