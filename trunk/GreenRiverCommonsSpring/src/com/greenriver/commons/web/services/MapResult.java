
package com.greenriver.commons.web.services;

import java.util.HashMap;
import java.util.Map;

/**
 * A service result encapsultaror for methods returning maps.
 * @author luisro
 */
public class MapResult<K,V> extends Result<Map<K,V>> {

    public MapResult() {
        this.setResult(new HashMap<K,V>());
    }
    
    public void put(K key, V value) {
        this.getResult().put(key, value);
    }
    
    public void putAll(Map<K,V> values) {
        this.getResult().putAll(values);
    }

    public boolean containsKey(K key) {
        return this.getResult().containsKey(key);
    }

    public V get(K key) {
        return getResult().get(key);
    }
    
}
