package com.example.myproject.services;

import com.example.myproject.pojo.UsersInformation;
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

public class ContactService {

	private DatastoreService entityStore;

	public ContactService() {
		entityStore = DatastoreServiceFactory.getDatastoreService();
	}

	// return the entity if the username and password matches or else null
	public Entity userAvailability(String userName, String userPassword) {
		// System.out.println("The useravailability");
		Entity user = checkUser(userName);
		if (user != null) {
			String dbPasswordValue = (String) user.getProperty("Password");
			if (dbPasswordValue.equals(userPassword)) {
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
	public Entity signUpUser(String userName, String password) {
		Entity user = checkUser(userName);
		// System.out.println("The signupuser");
		if (user == null) {// New user register into the database
			// System.out.println("user is going to create");
			Entity newEntity = new Entity("Contacts", userName);
			newEntity.setProperty("UserName", userName);
			newEntity.setProperty("Password", password);
			entityStore.put(newEntity);
			// System.out.println("new user is create");
			return newEntity;
		}
		return null;
	}

	// return entity if the user exist or else null (No user found)
	public Entity checkUser(String userName) {
		Key k = KeyFactory.createKey("Contacts", userName);
		try {
			Entity entityAvailable = entityStore.get(k);
			// User available in the database
			// System.out.println("The user is "+entityAvailable);
			return entityAvailable;
		} catch (EntityNotFoundException excep) {
			// No such user found
			// System.out.println("No such user found ");
			return null;
		}
	}

	// Add Entity (userInformation) in to database if not exist in the database
	// and returns Entity
	public void addUser(UsersInformation userInformation) {
		Entity user = checkUser(userInformation.getEmail());
		if (user == null) {
			// System.out.println("user is going to create");
			Entity newEntity = new Entity("Contacts", userInformation.getEmail());
			newEntity.setProperty("userName", userInformation.getEmail());
			newEntity.setProperty("name", userInformation.getName());
			newEntity.setProperty("email", userInformation.getEmail());
			newEntity.setProperty("password", userInformation.getPassword());
			entityStore.put(newEntity);
			// System.out.println("new user is create");
			// return newEntity;
		}
		// return user;
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
}
