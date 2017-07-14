package com.example.myproject.services;

import java.util.logging.Level;

import com.example.myproject.pojo.Contact;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class ContactService {

	private DatastoreService entityStore;

	public ContactService() {
		entityStore = DatastoreServiceFactory.getDatastoreService();
	}

	// return the entity if the username and password matches or else null
	public Entity loginUser(String userEmail, String userPassword) {
		Entity user = getId(userEmail);
		if (user != null) {
			String dbPasswordValue = (String) user.getProperty("password");
			if (userPassword.equals(dbPasswordValue)) {
				// System.out.println("password correct");
				return user;
			} else {
				// System.out.println("password missmatch");
				return null;
			}
		} else {
			// System.out.println("no user found");
			return null;
		}

	}

	// return the entity, new user created(stored in the database) else null
	// (already user exist in that name)
	public Entity signUpUser(String name, String password, String email) {
		Entity user = getId(email);
		if (user == null) {
			return createContact(email, name, password);
		}
		return null;
	}

	// return entity if the user exist or else null (No user found)
	public Entity getId(String email) {
		
		MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
		memcache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		Key key = KeyFactory.createKey("Contacts", email);
		
		Object contactFromMemCache = memcache.get(key);
		if (contactFromMemCache == null) {
			System.out.println("entity not found in the cache");
			try {
				System.out.println("retriving entity from the datastore");
				
				Entity contact = entityStore.get(key);
				memcache.put(key, contact, Expiration.byDeltaSeconds(3600));
				System.out.println("retriving entity from the datastore and storing it in the cache");
				return contact;

			} catch (EntityNotFoundException excep) {
				// return null;
				System.out.println("Exception occured" + excep);
			}
			return null;
		}
		else{
			System.out.println("retriving entity from the cache memory");
			return (Entity)contactFromMemCache;
		}
		
		
		
		/*Key k = KeyFactory.createKey("Contacts", email);
		try {
			Entity contact = entityStore.get(k);
			return contact;

		} catch (EntityNotFoundException excep) {
			// return null;
			System.out.println("Exception occured" + excep);
		}
		return null;*/
	}

	// Add Entity (userInformation) in to database if not exist in the database
	// and returns Entity
	public void addUser(Contact userInformation) {
		Entity user = getId(userInformation.getEmail());
		if (user == null) {
			// System.out.println("user is going to create");
			/*
			 * user = new Entity("Contacts", userInformation.getEmail());
			 * user.setProperty("userName", userInformation.getEmail());
			 * user.setProperty("name", userInformation.getName());
			 * user.setProperty("email", userInformation.getEmail());
			 * user.setProperty("password", userInformation.getPassword());
			 * entityStore.put(user);
			 */

			createContact(userInformation.getEmail(), userInformation.getName(), userInformation.getPassword());
			// System.out.println("new user is create");
			// return newEntity;
		}
		// return user;
	}

	public Entity addGoogleUser(String email, String name) {
		Entity user = getId(email);
		if (user == null) {
			// Entity newEntity = new Entity("Contacts", email);
			/*
			 * user = new Entity("Contacts", email); user.setProperty("email",
			 * email); user.setProperty("userName", email);
			 * user.setProperty("name", name);
			 * 
			 * entityStore.put(user);
			 */
			return createContact(email, name, "");
		}
		return user;
		// return user;
	}

	public String deleteUser(String email) {
		System.out.println("email id going to delete by the delete function " + email);
		Entity user = getId(email);
		if (user != null) {
			Key userKey = KeyFactory.createKey("Contacts", email);
			
			MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
			memcache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			Object contactFromMemCache = memcache.get(userKey);
			if (contactFromMemCache == null) {
				System.out.println("entity not found in the cache for delete");
			}else{				
				memcache.delete(userKey);
				System.out.println("Deleted from the cache");
			}
			
			entityStore.delete(userKey);
			System.out.println("user deleted successfully from the datastore");
			
			return "Success";
		} else {
			System.out.println("user didn't found");
			return "Failure";
		}
		/*System.out.println("email id going to delete by the delete function " + email);
		Entity user = getId(email);
		if (user != null) {
			Key userKey = KeyFactory.createKey("Contacts", email);
			entityStore.delete(userKey);
			System.out.println("user deleted successfully");
			return "Success";
		} else {
			System.out.println("user didn't found");
			return "Failure";
		}*/
		
		/*
		 * Entity user = fetchUserInformation(email);
		 * System.out.println("entity info in the delete function "+user);
		 * if(user != null) { Key userKey = KeyFactory.createKey("Contacts",
		 * email); entityStore.delete(userKey);
		 * System.out.println("user deleted successfully"); return "Success"; }
		 * else{ System.out.println("user didn't found"); return "Failure"; }
		 */
	}

	public Entity createContact(String email, String name, String password) {
		// System.out.println("creating new the name " + name);
		Entity user = new Entity("Contacts", email);
		user.setProperty("email", email);
		user.setProperty("userName", email);
		user.setProperty("name", name);
		user.setProperty("password", password);

		entityStore.put(user);
		System.out.println("New contact is created and stored into the datastore");
		
		MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
		memcache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		Key key = KeyFactory.createKey("Contacts", email);
		memcache.put(key, user, Expiration.byDeltaSeconds(3600));
		System.out.println("Updated information into the cache successfully");
		
		return user;
	}

	public String updateUserName(String emailId, String newName) {
		
		Key key = KeyFactory.createKey("Contacts", emailId);
		try {
			Entity user = entityStore.get(key);
			user.setProperty("name", newName);
			entityStore.put(user);
			System.out.println("Renamed and stored into the datastore successfully");
			
			MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
			memcache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			memcache.put(key, user, Expiration.byDeltaSeconds(3600));
			System.out.println("Information is updated into the cache successfully");
			
			return "Success";
			
		} catch(EntityNotFoundException exception){
			System.out.println("No User found");
		}
		return "Failure";
		
		/*System.out.println("update username function");
		Entity user = getId(emailId);
		if (user != null) {
			user.setProperty("name", newName);
			entityStore.put(user);
			return "Success";
		}
		return "Failure";*/
	}

	public QueryResultList<Entity> fetchUserInformationWithLimit(int limit, String startCursor) {

		FetchOptions fetchOptionsLimit = FetchOptions.Builder.withLimit(limit);
		if (startCursor != null) {
			fetchOptionsLimit.startCursor(Cursor.fromWebSafeString(startCursor));
		}
		Query query = new Query("Contacts");
		PreparedQuery preparedQuery = entityStore.prepare(query);
		return preparedQuery.asQueryResultList(fetchOptionsLimit);

	}

	/*
	 * public Entity fetchUserInformation(String email) { System.out.
	 * println("Using query The value of email going to search using key "+email
	 * ); //Key k = KeyFactory.createKey("Contacts", email); Key contactKey =
	 * KeyFactory.createKey("Contacts", email);
	 * System.out.println("Using query The value of key going to search "
	 * +contactKey); Filter keyFilter = new
	 * FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL,
	 * contactKey); Query query = new Query("Contacts").setFilter(keyFilter);
	 * PreparedQuery preparedQuery = entityStore.prepare(query);
	 * 
	 * return preparedQuery.asSingleEntity(); }
	 */
}
