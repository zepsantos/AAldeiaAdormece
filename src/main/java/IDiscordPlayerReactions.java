import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.MessageChannel;

public interface IDiscordPlayerReactions {
    void react(MessageChannel channel , Snowflake lastMessageID, RolePapelFinishCallback callback);
}
