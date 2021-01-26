import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Map<String,User> playersList;
    private boolean gameStarted = false;
    private DiscordClient client;
    private MessageChannel channel;
    private static final int numOfTypesOfCharacters = 2;
    private Map<String,Player> playersOnGame;
    private String lastPlayerKilled;
    private boolean playerSaved = false;
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
        playersOnGame.put(wolf.getID(),wolf);
        if(userList.size() > 1) {
            Player healer = new Healer(userList.get(randomList.get(1)));
            playersOnGame.put(healer.getID(), healer);
        }

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
        while(gameStarted) {
            playRound();
            if(checkGameStatus()) break;
            voteForWolf();
            if(checkGameStatus()) break;
        }
        advertiseEndGame();

    }


    private void voteForWolf() {
       channel.createMessage("----------------//////////////////////////////VOTACAO/////////////////////-------------------").block();
       DiscordPlayerVote playerVote = new DiscordPlayerVote(playersAlive(),channel,new VoteKickReact(playersAlive().size()));
       playerVote.broadcastPlayersList();
       playerVote.reactToPlayerDecision(playerName -> {
           killPlayer(playerName);
           channel.createMessage("O Jogador " + playerName + " foi morto!").block();
       });

    }

    private void advertiseEndGame() {
        if(getPlayerType(Wolf.class).isAlive())
        channel.createMessage("O Lobo venceu").block();
        else
        channel.createMessage("Os aldeoes venceram").block();
        this.gameStarted = false;
    }

    private boolean checkGameStatus() {
        int nofPlayers = playersAlive().size();
        this.gameStarted = (!wolfAlive() && nofPlayers > 1);
       return gameStarted;
    }

    private void playRound() {
        channel.createMessage("A aldeia adormece...").block();
        channel.createMessage("O lobo acorda e escolhe uma pessoa para matar").block();
        executeWolfRole();
        channel.createMessage("O lobo adormece...").block();
        channel.createMessage("O healer acorda e escolhe uma pessoa para salvar").block();
        if(healerInGame()) {
            executeHealerRole();
        }
        if(this.playerSaved) {
            channel.createMessage("A aldeia acorda com a feliz noticia de que ninguem morreu").block();
        } else {
            channel.createMessage("A aldeia acorda com a infeliz noticia de que " + this.lastPlayerKilled + " morreu").block();
        }
        clearDead();
    }

    private void clearDead() {
        for(Map.Entry<String,Player> entry : this.playersOnGame.entrySet()) {
            if(!entry.getValue().isAlive()) this.playersOnGame.remove(entry.getKey(),entry.getValue());
        }
    }


    public boolean isGameStarted() {
        return gameStarted;
    }

    public Player getPlayerType(Class<? extends Player> playerClass) {
        for(Player tmp : this.playersOnGame.values()) {
            if(playerClass.isInstance(tmp)) return tmp;
        }
        return null;
    }

    private void advertiseRoles() {
        for(Player tmp : this.playersOnGame.values()) {
            tmp.advertiseRole();
        }
    }

    private void executeHealerRole() {
        getPlayerType(Healer.class).doRolePapel(this, this::savePlayer);
    }

    private void executeWolfRole() {
        getPlayerType(Wolf.class).doRolePapel(this, this::killPlayer);
    }


    /** SO permite uma healer **/
    private void savePlayer(String playerName) {
        for(Player p : this.playersOnGame.values()) {
            if(p.getDiscordUser().getUsername().equals(playerName)) {
                this.playerSaved = true;
                p.setAlive();
                return;
            }
        }
    }


    private void killPlayer(String playerName) {
        for(Player p : this.playersOnGame.values()) {
            if(p.getDiscordUser().getUsername().equals(playerName)){
                lastPlayerKilled = playerName;
                p.setKilled();
                return;
            }
        }
    }



    public List<Player> playersAlive() {
        return this.playersOnGame.values().stream().filter(Player::isAlive).collect(Collectors.toList());
    }

    public List<Player> getPlayersOnGame() {
        return new ArrayList<>(playersOnGame.values());
    }

    private boolean healerInGame() {
        return this.playersOnGame.values().stream().anyMatch(h -> h instanceof Healer);
    }

    private boolean wolfAlive() {
        return playersAlive().stream().anyMatch(h -> h instanceof Wolf);
    }
}
