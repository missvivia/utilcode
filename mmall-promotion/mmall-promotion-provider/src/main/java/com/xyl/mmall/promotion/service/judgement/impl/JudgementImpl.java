/*
 * 2014-9-10
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 */
package com.xyl.mmall.promotion.service.judgement.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.xyl.mmall.promotion.service.judgement.Judgement;

/**
 * JudgementImpl.java
 * 
 * @author <A HREF="mailto:hzruanhy@corp.netease.com">Roy</A>
 * @version 1.0 2014-9-10
 * @since 1.0
 */
@Service
public class JudgementImpl implements Judgement {

	@Override
	public boolean equal(Object base, Object desc) {
		if (base instanceof Number) {
			return base == desc;
		} else if (base instanceof String) {
			return base.equals(desc);
		}
		return base == desc;
	}

	@Override
	public boolean nonEqual(Object base, Object desc) {
		if (base instanceof Number) {
			return base != desc;
		} else if (base instanceof String) {
			return !base.equals(desc);
		}
		return base != desc;
	}

	@Override
	public boolean greaterThan(Object base, Object desc) {
		if (base instanceof Number) {
			return new BigDecimal(String.valueOf(base)).compareTo(new BigDecimal(String.valueOf(desc))) > 0;
		}
		return false;
	}

	@Override
	public boolean greaterEqualThan(Object base, Object desc) {
		if (base instanceof Number) {
			return new BigDecimal(String.valueOf(base)).compareTo(new BigDecimal(String.valueOf(desc))) >= 0;
		}
		return false;
	}

	@Override
	public boolean lessThan(Object base, Object desc) {
		if (base instanceof Number) {
			return new BigDecimal(String.valueOf(base)).compareTo(new BigDecimal(String.valueOf(desc))) < 0;
		}
		return false;
	}

	@Override
	public boolean lessEqualThan(Object base, Object desc) {
		if (base instanceof Number) {
			return new BigDecimal(String.valueOf(base)).compareTo(new BigDecimal(String.valueOf(desc))) <= 0;
		}
		return false;
	}

	@Override
	public boolean between(Object base, Object low, Object heigh) {
		if (base instanceof Number) {
			return new BigDecimal(String.valueOf(base)).compareTo(new BigDecimal(String.valueOf(low))) >= 0
					&& new BigDecimal(String.valueOf(base)).compareTo(new BigDecimal(String.valueOf(heigh))) < 0;
		}
		return false;
	}

	@Override
	public boolean contains(Object base, Object desc) {
		if (desc == null) {
			return false;
		}

		if (base == null) {
			return true;
		}

		return String.valueOf(base).contains(String.valueOf(desc));
	}

	@Override
	public boolean indexOf(Object base, String desc) {
		return String.valueOf(desc).contains(String.valueOf(base));
	}

}
