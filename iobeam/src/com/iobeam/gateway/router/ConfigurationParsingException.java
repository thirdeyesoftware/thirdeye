package com.iobeam.gateway.router;

public class ConfigurationParsingException extends Exception  {
  public ConfigurationParsingException(String msg) {
    super(msg);
  }
  public String toString() {
    return "ConfigurationParsingException:" + super.getMessage();
  }
  
}