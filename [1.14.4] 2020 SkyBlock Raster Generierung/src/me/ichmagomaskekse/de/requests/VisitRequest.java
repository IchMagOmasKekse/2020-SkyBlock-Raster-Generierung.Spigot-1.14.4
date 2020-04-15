package me.ichmagomaskekse.de.requests;

import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.commands.CommandFunction;

public class VisitRequest extends Request {
	
	public VisitRequest(Player sender, Player receiver) {
		super(sender, receiver, RequestType.VISITING);
		registerRequest(this);
		sender.sendMessage(Prefixes.REQUEST.getPrefix()+"Deine Anfrage wurde verschickt. Diese gilt für §75 Minuten");
		receiver.sendMessage(Prefixes.REQUEST.getPrefix()+"§e"+sender.getName()+" §bmöchte deine Insel besuchen");
		receiver.sendMessage(Prefixes.REQUEST.getPrefix()+"Nutze §7/accept §bzum §aannehmen §boder §7/deny §bzum §cablehnen");
	}

	@Override
	public boolean accept() {
		this.hasAccepted = true;
		sender.sendMessage(Prefixes.REQUEST.getPrefix()+"§e"+receiver.getName()+" §bhat deine Anfrage §aangenommen");
		receiver.sendMessage(Prefixes.REQUEST.getPrefix()+"Du hast die Anfrage von §e"+sender.getName()+" §aangenommen");
		CommandFunction.visitIsland(sender, receiver);
		RequestManager.removeRequest(this);
		return false;
	}

	@Override
	public boolean deny() {
		this.hasDenied = true;
		sender.sendMessage(Prefixes.REQUEST.getPrefix()+"§e"+receiver.getName()+" §bhat deine Anfrage §cabgelehnt");
		receiver.sendMessage(Prefixes.REQUEST.getPrefix()+"Du hast die Anfrage von §e"+sender.getName()+" §cabgeehnt");
		RequestManager.removeRequest(this);
		return false;
	}

}
