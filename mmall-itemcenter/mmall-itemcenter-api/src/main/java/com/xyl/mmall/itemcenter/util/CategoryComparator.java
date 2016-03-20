package com.xyl.mmall.itemcenter.util;

import java.util.Comparator;

import com.xyl.mmall.itemcenter.dto.CategoryGroupDTO;
import com.xyl.mmall.itemcenter.meta.PoProduct;

public class CategoryComparator implements Comparator<CategoryGroupDTO> {

	private boolean isAsc;

	public CategoryComparator(boolean isAsc) {
		this.isAsc = isAsc;
	}

	@Override
	public int compare(CategoryGroupDTO arg0, CategoryGroupDTO arg1) {
		if (arg0.getCategoryIndex() == 0 && arg1.getCategoryIndex() == 0)
			return 0;
		else {
			if (isAsc)
				return arg0.getCategoryIndex() - arg1.getCategoryIndex();
			else
				return arg1.getCategoryIndex() - arg0.getCategoryIndex();
		}
	}

}
