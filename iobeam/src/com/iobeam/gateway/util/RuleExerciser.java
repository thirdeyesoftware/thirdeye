package com.iobeam.gateway.util;


import com.iobeam.gateway.router.*;
import com.iobeam.gateway.router.rule.*;


import java.io.*;

import java.util.*;

public class RuleExerciser  {

	private RuleExerciser() {

	}

	/**
	 * @see RuleAction for actions
	 * 
	 */
	public static void applyRule(RuleAction action, Table table, 
				Chain chain, Rule rule) throws ExerciserException {
		try {
			StringBuffer sb = new StringBuffer("iptables ");

			sb.append(table.getCommandString());
			sb.append(" ");
			sb.append(action.getCommandString());
			sb.append(" " );
			sb.append(chain.getCommandString());
			sb.append(" ");
			sb.append(rule.getCommandString());

			System.out.println("RuleExerciser.applyRule: " + sb.toString());

			Process p = Runtime.getRuntime().exec(sb.toString());
			p.waitFor();

			if (p.exitValue() < 0) {
				throw new ExerciserException("Failed attempt: " + 
					sb.toString());
			}

		}
		catch (Exception ee) {
			ee.printStackTrace();
		}
	
	}

	/**
	 * @param Table table - target table
	 * causes table to flush all rules
	 */
	public static void flush(Table table) 
			throws ExerciserException {
		try {
			StringBuffer sb = new StringBuffer("iptables ");
			sb.append(table.getCommandString());
			sb.append(" " );
			sb.append(" --flush");
			Process p = Runtime.getRuntime().exec(sb.toString());
			p.waitFor();
			if (p.exitValue() < 0) {
				throw new ExerciserException("Failed flush attempt on table " + 
					table.getName());
			}

		}
		catch (Exception ee) {
			throw new ExerciserException(ee.toString());
		}
	}

		
	public static void applyConfiguration(RouterConfiguration config) 
			throws ExerciserException {
		String cmd = null;
		try {
				
			for (Iterator it = config.getTables().iterator();it.hasNext();) {
					Table table = (Table)it.next();
					for (Iterator chains = 
							table.getChains().iterator();chains.hasNext();) {
						Chain chain = (Chain)chains.next();

						cmd = "iptables " + table.getCommandString() + 	
							" " + RuleAction.ACTION_CREATE.getCommandString() + 
							" " + chain.getName();

						if (!chain.isDefault()) {
							System.out.println(
									"RuleExerciser.applyConfiguration: " + cmd);

							Process p = Runtime.getRuntime().exec(cmd);
							p.waitFor();
							if (p.exitValue() < 0) {
								throw new ExerciserException("Unexpected/Failed attempt: " + 
									cmd);
							}
						}
					}
					for (Iterator chains = 
								table.getChains().iterator();chains.hasNext();) {
						Chain chain = 
							(Chain)chains.next();
					
						for (Iterator rules = 
									chain.getRules().iterator();rules.hasNext();) {
							Rule rule = 
								(Rule)rules.next();
							String command = "iptables " + table.getCommandString() + 
								" " + RuleAction.ACTION_APPEND.getCommandString() + 
								" " + chain.getCommandString() + 
								" " + rule.getCommandString();

							System.out.println(
									"RuleExerciser.applyConfiguration: " +
									command);

							Process pp = Runtime.getRuntime().exec(command);
							pp.waitFor();

							if (pp.exitValue() < 0) {
								throw new ExerciserException("Failed Attempt: " + 
									cmd);
							}

						
						}
					}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
