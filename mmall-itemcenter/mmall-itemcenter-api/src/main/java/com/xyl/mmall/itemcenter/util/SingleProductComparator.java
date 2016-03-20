package com.xyl.mmall.itemcenter.util;

import java.util.Comparator;

import com.xyl.mmall.itemcenter.meta.PoProduct;

public class SingleProductComparator implements Comparator<PoProduct> {

	@Override
	public int compare(PoProduct o1, PoProduct o2) {
		if (o1.getCategoryIndex() == 0 && o2.getCategoryIndex() == 0)
			return 0;
		else {
			return o1.getSingleIndex() - o2.getSingleIndex();
		}
	}

}
