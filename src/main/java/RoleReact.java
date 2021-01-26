import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.Reaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoleReact implements  IDiscordPlayerReactions{
    @Override
    public void react(MessageChannel channel , Snowflake lastMessageID, RolePapelFinishCallback callback) {
        Map<Message, Set<Reaction>> reactionMap = new HashMap<>();
        int numReactions = 0;
        while(reactionMap.isEmpty()) {
            numReactions = 0;
            List<Message> tmp = channel.getMessagesAfter(lastMessageID).buffer().blockLast();
            if (tmp != null) {
                for (Message message : tmp) {
                    Set<Reaction> reactions = message.getReactions();
                    if(!reactions.isEmpty())
                        reactionMap.put(message, reactions);
                    numReactions += reactions.size();
                }
            }
            if (numReactions == 1) {
                for (Map.Entry<Message, Set<Reaction>> entry : reactionMap.entrySet()) {
                    String userToReact = entry.getKey().getContent();
                    callback.onComplete(userToReact);
                }
            } else {

            }
        }

    }
}
