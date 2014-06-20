package org.slf4j.impl;


import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import org.slf4j.helpers.MarkerIgnoringBase;


public class ListLogger extends MarkerIgnoringBase implements Iterable<String> {
  private final List<String> messages;
  private String level;

  public ListLogger(String name) {
    this.name = name;
    this.messages = new LinkedList<String>();
    reset();
  }

  public void reset() {
    level = "trace";
    messages.clear();
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public Iterator<String> iterator() {
    return messages.iterator();
  }

  public boolean isTraceEnabled() { return level.equals("trace"); }
  public boolean isDebugEnabled() { return level.equals("debug") || isTraceEnabled(); }
  public boolean isInfoEnabled() { return level.equals("info") || isDebugEnabled(); }
  public boolean isWarnEnabled() { return level.equals("warn") || isInfoEnabled(); }
  public boolean isErrorEnabled() { return level.equals("error") || isWarnEnabled(); }

  public void trace(String msg) { }
  public void trace(String format, Object arg) { }
  public void trace(String format, Object arg1, Object arg2) { }
  public void trace(String format, Object... arguments) { }
  public void trace(String msg, Throwable t) { }

  public void debug(String msg) { messages.add("DEBUG " + msg); }
  public void debug(String format, Object arg) { }
  public void debug(String format, Object arg1, Object arg2) { }
  public void debug(String format, Object... arguments) { }
  public void debug(String msg, Throwable t) { }

  public void info(String msg) { messages.add("INFO " + msg); }
  public void info(String format, Object arg) { }
  public void info(String format, Object arg1, Object arg2) { }
  public void info(String format, Object... arguments) { }
  public void info(String msg, Throwable t) { }

  public void warn(String msg) { messages.add("WARN " + msg); }
  public void warn(String format, Object arg) { }
  public void warn(String format, Object... arguments) { }
  public void warn(String format, Object arg1, Object arg2) { }
  public void warn(String msg, Throwable t) { }

  public void error(String msg) { messages.add("ERROR " + msg); }
  public void error(String format, Object arg) { }
  public void error(String format, Object arg1, Object arg2) { }
  public void error(String format, Object... arguments) { }
  public void error(String msg, Throwable t) { }
}


