import discord4j.core.object.entity.User;

public abstract class Player {
    private User discordUser;
    private boolean alive = true;



    public Player(User discordUser) {
        this.discordUser = discordUser;
    }
    abstract void advertiseRole();
    abstract void doRolePapel(Game game, RolePapelFinishCallback callback);
    public void voteFor() {

    }

    public User getDiscordUser() {
        return discordUser;
    }

    public String getID() {
        return this.discordUser.getId().asString();
    }

    public boolean isAlive() {
        return this.alive;
    }
}
