package com.hardcodacii.utils.cmdLine.performActions.option;

import com.hardcodacii.utils.cmdLine.performActions.CmdOptionPerformAction;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

public class PrintTo implements CmdOptionPerformAction {
	private static final PrintTo INSTANCE = new PrintTo();

	private PrintTo() {
	}

	public static PrintTo getInstance() {
		return INSTANCE;
	}

	@Override
	public void performAction(String str) {
		System.out.println("THis is the usage for --printTo=" + str);
	}
}
