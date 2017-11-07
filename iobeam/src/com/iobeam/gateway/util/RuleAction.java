package com.iobeam.gateway.util;


public class RuleAction  {

	public static RuleAction ACTION_APPEND = new RuleAction("A");
	public static RuleAction ACTION_INSERT = new RuleAction("I");
	public static RuleAction ACTION_DELETE = new RuleAction("D");
	public static RuleAction ACTION_CREATE = new RuleAction("N");

	private String mCommand;

	private RuleAction(String command) {
		mCommand = command;
	}
	
	public String getCommandString() {
		return "-" + mCommand;
	}
}
