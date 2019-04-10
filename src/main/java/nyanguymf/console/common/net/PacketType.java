package nyanguymf.console.common.net;

import java.io.Serializable;

public enum PacketType implements Serializable {
    REMOTE_COMMAND,
    INVALID_CREDENTIALS,
    INFO,
    LOG_ENABLE,
    LOG_DISABLE,
    LOG_MESSAGE;
}
