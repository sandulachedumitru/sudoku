package com.hardcodacii.utils.cmdLine.performActions;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

public class Debug implements CmdOptionPerformAction {
	private static final Debug INSTANCE = new Debug();

	private Debug() {
	}

	public static Debug getInstance() {
		return INSTANCE;
	}

	@Override
	public void performAction(String str) {

	}
}
