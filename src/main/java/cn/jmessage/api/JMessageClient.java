package cn.jmessage.api;

import cn.jiguang.common.connection.HttpProxy;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.common.JMessageConfig;
import cn.jmessage.api.common.model.*;
import cn.jmessage.api.group.CreateGroupResult;
import cn.jmessage.api.group.GroupClient;
import cn.jmessage.api.group.GroupInfoResult;
import cn.jmessage.api.group.GroupListResult;
import cn.jmessage.api.group.MemberListResult;
import cn.jmessage.api.message.MessageClient;
import cn.jmessage.api.message.MessageListResult;
import cn.jmessage.api.message.SendMessageResult;
import cn.jmessage.api.user.*;

public class JMessageClient {

    private final UserClient _userClient;
    private final GroupClient _groupClient;
    private final MessageClient _messageClient;
    private final int _sendVersion;

    /**
     * Create a JMessage Client.
     *
     * @param appkey The KEY of one application on JPush.
     * @param masterSecret API access secret of the appKey.
     */
    public JMessageClient(String appkey, String masterSecret) {
        this(appkey, masterSecret, null, JMessageConfig.getInstance());
    }

    /**
     * Create a JMessage Client with custom maxRetryTimes.
     *
     * @param appkey The KEY of one application on JPush.
     * @param masterSecret API access secret of the appKey.
     * @param maxRetryTimes The max retry times.
     */
    public JMessageClient(String appkey, String masterSecret, int maxRetryTimes) {
        this(appkey, masterSecret, null, JMessageConfig.getInstance().setMaxRetryTimes(maxRetryTimes));
    }

    /**
     * Create a JMessage Client with a proxy.
     *
     * @param appkey The KEY of one application on JPush.
     * @param masterSecret API access secret of the appKey.
     * @param proxy The proxy, if there is no proxy, should be null.
     */
    public JMessageClient(String appkey, String masterSecret, HttpProxy proxy) {
        this(appkey, masterSecret, proxy, JMessageConfig.getInstance());
    }

    /**
     * Create a JMessage Client with a custom hostname.
     * If you are using JPush/JMessage privacy cloud, maybe this constructor is what you needed.
     *
     * @param appkey The KEY of one application on JPush.
     * @param masterSecret API access secret of the appKey.
     * @param hostname The custom hostname.
     */
    public JMessageClient(String appkey, String masterSecret, String hostname) {
        this(appkey, masterSecret, null, JMessageConfig.getInstance().setApiHostName(hostname));
    }


    /**
     * Create a JMessage Client by custom Client configuration.
     * If you are using custom parameters, maybe this constructor is what you needed.
     *
     * @param appkey The KEY of one application on JPush.
     * @param masterSecret API access secret of the appKey.
     * @param proxy The proxy, if there is no proxy, should be null.
     * @param config The client configuration. Can use JMessageConfig.getInstance() as default.
     */
    public JMessageClient(String appkey, String masterSecret, HttpProxy proxy, JMessageConfig config) {
        _userClient = new UserClient(appkey, masterSecret, proxy, config);
        _groupClient = new GroupClient(appkey, masterSecret, proxy, config);
        _messageClient = new MessageClient(appkey, masterSecret, proxy, config);
        _sendVersion = (Integer) config.get(JMessageConfig.SEND_VERSION);
    }

    public String registerUsers(RegisterInfo[] users)
            throws APIConnectionException, APIRequestException {
        RegisterPayload payload = RegisterPayload.newBuilder()
                .addUsers(users)
                .build();

        return _userClient.registerUsers(payload).responseContent;
    }

    public String registerAdmins(String username, String password)
            throws APIConnectionException, APIRequestException {
        RegisterInfo payload = RegisterInfo.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        return _userClient.registerAdmins(payload).responseContent;
    }

    public UserInfoResult getUserInfo(String username)
            throws APIConnectionException, APIRequestException {
        return _userClient.getUserInfo(username);
    }

    public UserStateResult getUserState(String username)
            throws APIConnectionException, APIRequestException {
        return _userClient.getUserState(username);
    }

    public void updateUserPassword(String username, String password)
            throws APIConnectionException, APIRequestException {
        _userClient.updatePassword(username, password);
    }

