package com.pixelandtag.smartagg.android.gcm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.XMPPError;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class SmartStanzaListener implements StanzaListener {
	
	//private XMPPConnection connection;

	private static final Logger logger = Logger.getLogger("SmackCcsClient");

	@Override
	@SuppressWarnings({"unchecked" })
	public void processPacket(Stanza packet) throws NotConnectedException {
		GcmPacketExtension gcmPacket = (GcmPacketExtension) packet.getExtension(GCMProps.GCM_NAMESPACE);/*(GcmPacketExtension) incomingMessage
				.getExtension(GCMProps.GCM_NAMESPACE);*/
		System.out.println("\n\n\t\t>> gcmPacket: "+gcmPacket);
		
		if(gcmPacket!=null){
			
			String json = gcmPacket.getJson();
			System.out.println("\n\n\t\t>> json: "+json);
			try {
				Map<String, Object> jsonMap = (Map<String, Object>) JSONValue.parseWithException(json);
				handleMessage(jsonMap);
			} catch (ParseException e) {
				logger.error(e.getMessage() + "Error parsing JSON " + json, e);
			} catch (Exception e) {
				logger.error(e.getMessage() + "Couldn’t send echo.", e);
			}
		}

	}

	private void handleMessage(Map<String, Object> jsonMap) throws NotConnectedException {
		// only present for "ack"/"nack" messages
		Object messageType = jsonMap.get("message_type");
		if (messageType == null) {
			// Normal upstream data message
			CcsMessage msg = getMessage(jsonMap);
			handleIncomingDataMessage(msg);
			// Send ACK to CCS
			String ack = createJsonAck(msg.getFrom(), msg.getMessageId());
			logger.info(" \t ack to be sent back ? "+ack);
			send(ack);
		} else if ("ack".equals(messageType.toString())) {
			// Process Ack
			handleAckReceipt(jsonMap);
		} else if ("nack".equals(messageType.toString())) {
			// Process Nack
			handleNackReceipt(jsonMap);
		} else {
			logger.warn(String.format("Unrecognized message type (%s)",messageType.toString()));
		}
	}

	private CcsMessage getMessage(Map<String, Object> jsonObject) {
		String from = jsonObject.get("from").toString();

		// PackageName of the application that sent this message.
		String category = jsonObject.get("category").toString();

		// unique id of this message
		String messageId = jsonObject.get("message_id").toString();

		@SuppressWarnings("unchecked")
		Map<String, String> payload = (Map<String, String>) jsonObject
				.get("data");

		CcsMessage msg = new CcsMessage(from, category, messageId, payload);

		return msg;
	}

	// / new: customized version of the standard handleIncomingDateMessage
	// method
	/**
	 * Handles an upstream data message from a device application.
	 */
	public void handleIncomingDataMessage(CcsMessage msg) {
		if (msg.getPayload().get("action") != null) {
			PayloadProcessor processor = ProcessorFactory.getProcessor(msg
					.getPayload().get("action"));
			processor.handleMessage(msg);
		}
	}
	
	/**
     * Creates a JSON encoded ACK message for an upstream message received from
     * an application.
     *
     * @param to RegistrationId of the device who sent the upstream message.
     * @param messageId messageId of the upstream message to be acknowledged to
     * CCS.
     * @return JSON encoded ack.
     */
    public static String createJsonAck(String to, String messageId) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("message_type", "ack");
        message.put("to", to);
        message.put("message_id", messageId);
        return JSONValue.toJSONString(message);
    }
    
    
    /**
     * Sends a downstream GCM message.
     * @throws NotConnectedException 
     */
    public void send(String jsonRequest) throws NotConnectedException {
        Stanza request = new GcmPacketExtension(jsonRequest).toPacket();
        CcsClient.getInstance().connection.sendStanza(request);
        // connection.sendStanza(request);
		
    }
    
    /**
     * Handles an ACK.
     *
     * <p>
     * By default, it only logs a INFO message, but subclasses could override it
     * to properly handle ACKS.
     */
    public void handleAckReceipt(Map<String, Object> jsonObject) {
        String messageId = jsonObject.get("message_id").toString();
        String from = jsonObject.get("from").toString();
        logger.info("handleAckReceipt() from: " + from + ", messageId: " + messageId);
    }
    
    
    /**
     * Handles a NACK.
     *
     * <p>
     * By default, it only logs a INFO message, but subclasses could override it
     * to properly handle NACKS.
     */
    public void handleNackReceipt(Map<String, Object> jsonObject) {
        String messageId = jsonObject.get("message_id").toString();
        String from = jsonObject.get("from").toString();
        logger.info("handleNackReceipt() from: " + from + ", messageId: " + messageId);
    }

}
