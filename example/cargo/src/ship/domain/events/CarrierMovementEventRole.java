/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ship.domain.events;

import java.util.Date;

import ship.domain.Cargo;
import ship.domain.CarrierMovement;
import ship.domain.HandlingEvent;
import ship.domain.Location;

public class CarrierMovementEventRole {

	public void load(CarrierMovement carrierMovement, Location location, Cargo cargo) {
		System.out.print(" load from: " + carrierMovement.getFrom());
	}

	public void unLoad(CarrierMovement carrierMovement, Location location, Cargo cargo) {
		System.out.print(" unLoad to: " + carrierMovement.getTo());
		if (carrierMovement.getTo().equals(location)) {
			// 创建 HandleEvent
			Date completionTime = new Date();
			final HandlingEvent event = new HandlingEvent(carrierMovement, cargo, location, completionTime, new Date(), HandlingEvent.Type.UNLOAD);
			// add HandleEvent DeliveryHistory
			cargo.addHandlingEvent(event);
			// 改变Cargo运输状态
			cargo.changeState();
		}

	}
}
