package com.iobeam.gateway.router;


import java.util.*;
import java.io.*;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.*;


import com.iobeam.gateway.router.rule.*;
import com.iobeam.gateway.util.GatewayConfiguration;


/**
* Contains all configuration properties, interfaces, targets and rules
* for a router.
* 
*/

public class RouterConfiguration implements Configuration {

	protected String mName;
	protected Type mType;
	protected static Vector mInterfaces = new Vector();
	protected static Vector mTables = new Vector();
	protected static Vector mNameServers = new Vector();	
	protected static Address mHostAddress;
	protected static Address mLocalSubnet;

	
	static PrintWriter mOut;
	
	public Type getType() {
		return mType;
	}

	public String getName() {
		return mName;
	}

	public Collection getInterfaces() {
		return mInterfaces;
	}

	public Collection getNameServers() {
		return mNameServers;
	}
	
	public Address getHostAddress() {
		return mHostAddress;
	}

	public Address getLocalSubnet() {
		return mLocalSubnet;
	}
	public Collection getTables() {
		return mTables;
	}

	
	/**
	* @param fileName XML formatted configuration file containing 
	* all elements
	* for this configuration.
	*/
	public static RouterConfiguration parse(String fileName) 
			throws ConfigurationParsingException {

		RouterConfiguration config;
			
		try {
			DocumentBuilderFactory builderFactory = 
				DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(false);
			builderFactory.setCoalescing(false);
			builderFactory.setIgnoringComments(true);
			builderFactory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			OutputStreamWriter errorWriter =
					new OutputStreamWriter(System.err, "UTF-8");
			builder.setErrorHandler(
					new ParsingErrorHandler(new PrintWriter(errorWriter, true)));
			mOut = new PrintWriter(
				new OutputStreamWriter(System.out,"UTF-8"));
			System.out.println(fileName);
			
			Document document = builder.parse(
					RouterConfiguration.class.getResourceAsStream(fileName));

			Node root = document.getFirstChild();
			
			if (!root.getNodeName().trim().toLowerCase().equals("iobeam")) {
				throw new ConfigurationParsingException("Illegal Format");
			}
			NodeList children = root.getChildNodes();
			
			for (int z = 0; z < children.getLength(); z++) {
				Node child = children.item(z);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					
					if (!child.getNodeName().trim().toLowerCase().equals(
						"routerconfig")) {
						throw new ConfigurationParsingException("Illegal Format");
					}
					config = new RouterConfiguration();
			
					NodeList configList = child.getChildNodes();
					
					for (int i = 0; i < configList.getLength(); i ++) {
						
						Node node = configList.item(i);
						if (node.getNodeType() ==Node.ELEMENT_NODE) {
							String name = node.getNodeName().trim().toLowerCase();
				
							if (name.equals("tables")) {
								parseUserDefinedChains(config, node);
							}
							else if (name.equals("interfaces")) {
								parseInterfaces(config, node);
							}
							else if (name.equals("name_servers")) {
								parseNameServers(config, node);
							}
							else if (name.equals("host_address")) {
								parseHostAddress(config, node);
							}	
							
						}
					}
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ConfigurationParsingException(e.toString());
		}
		return new RouterConfiguration();
			
	}

	
	private static void parseUserDefinedChains(
			RouterConfiguration config, Node node) throws 
		ConfigurationParsingException {
		String name;
		
		try {
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i ++) {
				Node tableNode = list.item(i);
				if (tableNode.getNodeName().trim().equalsIgnoreCase("table")) {
					Table table = 
						Table.getInstanceFor(getValueFor(
							tableNode.getAttributes().getNamedItem(
									"name").getNodeValue()));

										
					NodeList tableNodes = tableNode.getChildNodes();
					for (int cc = 0; cc < tableNodes.getLength();cc++) {
						if (tableNodes.item(cc).getNodeName().toLowerCase().equals(
							"chains")) {
							NodeList chains = tableNodes.item(cc).getChildNodes();
							for (int c = 0; c < chains.getLength(); c++) {
								Node n = chains.item(c);
								if (n.getNodeName().trim().equalsIgnoreCase("chain")) {
									name = getValueFor(n.getAttributes().getNamedItem(
										"name").getNodeValue());
									Chain chain;
									if (table.containsChain(name)) {
										chain = table.getChain(name);
									} else {
										chain = new Chain(name,false);
									}
									table.addChain(chain);
								}
							}
							
						} else if 
								(tableNodes.item(cc).getNodeName().toLowerCase().equals(
										"rules")) {
							NodeList rules = tableNodes.item(cc).getChildNodes();
							for (int c = 0; c < rules.getLength(); c++) {
								Node nn = rules.item(c);
								if (nn.getNodeName().trim().equalsIgnoreCase("rule")) {
									name = getValueFor(nn.getAttributes().getNamedItem(
										"chain").getNodeValue());
									Rule rule;
									table.getChain(name).addRule(rule = parseRule(table.getName(),nn));
//									System.out.println(rule);
								}
							}
						}
					}
					mTables.addElement(table);
				}
				
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ConfigurationParsingException(e.getMessage());
		}
	}



	public static void parseInterfaces(RouterConfiguration config, 
				Node root) throws ConfigurationParsingException {

		Vector tables = new Vector();
		
		try {
			NodeList list = root.getChildNodes();
			for (int i = 0; i < list.getLength(); i ++) {
				Node iNode = list.item(i);
				Interface iface;
				String type = null;
				String name = null;
				String address = null;
				String subnet = null;
				
				if (iNode.getNodeName().trim().equalsIgnoreCase("interface")) {
					type = getValueFor(
									iNode.getAttributes().getNamedItem("type").getNodeValue());
					NodeList nl = iNode.getChildNodes();
					for (int c = 0;c < nl.getLength();c++) {
						if (nl.item(c).getNodeType() == Node.ELEMENT_NODE) {
							if (nl.item(c).getNodeName().trim()
									.equalsIgnoreCase("name")) {
								name = getValueFor(nl.item(c).getFirstChild().getNodeValue());
							}
							else if (nl.item(c).getNodeName().trim().equalsIgnoreCase(
									"address")) {
								address = getValueFor(nl.item(c).getFirstChild().getNodeValue());
							}
							else if (nl.item(c).getNodeName().trim().equalsIgnoreCase(
									"subnet")) {
								subnet = getValueFor(nl.item(c).getFirstChild().getNodeValue());
							}
						}
					}
					
					iface = new Interface(name,type.equals(
						"internal") ? 1 : 0,address,subnet);
				}
			}
		}
		catch (Exception e) {
			throw new ConfigurationParsingException(e.getMessage());
		}
	}



	public static void parseNameServers(
		RouterConfiguration config, Node n) 
			throws ConfigurationParsingException {
		Vector servers = new Vector();
		
		try {
			NodeList list = n.getChildNodes();
			for (int i = 0; i < list.getLength(); i ++) {
				Node serverNode = list.item(i);
				if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
					Address address = new Address(
							getValueFor(serverNode.getFirstChild().getNodeValue()),false);
					servers.addElement(address);
				}
			}
		}
		catch (Exception ee) {
			throw new ConfigurationParsingException(ee.getMessage());
		}
		config.mNameServers = servers;		
				
	}



