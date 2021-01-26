import com.iwebpp.crypto.TweetNaclFast;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.reaction.Reaction;

import java.util.*;
import java.util.function.Function;

public class DiscordPlayerVote {
    private MessageChannel channel;
    private List<Player> playersListToBroadcast;
    private Snowflake lastMessageID;
    private IDiscordPlayerReactions reaction;
    public DiscordPlayerVote(List<Player> playersListToBroadcast, MessageChannel channel, IDiscordPlayerReactions reaction) {
        this.playersListToBroadcast = playersListToBroadcast;
        this.channel = channel;
        lastMessageID = channel.getLastMessageId().isPresent()?channel.getLastMessageId().get():null;
        this.reaction = reaction;
    }

    public void broadcastPlayersList() {
        for(Player p : playersListToBroadcast) {
            channel.createMessage(p.getDiscordUser().getUsername()).block();
        }
    }

    public void reactToPlayerDecision(RolePapelFinishCallback callback) {
        reaction.react(channel,lastMessageID,callback);
    }
}
