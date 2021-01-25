import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.PrivateChannel;

import java.util.List;

public class DiscordRespondToRole {
    PrivateChannel channel;
    List<Player> playersListToBroadcast;
    Snowflake lastMessageID;
    public DiscordRespondToRole(List<Player> playersListToBroadcast, PrivateChannel channel) {
        this.playersListToBroadcast = playersListToBroadcast;
        this.channel = channel;
        lastMessageID = channel.getLastMessageId().isPresent()?channel.getLastMessageId().get():null;
        broadcastPlayersList();
    }

    private void broadcastPlayersList() {
        for(Player p : playersListToBroadcast) {
            channel.createMessage(p.getDiscordUser().getUsername());
        }
    }
}