	public static void parseHostAddress(
		RouterConfiguration config, Node n)
			throws ConfigurationParsingException {

		Address address;
		try {
			if (n.getFirstChild().getNodeType() == Node.TEXT_NODE) {
				address = new Address(
					getValueFor(n.getFirstChild().getNodeValue()),false);
				config.mHostAddress = address;
			}
		}
		catch (Exception ee) {
			throw new ConfigurationParsingException(ee.getMessage());
		}

	}



	public static Rule parseRule(String table, Node root) throws
		ConfigurationParsingException {
//		System.out.println("parsing rule for " + table);
		NodeList list = root.getChildNodes();
		String chain = null;
		Match match = null;
		Target target = null;
		String externalInterface = null;
		String internalInterface= null;
		int dPort = 0;
		int sPort = 0;
		String protocol = null;
		Address source = null;
		Address destination = null;
		String name = null;
		String value = null;
		String toAddress = null;
		NamedNodeMap attrs = null;		
		boolean except = false;
		try {
			chain = getValueFor(
				root.getAttributes().getNamedItem("chain").getNodeValue());
			
			for (int z = 0; z < list.getLength(); z++) {

				if (list.item(z).getNodeType() == Node.ELEMENT_NODE) {

					if (list.item(z).getNodeName().equalsIgnoreCase("target")) {
						attrs = list.item(z).getAttributes();
						name = getValueFor(attrs.getNamedItem("name").getNodeValue());
						
						if (name.equalsIgnoreCase("mark")) {
							if (attrs.getNamedItem("value") != null) {
								value = getValueFor(attrs.getNamedItem("value").getNodeValue());
							}
							target = (Target)new Mark((new Integer(value)).intValue());
						}
						else if (name.equalsIgnoreCase("redirect")) {
							
							String redirectPortString = getValueFor(attrs.getNamedItem("port").getNodeValue());
							int redirectPort = (new Integer(redirectPortString)).intValue();

							if (attrs.getNamedItem("address") != null) {
								System.out.println("redirect address = " + 
									getValueFor(attrs.getNamedItem("address").getNodeValue()));

								target = (Target)new
									RedirectTarget(getValueFor(getValueFor(attrs.getNamedItem("address").getNodeValue())),
										redirectPort);

							}
							else {
								target = (Target)new RedirectTarget(redirectPort);
							}

						} else {
								target = Target.getDefaultInstanceFor(name);
								if (target == null) target = new Target(name,false);
						}
					}

					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("source")) {
						value = getValueFor(list.item(z).getFirstChild().getNodeValue());
						
						attrs = list.item(z).getAttributes();
						if (attrs.getNamedItem("except") != null) {
							except = Boolean.valueOf(getValueFor(
							attrs.getNamedItem("except").getNodeValue()).toLowerCase()).booleanValue();
						} else {
							except = false;
						}


						source = new Address(value,(value.indexOf("/") > 0), except);
					}
					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("destination")) {
						value = getValueFor(list.item(z).getFirstChild().getNodeValue());
						
						attrs = list.item(z).getAttributes();
						if (attrs.getNamedItem("except") != null) {
							except = Boolean.valueOf(
								getValueFor(attrs.getNamedItem("except").getNodeValue()).toLowerCase()).booleanValue();
						} else {
							except = false;
						}


						destination = new Address(value,(value.indexOf("/") > 0), except);
					}
					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("protocol")) {
						protocol = getValueFor(list.item(z).getFirstChild().getNodeValue());
					}
					else if 
						(list.item(z).getNodeName()
							.equalsIgnoreCase("destination_port")) {
						value = getValueFor(list.item(z).getFirstChild().getNodeValue());
						dPort = (new Integer(value)).intValue();
					}
					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("source_port")) {
						value = getValueFor(list.item(z).getFirstChild().getNodeValue());
						sPort = (new Integer(value)).intValue();
					}					
					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("internal_interface")) {
						internalInterface = getValueFor(list.item(z)
							.getFirstChild().getNodeValue());
					}
					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("external_interface")) {
						externalInterface = getValueFor(list.item(z)
							.getFirstChild().getNodeValue());
					}

					else if (list.item(z).getNodeName()
							.equalsIgnoreCase("match")) {
						attrs = list.item(z).getAttributes();
						int dport = 0;
						int sport = 0;
							
						if (attrs.getNamedItem("type") != null) {
							name = getValueFor(attrs.getNamedItem("type").getNodeValue());
						}
						if (attrs.getNamedItem("value") != null) {
							value = getValueFor(attrs.getNamedItem("value").getNodeValue());
						}
						if (attrs.getNamedItem("dport") != null) {
							dport = (new Integer(getValueFor(
								attrs.getNamedItem("dport").getNodeValue()))).intValue();
						}
						if (attrs.getNamedItem("sport") != null) {
							sport = (new Integer(getValueFor(
								attrs.getNamedItem("sport").getNodeValue()))).intValue();
						}
						if (attrs.getNamedItem("protocol") != null) {
							protocol = getValueFor(attrs.getNamedItem("protocol").getNodeValue());
						}
							
						Mark mark = new Mark((new Integer(value)).intValue());
						
						match = new Match();
						if (protocol != null) {
							match.setProtocol(Protocol.getInstanceFor(protocol));
						}	
						match.setSourcePort(sport);
						match.setDestinationPort(dport);
						match.setMark(mark);
						
					}
						
				}

			} //end for
		}
		catch (Exception ee) {
			System.out.println("parseRule()");
			ee.printStackTrace();
			throw new ConfigurationParsingException(ee.getMessage());
		}
		return new Rule(source, destination, table, target, 
										chain, protocol, match, sPort, dPort, 
										internalInterface,
										externalInterface);
		
	}
	

	private static String fixNull(String s) {
		if (s == null) return "";
		else return s;
	}
	

	/**
	 * returns either a literal or system property pointed to by
	 * specified variable name.
	 */
	private static String getValueFor(String string) {
		String variable_indicator = "$";
		String variable_start_bracket = "{";
		String variable_end_bracket = "}";

		if (string == null || string.trim().equals("")) return string;

		if (string.startsWith(variable_indicator + variable_start_bracket)) {
			return GatewayConfiguration.getInstance().
					getProperty(string.substring(2, string.length()-1));
		}
		else {
			return string;
		}
	}
	
	/**
	* describes the router configuration
	*/
	public static class Type {
		private static Vector mInstances = new Vector();
		private static Hashtable mInstanceHash = new Hashtable();
		private String mName;
		private int mValue;
		
		public static final Type DEFAULT = new Type(0,"default");
		public static final Type CUSTOM = new Type(1, "custom");

		private Type(int type, String name) {
			mValue = type;
			mName = name;
			mInstances.addElement(this);
			mInstanceHash.put(mName,this);
		}

		
		public static Type getInstanceFor(String name) {
			if (name == null || name.trim().equals("")) {
				throw new IllegalArgumentException(
						"Key can not be null or empty.");
			}
			
			return (Type)mInstanceHash.get(name.trim().toLowerCase());
		}


		public static Collection getInstances() {
			return mInstances;
		}
		public String getName() {
			return mName;
		}
		public int getValue() {
			return mValue;
		}
		public String toString() {
			return "RouterConfiguration.Type:" +
				" id = " + mValue + 
				"\nName = " + mName;
		}
		public boolean equals(Object o) {
			if (o instanceof Type) {
				Type t = (Type)o;
				return t.getName().equals(getName());
			}
			return false;
		}
	}


	// Error handler to report errors and warnings
	//
	private static class ParsingErrorHandler implements ErrorHandler {

		/** Error handler output goes here */
		private PrintWriter out;

		ParsingErrorHandler(PrintWriter out) {
				this.out = out;
		}

		/**
		 * Returns a string describing parse exception details
		 */
		private String getParseExceptionInfo(SAXParseException spe) {
				String systemId = spe.getSystemId();
				if (systemId == null) {
						systemId = "null";
				}
				String info = "URI=" + systemId +
						" Line=" + spe.getLineNumber() +
						": " + spe.getMessage();
				return info;
		}

		// The following methods are standard SAX ErrorHandler methods.
		// See SAX documentation for more info.

		public void warning(SAXParseException spe) throws SAXException {
				mOut.println("Warning: " + getParseExceptionInfo(spe));
		}
				
		public void error(SAXParseException spe) throws SAXException {
				String message = "Error: " + getParseExceptionInfo(spe);
				throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
				String message = "Fatal Error: " + getParseExceptionInfo(spe);
				throw new SAXException(message);
		}
	}

}
