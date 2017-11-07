package telesync.gui;

import java.util.EventListener;

public interface ErrorEventListener extends EventListener {
	public void errorEventNotify(ErrorEvent event);
}
