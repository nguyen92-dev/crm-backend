package vn.io.nguyen32.crm.rediscache.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.nguyennd.restsqlbackend.abstraction.common.ErrorStatus;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import vn.io.nguyen32.crm.rediscache.IRedisCacheService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements IRedisCacheService {

  StringRedisTemplate redisTemplate;
  ObjectMapper objectMapper;

  @Override
  public void setCache(String key, Object value) {
    redisTemplate.opsForValue().set(key, writeValueAsString(value));
  }

  @Override
  public void setCache(String key, Object value, long ttl, TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(key, writeValueAsString(value), ttl, timeUnit);
  }

  @Override
  public <T> Optional<T> getCache(String key, Class<T> clazz) {
    if (!isExist(key)) {
      return Optional.empty();
    }
    String result = redisTemplate.opsForValue().get(key);
    return Optional.ofNullable(readValue(result, clazz));
  }

  @Override
  public void setHashCache(String key, String hashKey, Object value) {
    redisTemplate.opsForHash().put(key, hashKey, writeValueAsString(value));
  }

  @Override
  public <T> Optional<T> getHashCache(String key, String hashKey, Class<T> clazz) {
    if (!isExistHash(key, hashKey)) {
      return Optional.empty();
    }
    String result = redisTemplate.opsForHash().get(key, hashKey).toString();
    return Optional.ofNullable(readValue(result, clazz));
  }

  @Override
  public void setListCache(String key, Object value) {
    redisTemplate.opsForList().leftPush(key, writeValueAsString(value));
  }

  @Override
  public <T> List<T> getListCache(String key, Class<T> clazz) {
    if (!isExist(key)) {
      return List.of();
    }
    return redisTemplate.opsForList().range(key, 0, -1).stream().map(v -> readValue(v, clazz)).toList();
  }

  @Override
  public void deleteCache(String key) {
    redisTemplate.delete(key);
  }

  @Override
  public void deleteCache(Collection<String> keys) {
    redisTemplate.delete(keys);
  }

  @Override
  public void deleteAllByPattern(String pattern) {
    ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
    redisTemplate.execute((RedisCallback<Void>) connection -> {
      try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
        while (cursor.hasNext()) {
          connection.keyCommands().del(cursor.next());
        }
      }
      return null;
    });
  }

  @Override
  public Collection<String> scanKeys(String pattern) {
    ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
    return redisTemplate.execute((RedisCallback<Collection<String>>) connection -> {
      List<String> keys = new ArrayList<>();
      try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
        while (cursor.hasNext()) {
          keys.add(new String(cursor.next()));
        }
        return keys;
      }
    });
  }

  @Override
  public boolean isExist(String key) {
    return redisTemplate.opsForValue().getOperations().hasKey(key);
  }

  public boolean isExistHash(String key, String hashKey) {
    return redisTemplate.opsForHash().hasKey(key, hashKey);
  }

  @Override
  public void deleteHashCache(String key, String hashKey) {
    redisTemplate.opsForHash().delete(key, hashKey);
  }

  @Override
  public void flushAll() {
    redisTemplate.execute((RedisCallback<Void>)connection -> {
      connection.serverCommands().flushAll();
      return null;
    });
  }

  private String writeValueAsString(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new BusinessException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  private <T> T readValue(String value, Class<T> clazz) {
    try {
      if (isNull(value) || value.isBlank() || value.equals("null")) {
        return null;
      }
      return objectMapper.readValue(value, clazz);
    } catch (JsonProcessingException e) {
      throw new BusinessException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
