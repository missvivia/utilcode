
package com.xyl.mmall.oms.warehouse.util;

import java.math.BigDecimal;

public interface BasicTypeConverter<T> {
	T convert(String value);

	T defaultVal();

	Class<T> clazz();

	public abstract class AbstractFormatter<T> implements BasicTypeConverter<T> {
		public T convert(String value) {
			if (value == null) {
				return null;
			}
			value = value.trim();
			return doConvert(value);
		}

		public abstract T doConvert(String value);
	}

	public static abstract class NumberFormatter<T> extends AbstractFormatter<T> {

		@Override
		public T doConvert(String value) {
			Double number =Double.parseDouble(value);
			return convertNumber(number);
		}
		
		public abstract T convertNumber(Double number);

	}

	public static class IntegerFormatter extends NumberFormatter<Integer> {
		public static final IntegerFormatter INSTANCE = new IntegerFormatter();

		@Override
		public Integer convertNumber(Double number) {
			BigDecimal db = new BigDecimal(number).setScale(0,BigDecimal  .ROUND_HALF_UP);
			return db.intValue();
		}

		@Override
		public Class<Integer> clazz() {
			return Integer.class;
		}

		@Override
		public Integer defaultVal() {
			return 0;
		}
	}

	public static class LongFormatter extends NumberFormatter<Long> {
		public static final LongFormatter INSTANCE = new LongFormatter();

		@Override
		public Long convertNumber(Double number) {
			BigDecimal db = new BigDecimal(number).setScale(0,BigDecimal  .ROUND_HALF_UP);
			return db.longValue();
		}

		@Override
		public Class<Long> clazz() {
			return Long.class;
		}

		@Override
		public Long defaultVal() {
			return 0L;
		}
	}

	public static class DoubleFormatter extends NumberFormatter<Double> {
		public static final DoubleFormatter INSTANCE = new DoubleFormatter();

		@Override
		public Double convertNumber(Double number) {
			return number;
		}

		@Override
		public Class<Double> clazz() {
			return Double.class;
		}

		@Override
		public Double defaultVal() {
			return 0.0d;
		}
	}

	public static class FloatFormatter extends NumberFormatter<Float> {
		public static final FloatFormatter INSTANCE = new FloatFormatter();

		@Override
		public Float convertNumber(Double number) {
			return number.floatValue();
		}

		@Override
		public Class<Float> clazz() {
			return Float.class;
		}

		@Override
		public Float defaultVal() {
			return 0.0f;
		}
	}

	public static class ShortFormatter extends NumberFormatter<Short> {
		public static final ShortFormatter INSTANCE = new ShortFormatter();

		@Override
		public Short convertNumber(Double number) {
			BigDecimal db = new BigDecimal(number).setScale(0,BigDecimal  .ROUND_HALF_UP);
			return db.shortValue();
		}

		@Override
		public Class<Short> clazz() {
			return Short.class;
		}

		@Override
		public Short defaultVal() {
			return 0;
		}
	}

	public static class CharactorFormatter extends AbstractFormatter<Character> {
		public static final CharactorFormatter INSTANCE = new CharactorFormatter();

		@Override
		public Character doConvert(String raw) {
			return raw.charAt(0);
		}

		@Override
		public Class<Character> clazz() {
			return Character.class;
		}

		@Override
		public Character defaultVal() {
			return '\u0000';
		}
	}

	public static class ByteFormatter extends AbstractFormatter<Byte> {
		public static final ByteFormatter INSTANCE = new ByteFormatter();

		@Override
		public Byte doConvert(String raw) {
			return Byte.parseByte(raw, 10);
		}

		@Override
		public Class<Byte> clazz() {
			return Byte.class;
		}

		@Override
		public Byte defaultVal() {
			return 0;
		}
	}

	public static class BooleanFormatter extends AbstractFormatter<Boolean> {
		public static final BooleanFormatter INSTANCE = new BooleanFormatter();

		@Override
		public Boolean doConvert(String raw) {
			return Boolean.parseBoolean(raw);
		}

		@Override
		public Class<Boolean> clazz() {
			return Boolean.class;
		}

		@Override
		public Boolean defaultVal() {
			return false;
		}
	}

	public static class StringFormatter extends AbstractFormatter<String> {
		public static final StringFormatter INSTANCE = new StringFormatter();

		@Override
		public String doConvert(String raw) {
			return raw;
		}

		@Override
		public Class<String> clazz() {
			return String.class;
		}

		@Override
		public String defaultVal() {
			return "";
		}
	}
}
