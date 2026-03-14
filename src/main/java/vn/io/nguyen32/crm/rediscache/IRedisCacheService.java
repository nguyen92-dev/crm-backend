package vn.io.nguyen32.crm.rediscache;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface IRedisCacheService {
  void setCache(String key, Object value);
  void setCache(String key, Object value, long ttl, TimeUnit timeUnit);
  <T> Optional<T> getCache(String key, Class<T> clazz);
  void setHashCache(String key, String hashKey, Object value);
  <T> Optional<T> getHashCache(String key, String hashKey, Class<T> clazz);
  void setListCache(String key, Object value);
  <T> List<T> getListCache(String key, Class<T> clazz);
  Collection<String> scanKeys(String pattern);
  boolean isExist(String key);
  boolean isExistHash(String key, String hashKey);
  void deleteHashCache(String key, String hashKey);
  void deleteCache(String key);
  void deleteCache(Collection<String> keys);
  void deleteAllByPattern(String pattern);
  void flushAll();
}
