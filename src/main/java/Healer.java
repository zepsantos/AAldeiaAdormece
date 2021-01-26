import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.PrivateChannel;

public class Healer extends Player {

    public Healer(User discordUser) {
        super(discordUser);
    }

    @Override
    void advertiseRole() {
        PrivateChannel channel = this.getDiscordUser().getPrivateChannel().block();
        if(channel != null)
            channel.createMessage("Healer").block();
    }

    @Override
    void doRolePapel(Game game, RolePapelFinishCallback callback) {
        PrivateChannel channel = this.getDiscordUser().getPrivateChannel().block();
        if(channel != null){
            DiscordPlayerVote discordPlayerVote = new DiscordPlayerVote(game.getPlayersOnGame(), channel, new RoleReact());
            discordPlayerVote.broadcastPlayersList();
            discordPlayerVote.reactToPlayerDecision(callback);
        }
    }


}
