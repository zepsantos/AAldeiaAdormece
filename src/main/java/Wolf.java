import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.discordjson.json.MessageData;
import reactor.core.publisher.Mono;

public class Wolf extends Player {

    public Wolf(User discordUser) {
        super(discordUser);
        advertiseWolf();
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

            callback.onComplete();
        }

    }


    private void advertiseWolf() {
        User tmp = getDiscordUser();

    }
}
