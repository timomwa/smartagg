package com.pixelandtag.smartagg.android.gcm;

import org.jivesoftware.smack.SmackException.NotConnectedException;

public class MessageProcessor implements PayloadProcessor {

	@Override
	public void handleMessage(CcsMessage msg) {
		PseudoDao dao = PseudoDao.getInstance();
        CcsClient client = CcsClient.getInstance();
        String msgId = dao.getUniqueMessageId();
        String jsonRequest = 
                CcsClient.createJsonMessage(
                        msg.getFrom(), 
                        msgId, 
                        msg.getPayload(), 
                        null, 
                        null, // TTL (null -> default-TTL) 
                        false);
        try {
			client.send(jsonRequest);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} 

	}

}
