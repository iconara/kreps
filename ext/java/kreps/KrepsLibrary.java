package kreps;


import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.RubyClass;
import org.jruby.runtime.load.Library;


public class KrepsLibrary implements Library {
  public void load(Ruby runtime, boolean wrap) {
    runtime.getLoadService().require("logger");
    RubyModule krepsModule = runtime.defineModule("Kreps");
    krepsModule.defineAnnotatedMethods(Kreps.class);
    RubyClass loggerClass = krepsModule.defineClassUnder("Logger", runtime.getObject(), new Logger.LoggerAllocator());
    loggerClass.defineAnnotatedMethods(Logger.class);
  }
}
