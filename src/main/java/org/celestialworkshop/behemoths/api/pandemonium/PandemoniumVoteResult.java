package org.celestialworkshop.behemoths.api.pandemonium;

import net.minecraft.network.chat.Component;

public class PandemoniumVoteResult {
    private final boolean success;
    private final Component message;

    private PandemoniumVoteResult(boolean success, Component message) {
        this.success = success;
        this.message = message;
    }

    public static PandemoniumVoteResult success(Component message) {
        return new PandemoniumVoteResult(true, message);
    }

    public static PandemoniumVoteResult fail(Component message) {
        return new PandemoniumVoteResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public Component getMessage() {
        return message;
    }
}
