package nmo;

import java.util.Map;

public interface Action
{
	/** 
	 * Runs this action
	 * @param parameters A list of optional parameters
	 * 
	 * @throws Exception
	 */
	public void onAction(Map<String, String[]> parameters) throws Exception;

	public String getName();

	public String getDescription();

	public boolean isHiddenFromFrontend();

	public boolean isHiddenFromWebUI();

	public boolean isBlockedFromWebUI();
}
