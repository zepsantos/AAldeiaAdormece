import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.HashMap;
import java.util.Map;

public class App {
    private static final Map<String, Command> commands = new HashMap<>();
    private static Game game;
    private static final String GAMECHANNELID = "802709365806465044";
    public static void main(String[] args) {

        commands.put("ping", new Command() {
            @Override
            public void execute(MessageCreateEvent event) {
                event.getMessage().getChannel().block().createMessage("Pong!").block();
            }
        } );


        commands.put("ready", new Command() {
            @Override
            public void execute(MessageCreateEvent event) {
                MessageChannel channel = event.getMessage().getChannel().block();
                if(channel == null) return;
                if(checkOnGameChannel(channel.getId().asString())) {
                    if(event.getMessage().getAuthor().isPresent() && !game.isGameStarted()) {
                        User user = event.getMessage().getAuthor().get();
                        game.addPlayer(user);
                        channel.createMessage("Added to game player " + user.getUsername().toString() + "!").block();
                    } else {
                        if(game.isGameStarted()) channel.createMessage("Game is already started");
                        channel.createMessage("Failed to add player to the game!").block();
                    }
                }
            }
        } );

        commands.put("start", new Command() {
            @Override
            public void execute(MessageCreateEvent event) {
                Message message = event.getMessage();
                MessageChannel channel = message.getChannel().block();
                if(channel == null) return;
                if(checkOnGameChannel(channel.getId().asString())) {
                    game.initGame(channel,message.getGuild().block());
                }
            }
        } );
        final GatewayDiscordClient client = DiscordClientBuilder.create(args[0]).build()
                .login()
                .block();
        if(client != null) {
            game = new Game(client.rest());
            client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
                final String content = messageCreateEvent.getMessage().getContent();
                for (final Map.Entry<String, Command> entry : commands.entrySet()) {
                    // We will be using ! as our "prefix" to any command in the system.
                    if (content.startsWith('!' + entry.getKey())) {
                        entry.getValue().execute(messageCreateEvent);
                        break;
                    }
                }
            });
            client.onDisconnect().block();
        }
    }


    private static boolean checkOnGameChannel(String channelID) {
        return channelID.equals(GAMECHANNELID);
    }
}
