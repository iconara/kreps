# coding: utf-8

$: << File.expand_path('../lib', __FILE__)

require 'kreps/version'


Gem::Specification.new do |spec|
  spec.name          = 'kreps'
  spec.version       = Kreps::VERSION
  spec.authors       = ['Theo']
  spec.email         = ['theo@iconara.net']
  spec.summary       = %q{An SLF4J backed drop in replacement for Ruby's standard library Logger}
  spec.description   = %q{An adapter that makes it easy to use Log4j, Logback or any SLF4J compatible Java logger library through a Ruby standard library Logger interface}
  spec.homepage      = 'https://github.com/iconara/kreps'
  spec.license       = 'BSD-3-Clause'

  spec.files         = Dir['lib/**/*.rb', 'lib/**/*.jar']
  spec.require_paths = %w[lib]

  spec.add_dependency 'slf4j-jars', '~> 1.7'
end
