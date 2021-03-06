package dev._2lstudios.advancedauth.commands.player;

import dev._2lstudios.advancedauth.Logging;
import dev._2lstudios.advancedauth.commands.Argument;
import dev._2lstudios.advancedauth.commands.Command;
import dev._2lstudios.advancedauth.commands.CommandContext;
import dev._2lstudios.advancedauth.commands.CommandListener;
import dev._2lstudios.advancedauth.players.AuthPlayer;
import dev._2lstudios.advancedauth.players.LoginReason;

@Command(
    name = "login",
    arguments = { Argument.STRING },
    minArguments = 1,
    requireAuth = false,
    silent = true
)
public class LoginCommand extends CommandListener {
    @Override
    public void onExecuteByPlayer(CommandContext ctx) {
        AuthPlayer player = ctx.getPlayer();
        String password = ctx.getArguments().getString(0);
        
        if (!player.isRegistered()) {
            player.sendI18nMessage("register.not-registered");
            return;
        }

        if (player.isLogged() || player.isGuest()) {
            player.sendI18nMessage("login.already-logged");
            return;
        }

        if (player.comparePassword(password)) {
            player.login(LoginReason.PASSWORD);
            Logging.info(player.getName() + " has been logged in using a password.");
        } else {
            this.plugin.getFailLock().handleFail(player.getAddress());
            int tries = this.plugin.getFailLock().getTries(player.getAddress());
            int maxTries = this.plugin.getConfig().getInt("security.fail-lock.tries");

            String msg = player.getI18nMessage("login.wrong-password")
                .replace("{tries}", tries + "")
                .replace("{max-tries}", maxTries + "");

            if (this.plugin.getConfig().getBoolean("authentication.kick", false)) {
                player.sendMessage(msg);
            } else {
                player.kick(msg);
            }

            Logging.info(player.getName() + " password failed (" + tries + "/" + maxTries + ")");
        }
    }
}
