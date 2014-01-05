package com.lonepulse.robozombie.proxy;

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

import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.mock.ExtendedMockService;

/**
 * <p>Emulates a <b>sub-type</b> which extends a third-party service which should not be searched 
 * during endpoint injection.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br> 
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public class LocalMockService extends ExtendedMockService {
	
	
	@Bite
	private MockEndpoint localEndpoint; //super-type has taken care of infection
	
	public MockEndpoint getLocalEndpoint() {
		
		return localEndpoint;
	}
}
