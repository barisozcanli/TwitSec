package com.peace.twitsec.app.common;

import java.text.MessageFormat;

public enum ErrorCode {

	USER_NOT_FOUND("User not found", "100"),
	DUPLICATE_USER("There is already a user with given username", "101"),
	OPERATION_NOT_ALLOWED("Operation is not allowed for not authenticated users", "102"),

	DUPLICATE_GROUP("There is already a group with given name", "103"),
	GROUP_NOT_FOUND("Group not found", "104"),
	
	MEETING_NOT_FOUND("Meeting not found", "105"),
	INVALID_INPUT("Invalid input-> {0}", "106"),
	
	USER_IS_ALREADY_A_MEMBER("User is already member of this group", "107"),
	USER_IS_NOT_A_MEMBER("User is not a  member of this group", "108"),
	USER_IS_BLOCKED("User is blocked for this group", "109"),
	
	DUPLICATE_ROLE("There is already a role with given name", "110"),
	ROLE_NOT_FOUND("Role not found", "111"),
	SOME_ROLES_NOT_FOUND("Some roles could not be found", "112"),
	
	THERE_IS_NO_INVITED_PERSON("There is no invited person to the meeting", "113"),
	NOT_INVITED_TO_MEETING("You are not invited to the meeting", "114"),
	ALREADY_ACCEPTED_INVITATION("You have accepted meeting invitation already", "115"),
	ALREADY_REJECTED_INVITATION("You have rejected meeting invitation already", "116"),
	ALREADY_MARKED_AS_TENTATIVE_INVITATION("You have marked meeting invitation as tentative already", "117"),

	RESOURCE_NOT_FOUND("Resource not found", "118"),
	
	DISCUSSION_NOT_FOUND("Discussion not found", "130"),
	
	MEETING_PROPOSAL_NOT_FOUND("Discussion not found", "140"),
	
	ERROR_WHILE_SAVING_IMAGE("Error occured while saving image {0}", "150"),
	ERROR_WHILE_READING_IMAGE("Error occured while reading image {0}", "151"),

	COMMENT_NOT_FOUND("Discussion not found", "160"),
	
	NOTE_NOT_FOUND("Note not found", "119"),
	
	MESSAGE_NOT_FOUND("Message not found", "170"),

	PERMISSION_NOT_FOUND("You don't have permission for : ", "180"),
	
	TAG_CLASS_NOT_FOUND("Tag.class information does not recognized", "190"),
	TAG_NOT_FOUND("No tag found", "191"),
	
	DUPLICATE_MEETING_DISCUSSION("Duplicate meeting discussion record", "200"),
	MEETING_DISCUSSION_NOT_FOUND("Entity relation not found", "201"),

	INTERNAL_SERVER_ERROR("Internal Server Error", "900")
	;

	private String message;
	private String code;
	
	private ErrorCode(String message, String errorCode) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String format(Object... args){
		return MessageFormat.format(message, args);
	}
}
