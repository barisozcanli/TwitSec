package com.peace.twitsec.data.enums;

public enum FollowAction {

    FOLLOWED(0),
    UNFOLLOWED(1);

    private int value;

    private FollowAction(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
