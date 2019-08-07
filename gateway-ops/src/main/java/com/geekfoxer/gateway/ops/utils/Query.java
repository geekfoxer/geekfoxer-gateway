package com.geekfoxer.gateway.ops.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	// 
	private int offset;
	@Getter@Setter
	private int pageSize;
	@Getter@Setter
	private int pageNum;
	// 每页条数
	private int limit;

	public Query(Map<String, Object> params) {
		this.putAll(params);
		// 分页参数
		this.offset = Integer.parseInt(params.getOrDefault("offset", "1").toString());
		this.pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10").toString());
		this.limit = Integer.parseInt(params.getOrDefault("limit", "10").toString());
		this.pageNum = Integer.parseInt(params.getOrDefault("pageNum", "1").toString());
		this.put("offset", offset);
		this.put("page", offset / limit + 1);
		this.put("limit", limit);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
