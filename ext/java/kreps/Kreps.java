package kreps;


import org.jruby.Ruby;
import org.jruby.anno.JRubyModule;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.ThreadContext;


@JRubyModule(name = "Kreps")
public class Kreps {
  @JRubyMethod(module = true, required = 1)
  public static IRubyObject logger(ThreadContext ctx, IRubyObject recv, IRubyObject name) {
    return new Logger(ctx.getRuntime(), ctx.getRuntime().getModule("Kreps").getClass("Logger")).initialize(ctx, name);
  }
}
