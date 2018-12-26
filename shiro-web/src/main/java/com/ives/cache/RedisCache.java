package com.ives.cache;

import com.ives.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<K, V> implements Cache<K, V> {
   private final String CACHE_PREFIX = "ives-cache";

   @Resource
   private JedisUtil jedisUtil;

   private byte[] getKey(K k) {
      if(k instanceof String) {
         return (CACHE_PREFIX + k).getBytes();
      }

      return SerializationUtils.serialize(k);
   }

   public V get(K k) throws CacheException {
      System.out.println("从RedisCache中获取数据");
      byte[] value = jedisUtil.get(getKey(k));

      if(value != null) {
          return (V) SerializationUtils.deserialize(value);
      }
      return null;
   }

   public V put(K k, V v) throws CacheException {
      jedisUtil.set(getKey(k), SerializationUtils.serialize(v));
      jedisUtil.expire(getKey(k), 600);
      return null;
   }

   public V remove(K k) throws CacheException {
      byte[] key = getKey(k);
      byte[] value = jedisUtil.get(key);
      jedisUtil.delete(key);

      if(value != null) {
         return (V) SerializationUtils.deserialize(value);
      }
      return null;
   }

   public void clear() throws CacheException {
      //
   }

   public int size() {
      return 0;
   }

   public Set<K> keys() {
      return null;
   }

   public Collection<V> values() {
      return null;
   }
}