    public void updateUserInfo(String username, String nickname, String birthday, String signature, int gender,
                               String region, String address)
            throws APIConnectionException, APIRequestException {
        UserPayload payload = UserPayload.newBuilder()
                .setNickname(nickname)
                .setBirthday(birthday)
                .setSignature(signature)
                .setGender(gender)
                .setRegion(region)
                .setAddress(address)
                .build();

        _userClient.updateUserInfo(username, payload);
    }

    /**
     * Get user list
     * @param start The start index of the list
     * @param count The number that how many you want to get from list
     * @return User info list
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public UserListResult getUserList(int start, int count)
            throws APIConnectionException, APIRequestException {
        return _userClient.getUserList(start, count);
    }
    
    /**
     * Get admins by appkey
     * @param start The start index of the list
     * @param count The number that how many you want to get from list
     * @return admin user info list
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public UserListResult getAdminListByAppkey(int start, int count)
    		throws APIConnectionException, APIRequestException {
    	return _userClient.getAdminListByAppkey(start, count);
    }

    /**
     * Get a user's all black list
     * @param username The owner of the black list
     * @return user info list
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public UserInfoResult[] getBlackList(String username)
            throws APIConnectionException, APIRequestException {
        return _userClient.getBlackList(username);
    }

    /**
     * Add Users to black list
     * @param username The owner of the black list
     * @param users The users that will add to black list
     * @return add users to black list
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public ResponseWrapper addBlackList(String username, String...users)
            throws APIConnectionException, APIRequestException {
        return _userClient.addBlackList(username, users);
    }

    /**
     * Get all groups of a user
     * @param username Necessary
     * @return Group info list
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public UserGroupsResult getGroupListByUser(String username)
            throws APIConnectionException, APIRequestException {
        return _userClient.getGroupList(username);
    }

    public void deleteUser(String username)
            throws APIConnectionException, APIRequestException {
        _userClient.deleteUser(username);
    }


    public GroupInfoResult getGroupInfo(long gid)
            throws APIConnectionException, APIRequestException {
        return _groupClient.getGroupInfo(gid);
    }

    public MemberListResult getGroupMembers(long gid)
            throws APIConnectionException, APIRequestException {
        return _groupClient.getGroupMembers(gid);
    }

    public GroupListResult getGroupListByAppkey(int start, int count)
            throws APIConnectionException, APIRequestException {
        return _groupClient.getGroupListByAppkey(start, count);
    }

    public CreateGroupResult createGroup(String owner, String gname, String desc, String... userlist)
            throws APIConnectionException, APIRequestException {
        Members members = Members.newBuilder().addMember(userlist).build();

        GroupPayload payload = GroupPayload.newBuilder()
                .setOwner(owner)
                .setName(gname)
                .setDesc(desc)
                .setMembers(members)
                .build();

        return _groupClient.createGroup(payload);
    }

    /**
     * Add or remove members from a group
     * @param gid The group id
     * @param addList If this parameter is null then send remove request
     * @param removeList If this parameter is null then send add request
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public void addOrRemoveMembers(long gid, String[] addList, String[] removeList)
            throws APIConnectionException, APIRequestException {
        Members add = Members.newBuilder()
                .addMember(addList)
                .build();

        Members remove = Members.newBuilder()
                .addMember(removeList)
                .build();

        _groupClient.addOrRemoveMembers(gid, add, remove);
    }

    public void deleteGroup(long gid)
            throws APIConnectionException, APIRequestException {
        _groupClient.deleteGroup(gid);
    }

    public void updateGroupInfo(long gid, String groupName, String groupDesc)
            throws APIConnectionException, APIRequestException {
        _groupClient.updateGroupInfo(gid, groupName, groupDesc);
    }

    /**
     * Send message
     * @param version Current version is 1
     * @param targetType Group or single
     * @param targetId The message receiver 
     * @param fromType Only support admin now
     * @param fromId Sender
     * @param messageType Only support text now
     * @param messageBody A MessageBody instance
     * @return return msg_id
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public SendMessageResult sendMessage(Integer version, String targetType, String targetId,
                            String fromType, String fromId, String messageType, MessageBody messageBody)
            throws APIConnectionException, APIRequestException {
        MessagePayload payload = MessagePayload.newBuilder()
                .setVersion(version)
                .setTargetType(targetType)
                .setTargetId(targetId)
                .setFromType(fromType)
                .setFromId(fromId)
                .setMessageType(messageType)
                .setMessageBody(messageBody)
                .build();
        return _messageClient.sendMessage(payload);
    }

    /**
     * Send single text message by admin
     * @param targetId target user's id
     * @param fromId sender's id
     * @param body message body, include text and extra(if needed).
     * @return return msg_id
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public SendMessageResult sendSingleTextByAdmin(String targetId, String fromId, MessageBody body)
            throws APIConnectionException, APIRequestException {
        return sendMessage(_sendVersion, "single", targetId, "admin",fromId, "text", body);
    }

    /**
     * Send group text message by admin
     * @param targetId target user's id
     * @param fromId sender's id
     * @param body message body, include text and extra(if needed).
     * @return return msg_id
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public SendMessageResult sendGroupTextByAdmin(String targetId, String fromId, MessageBody body)
            throws APIConnectionException, APIRequestException {
        return sendMessage(_sendVersion, "group", targetId, "admin",fromId, "text", body);
    }

    /**
     * Get message list from history, messages will store 60 days.
     * @param count Necessary parameter. The count of the message list.
     * @param begin_time Necessary parameter. The format must follow by 'yyyy-MM-dd HH:mm:ss'
     * @param end_time Necessary parameter. The format must follow by 'yyyy-MM-dd HH:mm:ss'
     * @return MessageListResult
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public MessageListResult getMessageList(int count, String begin_time, String end_time)
            throws APIConnectionException, APIRequestException {
        return _messageClient.getMessageList(count, begin_time, end_time);
    }

    public MessageListResult getMessageListByCursor(String cursor)
            throws APIConnectionException, APIRequestException {
        return _messageClient.getMessageListByCursor(cursor);
    }

    /**
     * Get message list from user's record, messages will store 60 days.
     * @param username Necessary parameter.
     * @param count Necessary parameter. The count of the message list.
     * @param begin_time Optional parameter. The format must follow by 'yyyy-MM-dd HH:mm:ss'
     * @param end_time Optional parameter. The format must follow by 'yyyy-MM-dd HH:mm:ss'
     * @return MessageListResult
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public MessageListResult getUserMessages(String username, int count, String begin_time, String end_time)
            throws APIConnectionException, APIRequestException {
        return _messageClient.getUserMessages(username, count, begin_time, end_time);
    }

    /**
     * Get user's message list with cursor, the cursor will effective in 120 seconds.
     * And will return same count of messages as first request.
     * @param username Necessary parameter.
     * @param cursor First request will return cursor
     * @return MessageListResult
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public MessageListResult getUserMessagesByCursor(String username, String cursor)
            throws APIConnectionException, APIRequestException {
        return _messageClient.getUserMessagesByCursor(username, cursor);
    }

    /**
     * Set don't disturb service while receiving messages.
     * You can Add or remove single conversation or group conversation
     * @param username Necessary
     * @param payload NoDisturbPayload
     * @return No content
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public ResponseWrapper setNoDisturb(String username, NoDisturbPayload payload)
            throws APIConnectionException, APIRequestException {
        return _userClient.setNoDisturb(username, payload);
    }

    /**
     * Add friends to username
     * @param username Necessary
     * @param users username to be add
     * @return No content
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public ResponseWrapper addFriends(String username, String...users)
            throws APIConnectionException, APIRequestException {
        return _userClient.addFriends(username, users);
    }

    /**
     * Delete friends
     * @param username Friends you want to delete to
     * @param users username to be delete
     * @return No content
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public ResponseWrapper deleteFriends(String username, String...users)
            throws APIConnectionException, APIRequestException {
        return _userClient.deleteFriends(username, users);
    }

    /**
     * Update friends' note information. The size is limit to 500.
     * @param username Necessary
     * @param array FriendNote array
     * @return No content
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public ResponseWrapper updateFriendsNote(String username, FriendNote[] array)
            throws APIConnectionException, APIRequestException {
        return _userClient.updateFriendsNote(username, array);
    }

    /**
     * Get all friends'info from a gived username
     * @param username Necessary
     * @return UserList
     * @throws APIConnectionException connect exception
     * @throws APIRequestException request exception
     */
    public UserInfoResult[] getFriendsInfo(String username)
            throws APIConnectionException, APIRequestException {
        return _userClient.getFriendsInfo(username);
    }

}
