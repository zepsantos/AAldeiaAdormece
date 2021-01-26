import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.Reaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VoteKickReact  implements  IDiscordPlayerReactions{
    private int numberOfPlayersAlive;
    public VoteKickReact(int numberOfPlayersAlive) {
        this.numberOfPlayersAlive = numberOfPlayersAlive;
    }
    @Override
    public void react(MessageChannel channel, Snowflake lastMessageID, RolePapelFinishCallback callback) { //TODO: ONLY 1 reaction per user
        PoolVote poolVote = new PoolVote();
        int numReactions = 0;
        while(numReactions != numberOfPlayersAlive) {
            numReactions = 0;
            List<Message> tmp = channel.getMessagesAfter(lastMessageID).buffer().blockLast();
            if (tmp != null) {
                for (Message message : tmp) {
                    Set<Reaction> reactions = message.getReactions();
                    if(!reactions.isEmpty()) {
                        poolVote.setVotePlayer(message.getContent(),reactions.size());
                    }
                    numReactions += reactions.size();
                }
            }
        }


    }
}
