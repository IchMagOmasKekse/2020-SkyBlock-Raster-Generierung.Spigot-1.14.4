package me.crafttale.de.requests;

import org.bukkit.entity.Player;

import me.crafttale.de.Chat;
import me.crafttale.de.Prefixes;
import me.crafttale.de.commands.SkyBlockCommandFunction;

public class JoinIslandRequest extends Request {
	
	public JoinIslandRequest(Player sender, Player receiver) {
		super(sender, receiver, RequestType.VISITING);
		sender.sendMessage(Prefixes.REQUEST.px()+"Deine Anfrage wurde verschickt. Diese gilt für §7"+(timeLeft/20/60)+" Minuten");
		receiver.sendMessage(Prefixes.REQUEST.px()+"§e"+sender.getName()+" §bmöchte deine Insel besuchen");
		Chat.sendClickableVisitingMessage(receiver,
				Prefixes.REQUEST.px()+"Nutze §7/accept §bzum §aannehmen",
				"§bKlicke um §aakzeptieren",
				"/accept",
				false,
				false);
		Chat.sendClickableVisitingMessage(receiver,
				Prefixes.REQUEST.px()+"Nutze §7/deny §bzum §cablehnen",
				"§bKlicke um §cablehnen",
				"/deny",
				false,
				false);
		registerRequest(this);
	}

	@Override
	public boolean accept() {
		this.hasAccepted = true;
		sender.sendMessage(Prefixes.REQUEST.px()+"§e"+receiver.getName()+" §bhat deine Anfrage §aangenommen");
		receiver.sendMessage(Prefixes.REQUEST.px()+"Du hast die Anfrage von §e"+sender.getName()+" §aangenommen");
		SkyBlockCommandFunction.visitIsland(sender, receiver);
		RequestManager.removeRequest(this);
		return false;
	}

	@Override
	public boolean deny() {
		this.hasDenied = true;
		sender.sendMessage(Prefixes.REQUEST.px()+"§e"+receiver.getName()+" §bhat deine Anfrage §cabgelehnt");
		receiver.sendMessage(Prefixes.REQUEST.px()+"Du hast die Anfrage von §e"+sender.getName()+" §cabgelehnt");
		RequestManager.removeRequest(this);
		return false;
	}

}
