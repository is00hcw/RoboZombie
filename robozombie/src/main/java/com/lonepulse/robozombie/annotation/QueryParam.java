package com.lonepulse.robozombie.annotation;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Identifies a <b>query parameter</b> to be included in a 
 * <a href="http://en.wikipedia.org/wiki/Query_string">query string</a>. 
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@GET("/repos/{user}/{repo}/issues")
 *List&lt;Issue&gt; getIssues(<b>@QueryParam("state")</b> String state, ... );</pre>
 * </code>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryParam {
	
	
	/**
	 * <p>The name of the query parameter which identifies this value in a name-value pair within a 
	 * query string.</p>
	 * 
	 * @return the name of the request parameter
	 * <br><br>
	 * @since 1.3.0
	 */
	String value();
}
