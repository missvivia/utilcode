package com.xyl.mmall.cart.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.xyl.mmall.cart.dto.CartItemDTO;

/**
 * @author Yang,Nan
 *
 */
public final class CollectionUtil {
	public static List<CartItemDTO> mapToSkuList(final Map<Long, Integer> input) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		EntryTransformer<Long, Integer, CartItemDTO> transformer = new EntryTransformer() {
			@Override
			public Object transformEntry(Object key, Object value) {
				// TODO Auto-generated method stub
				CartItemDTO ret = new CartItemDTO();
				ret.setSkuid((long) key);
				ret.setCount((int) value);
				return ret;
			}
		};

		Map<Long, CartItemDTO> transformEntries = Maps.transformEntries(input, transformer);
		return new ArrayList<CartItemDTO>(transformEntries.values());
	}

	public static Map<byte[], byte[]> ListToSkuMap(final List<CartItemDTO> list) {
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		for (CartItemDTO c : list) {
			map.put(Long.valueOf(c.getSkuid()).toString().getBytes(), Integer.valueOf(c.getCount()).toString()
					.getBytes());
		}
		return map;
	}

}
