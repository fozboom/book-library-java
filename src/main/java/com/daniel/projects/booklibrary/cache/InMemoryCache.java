package com.daniel.projects.booklibrary.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public abstract class InMemoryCache<K, V> {

	private final Map<K, V> cacheMap;
	private final Integer maxSize;

	public InMemoryCache(@Value("${cache.maxSize}") Integer maxSize) {
		this.maxSize = maxSize;
		this.cacheMap = new LinkedHashMap<>() {
			@Override
			protected boolean removeEldestEntry(Map.Entry eldest) {
				return size() > InMemoryCache.this.maxSize;
			}
		};
	}

	public V get(K key) {

		return cacheMap.get(key);
	}

	public void put(K key, V value) {

		cacheMap.put(key, value);
	}

	public void remove(K key) {

		cacheMap.remove(key);
	}

	public void clear() {

		cacheMap.clear();
	}
}