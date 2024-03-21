package com.daniel.projects.booklibrary.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public abstract class InMemoryCache<K, V> {
	private final Map<K, V> cacheMap;
	private final Integer maxSize;
	protected InMemoryCache(
			@Value("${cache.maxSize}") final Integer size) {
		this.maxSize = size;
		this.cacheMap = new LinkedHashMap<>() {
			@Override
			protected boolean removeEldestEntry(
					final Map.Entry eldest) {
				return size() > InMemoryCache.this.maxSize;
			}
		};
	}

	public V get(final K key) {

		return cacheMap.get(key);
	}

	public void put(final K key, final V value) {

		cacheMap.put(key, value);
	}

	public void remove(final K key) {

		cacheMap.remove(key);
	}

	public Collection<V> values() {

		return cacheMap.values();
	}

}
