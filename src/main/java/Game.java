import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.discordjson.json.ChannelData;
import discord4j.discordjson.json.GatewayData;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Map<String,User> playersList;
    private boolean gameStarted = false;
    private DiscordClient client;
    private MessageChannel channel;
    private static final int numOfTypesOfCharacters = 1;
    private Map<String,Player> playersOnGame;
    private Guild guild;
    public Game(DiscordClient client) {
        this.playersList = new HashMap<>();
        this.playersOnGame = new HashMap<>();
        this.client = client;
    }
    public void addPlayer(User player) {
        this.playersList.put(player.getId().asString(),player);
    }


    public void initGame(MessageChannel channel,Guild guild) {
        this.guild = guild;
        this.channel = channel;
        List<Integer> randomList = MultipleRandomNumber.getRandomNumbers(numOfTypesOfCharacters,playersList.size());
        List<User> userList = new ArrayList<>(playersList.values());
        Player wolf = new Wolf(userList.get(randomList.get(0))); //TODO: SER RESPONSIVO TENDO EM CONTA O N DE TIPOS DE PERSONAGENS
       // Player healer = new Healer(userList.get(randomList.get(1)));
        playersOnGame.put(wolf.getID(),wolf);
       // playersOnGame.put(healer.getID(),healer);
        for(int i = 0; i<userList.size(); i++) {
            if(randomList.contains(i)) continue;
            User tmp = userList.get(i);
            playersOnGame.put(tmp.getId().asString(),new Villager(tmp));
        }
        startGame();
    }



    public void startGame() {
        this.gameStarted = true;
        advertiseRoles();
        channel.createMessage("A aldeia adormece...").block();
        channel.createMessage("O lobo acorda e escolhe uma pessoa para matar").block();
        executeWolfRole();
    }


    public boolean isGameStarted() {
        return gameStarted;
    }

    public Player getWolf() {
        for(Player tmp : this.playersOnGame.values()) {
            if(tmp instanceof  Wolf) return tmp;
        }
        return null;
    }

    private void advertiseRoles() {
        for(Player tmp : this.playersOnGame.values()) {
            tmp.advertiseRole();
        }
    }

    private void executeWolfRole() {
        getWolf().doRolePapel(this, new RolePapelFinishCallback() {
            @Override
            public void onComplete() {

            }
        });
    }



    public List<Player> playersAlive() {
        return this.playersOnGame.values().stream().filter(p -> p.isAlive()).collect(Collectors.toList());
    }
}
