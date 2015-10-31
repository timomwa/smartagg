package com.pixelandtag.smartagg.ejb.device;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.pixelandtag.smartagg.dao.device.DeviceDAOI;
import com.pixelandtag.smartagg.entities.Device;

@Stateless
@Remote
public class DeviceEJBImpl implements DeviceEJBI {
	
	@PersistenceContext(unitName = "smartpu")
	protected EntityManager em;
	
	@Inject
	private DeviceDAOI deviceDAO;

	private Logger logger = Logger.getLogger(getClass());
	
	public Device save(Device device) throws Exception{
		return deviceDAO.save(device);
	}
	
	public Device findByRegistrationId(String registrationid){
		return deviceDAO.findBy("registrationID", registrationid);
	}

}
