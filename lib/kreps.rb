# encoding: utf-8

if defined?($KREPS_BACKEND)
  $SLF4J_BACKEND = $KREPS_BACKEND
end

unless defined?($SLF4J_BACKEND)
  $SLF4J_BACKEND = 'simple'
end

require 'jruby'
require 'slf4j-jars'
require 'kreps/kreps.jar'

Java::Kreps::KrepsLibrary.new.load(JRuby.runtime, false)