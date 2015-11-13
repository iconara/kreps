# encoding: utf-8

require 'bundler/gem_tasks'
require 'rake/javaextensiontask'


class Rake::Task
  def replace(options={}, &block)
    @actions.clear
    prerequisites.clear if options[:prerequisites]
    enhance(&block)
  end
end

Rake::JavaExtensionTask.new('kreps', eval(File.read('kreps.gemspec'))) do |ext|
  jars = $LOAD_PATH.flat_map { |path| Dir["#{path}/**/*.jar"] }
  ext.ext_dir = 'ext/java'
  ext.classpath = jars.map { |x| File.expand_path(x) }.join(':')
  ext.lib_dir = File.join('lib', 'kreps')
  ext.source_version = '1.6'
  ext.target_version = '1.6'
end

def filtered_jar(path, &filter)
  base_path = 'tmp/java/kreps'
  class_files = FileList["#{base_path}/**/*.class"].select(&filter)
  args = class_files.map do |path|
    "-C '#{base_path}' '#{path.sub("#{base_path}/", '')}'"
  end
  sh "jar cf #{path} #{args.join(' ')}"
end

Rake::Task['tmp/java/kreps/kreps.jar'].replace do
  filtered_jar('tmp/java/kreps/kreps.jar') { |path| !path.include?('org/slf4j') }
end

task 'tmp/java/kreps/kreps-slf4j.jar' => 'tmp/java/kreps/kreps.jar' do
  filtered_jar('tmp/java/kreps/kreps-slf4j.jar') { |path| path.include?('org/slf4j') }
end

task 'copy:kreps:java' => 'tmp/java/kreps/kreps-slf4j.jar' do
  install 'tmp/java/kreps/kreps-slf4j.jar', 'spec/kreps-slf4j.jar'
end

namespace :bundler do
  Bundler::GemHelper.install_tasks
end

desc 'Tag & release the gem'
task :release => [:spec, 'bundler:release']
