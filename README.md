# Kreps

Kreps makes it easy to use Log4j, Logback or any SLF4J compatible Java logger library through a Ruby standard library Logger interface.

## Requirements

JRuby 1.7.x.

## Installation

Kreps is available from RubyGems and can be installed by adding the following line to your application's `Gemfile`:

    gem 'kreps'

Or directly from RubyGems:

    $ gem install kreps

## Usage

```ruby
require 'kreps'

logger = Kreps.logger('MyLogger')
logger.info('hello world')
```

By default Kreps uses SLF4J's "simple" implementation, but you can change the SLF4J backend by using `require 'kreps/silent'`, `require 'kreps/jdk'` or `require 'kreps/custom'` instead of `require 'kreps'`.

The default logger backend, SLF4J's "simple" prints all messages at the info level and above to stderr, [but this can be configured](http://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html).

The "nop" backend, enabled with `require 'kreps/silent'`, simply turns off logging completely, which can be useful in some situations, for example when running tests.

The "jdk14" backend, enabled with `require 'kreps/jdk'`, uses Java's built in logging framework, see [`java.util.logging.Logger`](http://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html)).

```ruby
require 'kreps/jdk'

logger = Kreps.logger('MyLogger')
logger.info('hello world')
```

SLF4J ships with two more bridges, but they require additional libraries to be available and for that reason don't have any short cuts in Kreps. You can enable them by setting the `$KREPS_BACKEND` global before `require 'kreps'`, but make sure you also load their JARs. These bridges are "jcl" (for [Java Commons Logging](http://commons.apache.org/proper/commons-logging/)) and "log4j12" (for [Log4j 1.2](http://logging.apache.org/log4j/1.2/), probably the most widely used logging library in the Java world).

There are also logging libraries that come with their own SLF4J adapters, such as [Logback](http://logback.qos.ch/) and [Log4j 2](http://logging.apache.org/log4j/2.x/), both successors to Log4j 1.2 in some form or another.

To use these you can `require 'kreps/custom'` instead of just `require 'kreps'`, or you can set `$KREPS_BACKEND` to `nil` before `require 'kreps'`. Make sure you load the library's JARs.

```ruby
require 'kreps/custom'
require 'log4j-api-2.0-rc1.jar'
require 'log4j-core-2.0-rc1.jar'
require 'log4j-slf4j-impl-2.0-rc1.jar'

logger = Kreps.logger('MyLogger')
logger.info('hello world')
```

## How to contribute

[See CONTRIBUTING.md](CONTRIBUTING.md)

## Copyright

© 2014 Theo Hultberg, see [LICENSE.txt](LICENSE.txt) (BSD 3-Clause).
