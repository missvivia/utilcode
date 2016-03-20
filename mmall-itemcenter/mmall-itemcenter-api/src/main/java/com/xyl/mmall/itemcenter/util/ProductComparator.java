package com.xyl.mmall.itemcenter.util;

import java.math.BigDecimal;
import java.util.Comparator;

import com.xyl.mmall.itemcenter.dto.POProductDTO;
import com.xyl.mmall.itemcenter.enums.SortType;

public class ProductComparator implements Comparator<POProductDTO> {

	private boolean isAsc;

	private SortType sortType;

	/** 默认排序类型，0：默认排序，1：按类目排序，2：按手工排序 */
	private int defaultSortType;

	public ProductComparator(boolean isAsc, SortType sortType, int defaultSortType) {
		this.isAsc = isAsc;
		this.sortType = sortType;
		this.defaultSortType = defaultSortType;
	}

	public ProductComparator(boolean isAsc, SortType sortType) {
		this.isAsc = isAsc;
		this.sortType = sortType;
	}

	@Override
	public int compare(POProductDTO arg0, POProductDTO arg1) {
		if (sortType == SortType.DISCOUNT) {
			return sortByDiscount(arg0, arg1);
		} else if (sortType == SortType.PRICE) {
			return sortByPrice(arg0, arg1);
		} else if (sortType == SortType.SALE) {
			return sortBySale(arg0, arg1);
		} else {
			return sortByDefault(arg0, arg1);
		}
	}

	public int sortByDefault(POProductDTO arg0, POProductDTO arg1) {
		if (arg0.getStock() == 0 && arg1.getStock() == 0) {
			if (defaultSortType == 1) {
				int p0 = arg0.getCategoryIndex();
				int p1 = arg1.getCategoryIndex();
				return p0 - p1;
			} else if (defaultSortType == 2) {
				int p0 = arg0.getSingleIndex();
				int p1 = arg1.getSingleIndex();
				if (p0 == 0 && p1 == 0)
					return 0;
				else if (p0 != 0 && p1 != 0)
					return p0 - p1;
				else
					return p1 - p0;
			} else
				return 0;
		} else if (arg0.getStock() != 0 && arg1.getStock() != 0) {
			if ((arg0.getCartStock() == 0 && arg1.getCartStock() == 0)
					|| (arg0.getCartStock() != 0 && arg1.getCartStock() != 0)) {
				if (defaultSortType == 1) {
					int p0 = arg0.getCategoryIndex();
					int p1 = arg1.getCategoryIndex();
					return p0 - p1;
				} else if (defaultSortType == 2) {
					int p0 = arg0.getSingleIndex();
					int p1 = arg1.getSingleIndex();
					if (p0 == 0 && p1 == 0)
						return 0;
					else if (p0 != 0 && p1 != 0)
						return p0 - p1;
					else
						return p1 - p0;
				} else
					return 0;
			} else {
				if (arg0.getCartStock() == 0)
					return 1;
				else
					return -1;
			}
		} else {
			if (arg0.getStock() == 0)
				return 1;
			else
				return -1;
		}
	}

	public int sortByPrice(POProductDTO arg0, POProductDTO arg1) {
		if (arg0.getStock() == 0 && arg1.getStock() == 0) {
			BigDecimal p0 = arg0.getSalePrice();
			BigDecimal p1 = arg1.getSalePrice();
			if (isAsc) {
				return p0.subtract(p1).intValue();
			} else {
				return p1.subtract(p0).intValue();
			}
		} else if (arg0.getStock() != 0 && arg1.getStock() != 0) {
			if ((arg0.getCartStock() == 0 && arg1.getCartStock() == 0)
					|| (arg0.getCartStock() != 0 && arg1.getCartStock() != 0)) {
				BigDecimal p0 = arg0.getSalePrice();
				BigDecimal p1 = arg1.getSalePrice();
				if (isAsc) {
					return p0.subtract(p1).intValue();
				} else {
					return p1.subtract(p0).intValue();
				}
			} else {
				if (arg0.getCartStock() == 0)
					return 1;
				else
					return -1;
			}
		} else {
			if (arg0.getStock() == 0)
				return 1;
			else
				return -1;
		}
	}

	public int sortByDiscount(POProductDTO arg0, POProductDTO arg1) {
		if (arg0.getStock() == 0 && arg1.getStock() == 0) {
			BigDecimal p0 = arg0.getDiscount();
			BigDecimal p1 = arg1.getDiscount();
			if (isAsc) {
				if (p0.subtract(p1).doubleValue() < 0)
					return -1;
				else if (p0.subtract(p1).doubleValue() > 0)
					return 1;
				else
					return 0;
			} else {
				if (p1.subtract(p0).doubleValue() < 0)
					return -1;
				else if (p1.subtract(p0).doubleValue() > 0)
					return 1;
				else
					return 0;
			}
		} else if (arg0.getStock() != 0 && arg1.getStock() != 0) {
			if ((arg0.getCartStock() == 0 && arg1.getCartStock() == 0)
					|| (arg0.getCartStock() != 0 && arg1.getCartStock() != 0)) {
				BigDecimal p0 = arg0.getDiscount();
				BigDecimal p1 = arg1.getDiscount();
				if (isAsc) {
					if (p0.subtract(p1).doubleValue() < 0)
						return -1;
					else if (p0.subtract(p1).doubleValue() > 0)
						return 1;
					else
						return 0;
				} else {
					if (p1.subtract(p0).doubleValue() < 0)
						return -1;
					else if (p1.subtract(p0).doubleValue() > 0)
						return 1;
					else
						return 0;
				}
			} else {
				if (arg0.getCartStock() == 0)
					return 1;
				else
					return -1;
			}

		} else {
			if (arg0.getStock() == 0)
				return 1;
			else
				return -1;
		}
	}

	public int sortBySale(POProductDTO arg0, POProductDTO arg1) {
		if (arg0.getStock() == 0 && arg1.getStock() == 0) {
			int p0 = arg0.getSaleTotal();
			int p1 = arg1.getSaleTotal();
			if (isAsc) {
				return p0 - p1;
			} else {
				return p1 - p0;
			}
		} else if (arg0.getStock() != 0 && arg1.getStock() != 0) {
			if ((arg0.getCartStock() == 0 && arg1.getCartStock() == 0)
					|| (arg0.getCartStock() != 0 && arg1.getCartStock() != 0)) {
				int p0 = arg0.getSaleTotal();
				int p1 = arg1.getSaleTotal();
				if (isAsc) {
					return p0 - p1;
				} else {
					return p1 - p0;
				}
			} else {
				if (arg0.getCartStock() == 0)
					return 1;
				else
					return -1;
			}
		} else {
			if (arg0.getStock() == 0)
				return 1;
			else
				return -1;
		}
	}
}
