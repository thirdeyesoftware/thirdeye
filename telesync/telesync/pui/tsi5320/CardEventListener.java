package telesync.pui.tsi5320;



import pippin.pui.*;
import java.util.*;



public interface CardEventListener extends EventListener {

	public void cardEventNotify(CardEvent event);
}
