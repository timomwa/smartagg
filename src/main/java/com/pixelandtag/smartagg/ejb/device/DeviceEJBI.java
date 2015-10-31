package com.pixelandtag.smartagg.ejb.device;

import com.pixelandtag.smartagg.entities.Device;

public interface DeviceEJBI {

	public Device save(Device device) throws Exception;

	public Device findByRegistrationId(String registrationid);
}
