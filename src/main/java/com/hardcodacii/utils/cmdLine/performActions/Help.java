package com.hardcodacii.utils.cmdLine.performActions;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

public class Help implements CmdOptionPerformAction {
	private static final Help INSTANCE = new Help();

	private Help() {
	}

	public static Help getInstance() {
		return INSTANCE;
	}

	@Override
	public void performAction(String str) {

	}
}
