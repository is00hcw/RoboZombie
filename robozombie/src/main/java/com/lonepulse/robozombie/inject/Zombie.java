package com.lonepulse.robozombie.inject;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 Lonepulse
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.util.Log;

import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.executor.ConfigurationFailedException;
import com.lonepulse.robozombie.executor.RequestExecutors;
import com.lonepulse.robozombie.inject.EndpointDirectory;
import com.lonepulse.robozombie.inject.EndpointProxyFactory;

/**
 * <p>An animated corpse which spreads the {@link Endpoint} infection via a {@link Bite}. Used for <b>injecting</b> 
 * concrete implementations of endpoint interface definitions. Place an @{@link Bite} annotation on all instance 
 * properties which are endpoints and invoke {@link Zombie#infect(Object)} or {@link Zombie#infect(Class)}.</p> 
 *  
 * @version 1.3.0
 * <br><br>
 * @since 1.1.1
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Zombie {
	
	/**
	 * <p>The <b>default configuration</b> which is used for endpoint request execution. The configured properties 
	 * pertain to the <a href="http://hc.apache.org">Apache HTTP Components</a> library which provides the foundation 
	 * for network communication.</p>
	 * 
	 * <p>Configurations can be revised for each {@link Endpoint} using <b>@Configuration</b> by specifying the 
	 * {@link Class} of a {@link Configuration} extension. Simply override the required template methods and provide 
	 * a <b>new instance</b> of the desired property. For example, override {@link Configuration#httpClient()} to 
	 * return a custom {@link HttpClient} which might be configured with alternative {@link Scheme}s, timeouts ..etc.</p>
	 * 
	 * <p>For more information on configuring your own instance of {@link HttpClient} refer the 
	 * <a href="http://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/index.html">Apache HC Tutorial</a>.</p>
	 * 
	 * <p><b>Note</b> that all extensions must expose a default non-parameterized constructor.</p>
	 *  
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static abstract class Configuration {
		
		
		private static final Configuration DEFAULT = RequestExecutors.CONFIGURATION.getDefault();
		
		
		/**
		 * <p>The <i>out-of-the-box</i> configuration for an instance of {@link HttpClient} which will be used for 
		 * executing all endpoint requests.</p> 
		 * 
		 * <p>It registers two {@link Scheme}s:</p>
		 * 
		 * <ol>
		 * 	<li><b>HTTP</b> on port <b>80</b> using sockets from {@link PlainSocketFactory#getSocketFactory}</li>
		 * 	<li><b>HTTPS</b> on port <b>443</b> using sockets from {@link SSLSocketFactory#getSocketFactory}</li>
		 * </ol>
		 * 
		 * <p>It uses a {@link ThreadSafeClientConnManager} with the following parameters:</p>
		 * <ol>
		 * 	<li><b>Redirecting:</b> enabled</li>
		 * 	<li><b>Connection Timeout:</b> 30 seconds</li>
		 * 	<li><b>Socket Timeout:</b> 30 seconds</li>
		 * 	<li><b>Socket Buffer Size:</b> 12000 bytes</li>
		 * 	<li><b>User-Agent:</b> via <code>System.getProperty("http.agent")</code></li>
		 * </ol>
		 *
		 * @return the instance of {@link HttpClient} which will be used for request execution
		 * <br><br>
		 * @throws ConfigurationFailedException
		 * 			if the default configuration for the {@link HttpClient} failed to be instantiated
		 * <br><br>
		 * @since 1.2.4
		 * <br><br>
		 * @see <a href="http://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/index.html">Apache HC Tutorial</a>
		 */
		public HttpClient httpClient() {
			
			return DEFAULT.httpClient();
		}
	}
	
	
	/**
	 * <p>Constructor visibility restricted. Instantiation is nonsensical. 
	 */
	private Zombie() {}
	
	/**
	 * <p>Takes an object and scans it for {@link Bite} annotations. If found, 
	 * the <b>singleton</b> for the annotated endpoint interface implementation will be 
	 * injected.</p>
	 * <br>
	 * <b>Usage:</b>
	 * <br><br>
	 * <ul>
	 * <li>
	 * <h5>Property Injection</h5>
	 * <pre>
	 * <code>@Bite
	 * TwitterEndpoint twitterEndpoint;
	 * {
	 * &nbsp; &nbsp; Zombie.infect(this);
	 * }
	 * </code>
	 * </pre>
	 * </li>
	 * <li>
	 * <h5>Setter Injection</h5>
	 * <pre>
	 * <code>
	 * private TwitterEndpoint twitterEndpoint;
	 * </code>
	 * <code>@Bite
	 * public void setTwitterEndpoint(TwitterEndpoint twitterEndpoint) {
	 * 
	 * &nbsp; &nbsp; this.twitterEndpoint = twitterEndpoint;
	 * }
	 * <br><br>
	 * <i>Instantiation:</i><br><br>
	 * TwitterService twitterService = Zombie.infect(new TwitterService());
	 * </code>
	 * </pre>
	 * </li>
	 * </ul>
	 * 
	 * @param injectee
	 * 			the object to which the endpoint must be injected
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the object supplied for endpoint injection is {@code null} 
	 * <br><br>
	 * @since 1.1.1
	 */
	public static void infect(Object injectee) {
		
		if(injectee == null) {
			
			throw new IllegalArgumentException("The object supplied for endpoint injection was <null>");
		}
		
		Class<?> injecteeClass = injectee.getClass();
		Field[] fields = injecteeClass.getDeclaredFields();
		
		Class<?> endpointInterface = null;
		
		for (Field field : fields) {
			
			try {
				
				if(field.isAnnotationPresent(Bite.class)) {
					
					endpointInterface = field.getType();
					Object proxyInstance = Zombie.createAndRegisterProxy(endpointInterface);
					
					try { //1.Simple Property Injection 
						
						field.set(injectee, proxyInstance);
					}
					catch (IllegalAccessException iae) { //2.Setter Injection 
						
						String fieldName = field.getName();
						String mutatorName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1); 
						
						try {
						
							Method mutator = injecteeClass.getDeclaredMethod(mutatorName, endpointInterface);
							mutator.invoke(injectee, proxyInstance);
						}
						catch (NoSuchMethodException nsme) { //3.Forced Property Injection
							
							field.setAccessible(true);
							field.set(injectee, proxyInstance);
						}
					}
				}
			} 
			catch (Exception e) {
				
				StringBuilder stringBuilder = new StringBuilder()
				.append("Failed to inject the endpoint proxy instance of type ")
				.append(endpointInterface.getName())
				.append(" on property ")
				.append(field.getName())
				.append(" at ")
				.append(injecteeClass.getName())
				.append(". ");
				
				Log.e(Zombie.class.getName(), stringBuilder.toString(), e);
			}
		}
	}
	
	/**
	 * <p>Takes the {@link Class} structure of the injectee and uses the designated 
	 * parameterized constructor to create a new instance via <i>constructor injection</i>.</p>
	 * <br>
	 * <b>Usage:</b>
	 * <br><br>
	 * <ul>
	 * <li>
	 * <h5>Constructor Injection</h5>
	 * <br>
	 * <p>The implication is that endpoint <b>instantiation</b> is delegated to the {@link Zombie}.</p> 
	 * <pre>
	 * <code>@Bite
	 * public TwitterService(TwitterEndpoint twitterEndpoint) {
	 * 
	 * &nbsp; &nbsp; this.twitterEndpoint = twitterEndpoint;
	 * } 
	 * 
	 * <i>Instantiation:</i>
	 * 
	 * TwitterService twitterService = Zombie.infect(TwitterService.class);
	 * </code>
	 * </pre>
	 * </li>
	 * </ul>
	 * 
	 * @param injectee
	 * 			the {@link Class} structure of the object to be created with the constructor injection endpoint
	 * 
	 * @return a <b>new instance</b> of the injectee or {@code null} if constructor injection failed
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the {@link Class} supplied for endpoint injection is {@code null} 
	 * <br><br>
	 * @since 1.1.1
	 */
	public static <T extends Object> T infect(Class<T> injectee) {

		if(injectee == null) {
			
			throw new IllegalArgumentException("The java.lang.Class supplied for endpoint injection was <null>");
		}
		
		Class<?> endpointInterface = null;
		Constructor<?>[] constructors = injectee.getConstructors();

		for (Constructor<?> constructor : constructors) { //inject with the first annotated constructor

			if (constructor.isAnnotationPresent(Bite.class)) {

				Class<?>[] constructorParameters = constructor.getParameterTypes();

				if (constructorParameters.length > 0) {

					try {

						endpointInterface = constructorParameters[0];
						Object proxyInstance = Zombie.createAndRegisterProxy(endpointInterface);
								
						T instance = injectee.cast(constructor.newInstance(proxyInstance));
						
						Zombie.infect(instance); //constructor injection complete; now perform property injection 
						
						return instance;
					} 
					catch (Exception e) {
						
						StringBuilder stringBuilder = new StringBuilder()
						.append("Failed to inject the endpoint proxy instance of type ")
						.append(endpointInterface.getName())
						.append(" on constructor ")
						.append(constructor.getName())
						.append(" at ")
						.append(injectee.getName())
						.append(". ");
						
						Log.e(Zombie.class.getName(), stringBuilder.toString(), e);
					}
				}
			}
		}

		try { //constructor injection failed, attempt instantiation without injection
			
			StringBuilder stringBuilder = new StringBuilder()
			.append("Incompatible contructor(s) for injection on ")
			.append(injectee.getName())
			.append(". Are you missing an @Bite annotation on the constructor? \n")
			.append("Attempting property injection. ");
			
			Log.e(Zombie.class.getName(), stringBuilder.toString());
			
			T instance = injectee.newInstance();
			Zombie.infect(instance);
			
			return instance; 
		}
		catch (Exception e) {

			StringBuilder stringBuilder = new StringBuilder()
			.append("Failed to create an instance of  ")
			.append(injectee.getName())
			.append(" with constructor injection. ");
			
			Log.e(Zombie.class.getName(), stringBuilder.toString(), e);
			
			return null;
		}
	}
	
	private static Object createAndRegisterProxy(Class<?> endpointClass) throws InstantiationException, IllegalAccessException {
		
		Object proxyInstance = EndpointProxyFactory.INSTANCE.create(endpointClass); 
		EndpointDirectory.INSTANCE.put(endpointClass, proxyInstance);
		
		RequestExecutors.CONFIGURATION.register(endpointClass);
		
		return proxyInstance;
	}
}