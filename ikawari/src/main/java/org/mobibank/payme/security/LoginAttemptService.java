package org.mobibank.payme.security;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {

	private final int MAX_ATTEMPT = 10;
	LoadingCache<String, Integer> attemptsCache;

	public LoginAttemptService() {
		super();
		attemptsCache = CacheBuilder.newBuilder().
				expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}
	
	/**
	 * Cette appelé après une authentification reussie
	 * elle réinitialise le nombre de tentative
	 * @param key
	 */
	public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }
 
	/**
	 * Methode executé à chanque essaie de connexion
	 * @param key
	 */
    public void loginFailed(String key) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }
 
    /**
     * Verrifie si un utilisateur n'est pas blacklister
     * @param key
     * @return
     */
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
}