package org.slf4j.impl;


import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;


public class StaticLoggerBinder implements LoggerFactoryBinder {
  private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
  public static final String REQUESTED_API_VERSION = "1.6.99";
  private static final String loggerFactoryClassStr = ListLoggerFactory.class.getName();

  private final ILoggerFactory loggerFactory;

  public static final StaticLoggerBinder getSingleton() {
    return SINGLETON;
  }

  private StaticLoggerBinder() {
    loggerFactory = new ListLoggerFactory();
  }

  public ILoggerFactory getLoggerFactory() {
    return loggerFactory;
  }

  public String getLoggerFactoryClassStr() {
    return loggerFactoryClassStr;
  }
}
