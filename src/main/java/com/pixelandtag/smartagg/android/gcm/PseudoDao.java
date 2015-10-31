package com.pixelandtag.smartagg.android.gcm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
public class PseudoDao {


    private final static PseudoDao instance = new PseudoDao();
    private final static Random sRandom = new Random();
    private final Set<Integer> mMessageIds = new HashSet<Integer>();
    private final Map<String, List<String>> mUserMap = new HashMap<String, List<String>>();
    private final List<String> mRegisteredUsers = new ArrayList<String>();
    private final Map<String, String> mNotificationKeyMap = new HashMap<String, String>();
    
    private PseudoDao() {        
    }
    
    public static PseudoDao getInstance() {
        return instance;
    }
    
    public void addRegistration(String regId, String accountName) {
        synchronized(mRegisteredUsers) {
            if (!mRegisteredUsers.contains(regId)) {
                mRegisteredUsers.add(regId);
            }
            if (accountName != null) {
                List<String> regIdList = mUserMap.get(accountName);
                if (regIdList == null) {
                    regIdList = new ArrayList<String>();
                    mUserMap.put(accountName, regIdList);
                }
                if (!regIdList.contains(regId)) {
                    regIdList.add(regId);
                }
            }
        }
    }
    
    public List<String> getAllRegistrationIds() {
        return Collections.unmodifiableList(mRegisteredUsers);
    }
    
    public List<String> getAllRegistrationIdsForAccount(String account) {
        List<String> regIds = mUserMap.get(account);
        if (regIds != null) {
           return Collections.unmodifiableList(regIds);
        }
        return null;
    }
    
    public String getNotificationKeyName(String accountName) {
        return mNotificationKeyMap.get(accountName);
    }
    
    public void storeNotificationKeyName(String accountName, String notificationKeyName) {
        mNotificationKeyMap.put(accountName, notificationKeyName);
    }
    
    public Set<String> getAccounts() {
        return Collections.unmodifiableSet(mUserMap.keySet());
    }
    
    public String getUniqueMessageId() {
        int nextRandom = sRandom.nextInt();
        while (mMessageIds.contains(nextRandom)) {
            nextRandom = sRandom.nextInt();
        }
        return Integer.toString(nextRandom);
    }
}
