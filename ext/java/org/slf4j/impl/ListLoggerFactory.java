package org.slf4j.impl;


import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.ILoggerFactory;


public class ListLoggerFactory implements ILoggerFactory {
  private final Map<String, Logger> loggers;

  public ListLoggerFactory() {
    this.loggers = new HashMap<String, Logger>();
  }

  public Logger getLogger(String name) {
    if (!loggers.containsKey(name)) {
      loggers.put(name, new ListLogger(name));
    }
    return loggers.get(name);
  }
}
