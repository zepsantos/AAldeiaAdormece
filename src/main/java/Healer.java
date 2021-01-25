import discord4j.core.object.entity.Message;
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
            channel.createMessage("Villager").block();
    }

    @Override
    void doRolePapel(Game game, RolePapelFinishCallback callback) {

    }


}
