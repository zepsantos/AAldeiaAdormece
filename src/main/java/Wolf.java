import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.PrivateChannel;

import java.util.stream.Collectors;

public class Wolf extends Player {

    public Wolf(User discordUser) {
        super(discordUser);
    }

    @Override
    void advertiseRole() {
        PrivateChannel channel = this.getDiscordUser().getPrivateChannel().block();
        if(channel != null)
        channel.createMessage("Wolf").block();
    }

    @Override
    void doRolePapel(Game game,RolePapelFinishCallback callback) {
        PrivateChannel channel = this.getDiscordUser().getPrivateChannel().block();
        if(channel != null){
            DiscordPlayerVote discordPlayerVote = new DiscordPlayerVote(game.playersAlive().stream().filter(p -> !(p instanceof Wolf)).collect(Collectors.toList()), channel, new RoleReact());
            discordPlayerVote.broadcastPlayersList();
            discordPlayerVote.reactToPlayerDecision(callback);
        }

    }

}
