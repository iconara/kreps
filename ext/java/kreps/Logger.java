package kreps;


import org.slf4j.LoggerFactory;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyString;
import org.jruby.RubyObject;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;


@JRubyClass(name="Kreps::Logger")
public class Logger extends RubyObject {
  private org.slf4j.Logger logger;
  private RubyString name;
  private RubyClass stdLibLoggerClass;
  private IRubyObject stdLibDebugLevel;
  private IRubyObject stdLibInfoLevel;
  private IRubyObject stdLibWarnLevel;
  private IRubyObject stdLibErrorLevel;

  public Logger(Ruby runtime, RubyClass type) {
    super(runtime, type);
  }

  static class LoggerAllocator implements ObjectAllocator {
    public IRubyObject allocate(Ruby runtime, RubyClass klass) {
      return new Logger(runtime, klass);
    }
  }

  @JRubyMethod(required = 1)
  public IRubyObject initialize(ThreadContext ctx, IRubyObject name) {
    this.name = name.asString();
    this.logger = LoggerFactory.getLogger(this.name.decodeString());
    this.stdLibLoggerClass = ctx.getRuntime().getClass("Logger");
    this.stdLibDebugLevel = stdLibLoggerClass.getConstant("DEBUG");
    this.stdLibInfoLevel = stdLibLoggerClass.getConstant("INFO");
    this.stdLibWarnLevel = stdLibLoggerClass.getConstant("WARN");
    this.stdLibErrorLevel = stdLibLoggerClass.getConstant("ERROR");
    return this;
  }

  @JRubyMethod
  public IRubyObject name(ThreadContext ctx) {
    return name;
  }

  @JRubyMethod
  public IRubyObject progname(ThreadContext ctx) {
    return ctx.getRuntime().getNil();
  }

  @JRubyMethod
  public IRubyObject close(ThreadContext ctx) {
    return ctx.getRuntime().getNil();
  }

  @JRubyMethod
  public IRubyObject level(ThreadContext ctx) {
    if (logger.isDebugEnabled()) {
      return stdLibDebugLevel;
    } else if (logger.isInfoEnabled()) {
      return stdLibInfoLevel;
    } else if (logger.isWarnEnabled()) {
      return stdLibWarnLevel;
    } else {
      return stdLibErrorLevel;
    }
  }

  @JRubyMethod(name = "debug?")
  public IRubyObject isDebug(ThreadContext ctx) {
    return logger.isDebugEnabled() ? ctx.getRuntime().getTrue() : ctx.getRuntime().getFalse();
  }

  @JRubyMethod(name = "info?")
  public IRubyObject isInfo(ThreadContext ctx) {
    return logger.isInfoEnabled() ? ctx.getRuntime().getTrue() : ctx.getRuntime().getFalse();
  }

  @JRubyMethod(name = "warn?")
  public IRubyObject isWarn(ThreadContext ctx) {
    return logger.isWarnEnabled() ? ctx.getRuntime().getTrue() : ctx.getRuntime().getFalse();
  }

  @JRubyMethod(name = "error?", alias = {"fatal?"})
  public IRubyObject isError(ThreadContext ctx) {
    return logger.isErrorEnabled() ? ctx.getRuntime().getTrue() : ctx.getRuntime().getFalse();
  }

  private enum Level { DEBUG, INFO, WARN, ERROR }

  private boolean isSufficientLevel(Level level) {
    switch (level) {
      case DEBUG: return logger.isDebugEnabled();
      case INFO: return logger.isInfoEnabled();
      case WARN: return logger.isWarnEnabled();
      case ERROR: return logger.isErrorEnabled();
    }
    return false;
  }

  private IRubyObject log(Level level, ThreadContext ctx, IRubyObject[] args, Block block) {
    IRubyObject nil = ctx.getRuntime().getNil();
    IRubyObject message = args.length == 1 ? args[0] : null;
    if (block.isGiven()) {
      if (isSufficientLevel(level)) {
        message = block.yield(ctx, nil);
      }
    }
    if (message != null) {
      if (!(message instanceof RubyString)) {
        message = message.inspect();
      }
      String messageString = message.asString().decodeString();
      switch (level) {
        case DEBUG: logger.debug(messageString); break;
        case INFO: logger.info(messageString); break;
        case WARN: logger.warn(messageString); break;
        case ERROR: logger.error(messageString); break;
      }
    }
    return nil;
  }

  @JRubyMethod(optional = 1)
  public IRubyObject debug(ThreadContext ctx, IRubyObject[] args, Block block) {
    return log(Level.DEBUG, ctx, args, block);
  }

  @JRubyMethod(optional = 1)
  public IRubyObject info(ThreadContext ctx, IRubyObject[] args, Block block) {
    return log(Level.INFO, ctx, args, block);
  }

  @JRubyMethod(optional = 1)
  public IRubyObject warn(ThreadContext ctx, IRubyObject[] args, Block block) {
    return log(Level.WARN, ctx, args, block);
  }

  @JRubyMethod(optional = 1, alias = {"fatal"})
  public IRubyObject error(ThreadContext ctx, IRubyObject[] args, Block block) {
    return log(Level.ERROR, ctx, args, block);
  }

  private Level currentLevel() {
    if (logger.isDebugEnabled()) {
      return Level.DEBUG;
    } else if (logger.isInfoEnabled()) {
      return Level.INFO;
    } else if (logger.isWarnEnabled()) {
      return Level.WARN;
    } else {
      return Level.ERROR;
    }
  }

  private Level stdLibLevelToLevel(IRubyObject stdLibLevel) {
    if (stdLibLevel == stdLibDebugLevel) {
      return Level.DEBUG;
    } else if (stdLibLevel == stdLibInfoLevel) {
      return Level.INFO;
    } else if (stdLibLevel == stdLibWarnLevel) {
      return Level.WARN;
    } else if (stdLibLevel == stdLibErrorLevel) {
      return Level.ERROR;
    } else {
      return currentLevel();
    }
  }

  @JRubyMethod(optional = 1)
  public IRubyObject unknown(ThreadContext ctx, IRubyObject[] args, Block block) {
    return log(currentLevel(), ctx, args, block);
  }

  @JRubyMethod(name = "<<", required = 1)
  public IRubyObject append(ThreadContext ctx, IRubyObject message) {
    return unknown(ctx, new IRubyObject[] {message}, Block.NULL_BLOCK);
  }

  @JRubyMethod(required = 1, optional = 2, alias = {"log"})
  public IRubyObject add(ThreadContext ctx, IRubyObject[] args, Block block) {
    Level level = stdLibLevelToLevel(args[0]);
    IRubyObject message = null;
    if (args.length > 1 && !args[1].isNil()) {
      message = args[1];
    } else if (block.isGiven() && isSufficientLevel(level)) {
      message = block.yield(ctx, ctx.getRuntime().getNil());
    }
    IRubyObject[] logArgs = message == null ? new IRubyObject[] {} : new IRubyObject[] {message};
    return log(level, ctx, logArgs, Block.NULL_BLOCK);
  }
}
