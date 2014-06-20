# encoding: utf-8

require 'spec_helper'


module Kreps
  describe Logger do
    let :logger do
      described_class.new('foo')
    end

    let :raw_logger do
      Slf4j.logger('foo')
    end

    def logged_messages
      raw_logger.to_a
    end

    before do
      raw_logger.reset
    end

    describe '#name' do
      it 'has a name' do
        expect(logger.name).to eq('foo')
      end
    end

    describe '#level/#sev_threshold' do
      it 'returns the current log level threshold' do
        raw_logger.level = 'debug'
        expect(logger.level).to eq(::Logger::DEBUG)
        raw_logger.level = 'info'
        expect(logger.level).to eq(::Logger::INFO)
        raw_logger.level = 'warn'
        expect(logger.level).to eq(::Logger::WARN)
        raw_logger.level = 'error'
        expect(logger.level).to eq(::Logger::ERROR)
      end

      it 'returns debug when the log level threshold is trace' do
        raw_logger.level = 'trace'
        expect(logger.level).to eq(::Logger::DEBUG)
      end
    end

    describe '#progname' do
      it 'returns nil' do
        expect(logger.progname).to be_nil
      end
    end

    context 'when using unsupported Logger methods' do
      it 'does nothing when #close is called' do
        expect { logger.close }.to_not raise_error
      end

      it 'raises an error when #datetime_format is called' do
        expect { logger.datetime_format }.to raise_error(NoMethodError)
      end

      it 'raises an error when #datetime_format= is called' do
        expect { logger.datetime_format = nil }.to raise_error(NoMethodError)
      end

      it 'raises an error when #formatter is called' do
        expect { logger.formatter }.to raise_error(NoMethodError)
      end
    end

    context 'when logging' do
      %w[debug info warn error].each do |level|
        context "at the #{level} level" do
          it 'logs a message' do
            logger.send(level, 'hello world')
            expect(logged_messages).to include("#{level.upcase} hello world")
          end

          it 'knows whether or not it will log at this level' do
            raw_logger.level = level
            expect(logger.send("#{level}?")).to be_truthy
            raw_logger.level = "none"
            expect(logger.send("#{level}?")).to be_falsy
          end

          it 'uses the result of calling the given block as message' do
            logger.send(level) { 'hello world' }
            expect(logged_messages).to include("#{level.upcase} hello world")
          end

          it 'ignores the progname parameter when a block is given' do
            logger.send(level, 'foo bar') { 'hello world' }
            expect(logged_messages).not_to include("#{level.upcase} foo bar")
          end

          it 'does not evaluate the block when the message would not be logged' do
            called = false
            raw_logger.level = "none"
            logger.send(level) { called = true; 'hello world' }
            expect(called).to be_falsy
          end
        end
      end

      it 'outputs the result of #inspect when the message is not a string' do
        logger.info(:foo)
        logger.info(nil)
        expect(logged_messages).to include('INFO :foo')
        expect(logged_messages).to include('INFO nil')
      end

      it 'does not log anything when there is no message' do
        logger.info
        expect(logged_messages).to be_empty
      end

      context 'at the fatal level (which SLF4J does not support)' do
        it 'logs the message as an error' do
          logger.fatal('hello world')
          expect(logged_messages).to include('ERROR hello world')
        end

        it 'says it logs at the fatal level when it is configured for the error level' do
          raw_logger.level = 'error'
          expect(logger.fatal?).to be_truthy
          raw_logger.level = 'none'
          expect(logger.fatal?).to be_falsy
        end
      end

      context 'at the unknown level' do
        it 'logs a message at the minimum level that will get logged' do
          %w[debug info warn error].each do |level|
            raw_logger.reset
            raw_logger.level = level
            logger.unknown('foo')
            logger.unknown { 'bar' }
            expect(logged_messages).to include("#{level.upcase} foo")
            expect(logged_messages).to include("#{level.upcase} bar")
          end
        end
      end

      describe '#<<' do
        it 'works like #unknown (and not like Logger#<<, since that behaviour is not possible)' do
          raw_logger.level = 'info'
          logger << 'foo'
          expect(logged_messages).to include('INFO foo')
        end
      end

      describe '#add/#log' do
        it 'logs a message at the specified level' do
          logger.add(::Logger::INFO, 'hello world')
          logger.log(::Logger::WARN, 'foo bar')
          expect(logged_messages).to include('INFO hello world')
          expect(logged_messages).to include('WARN foo bar')
        end

        it 'ignores the progname parameter' do
          logger.add(::Logger::INFO, 'hello world', 'fibbliflubb')
          expect(logged_messages).to include('INFO hello world')
        end

        it 'uses the result of calling the given block as message' do
          logger.add(::Logger::INFO) { 'hello world' }
          expect(logged_messages).to include('INFO hello world')
        end

        it 'does not call the given block when there is a message parameter' do
          logger.add(::Logger::INFO, 'foo') { 'hello world' }
          expect(logged_messages).not_to include('INFO hello world')
          logger.add(::Logger::INFO, nil) { 'hello world' }
          expect(logged_messages).to include('INFO hello world')
        end

        it 'does not evaluate the block when the message would not be logged' do
          called = false
          raw_logger.level = "none"
          logger.add(::Logger::INFO) { called = true; 'hello world' }
          expect(called).to be_falsy
        end
      end
    end
  end
end
