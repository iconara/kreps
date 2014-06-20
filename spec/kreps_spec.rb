# encoding: utf-8

require 'spec_helper'


describe Kreps do
  describe '.logger' do
    it 'returns a logger with the specified name' do
      logger = described_class.logger('foo')
      expect(logger.name).to eq('foo')
    end
  end
end
