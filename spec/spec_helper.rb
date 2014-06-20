# encoding: utf-8

$SLF4J_BACKEND = nil

require 'kreps-slf4j'
require 'kreps'


module Slf4j
  include_package 'org.slf4j'

  def self.logger(name)
    LoggerFactory.get_logger(name)
  end
end