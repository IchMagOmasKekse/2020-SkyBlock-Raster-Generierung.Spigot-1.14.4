package me.crafttale.de.requests;

import org.bukkit.entity.Player;

import me.crafttale.de.Chat;
import me.crafttale.de.Prefixes;
import me.crafttale.de.commands.SkyBlockCommandFunction;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.gui.island.IslandVisitGUI;
import me.crafttale.de.gui.island.VisitAcceptedGUI;
import me.crafttale.de.gui.island.VisitDeniedGUI;

public class VisitRequest extends Request {
	
	public VisitRequest(Player sender, Player receiver) {
		super(sender, receiver, RequestType.VISITING);
		sender.sendMessage(Prefixes.REQUEST.px()+"Deine Anfrage wurde verschickt. Diese gilt f�r �7"+(timeLeft/20/60)+" Minuten");
		receiver.sendMessage(Prefixes.REQUEST.px()+"�e"+sender.getName()+" �bm�chte deine Insel besuchen");
		if(GUIManager.getGUI(receiver) == null) {
			GUIManager.openGUI(receiver, new IslandVisitGUI(receiver, sender));
		}else {
			Chat.sendClickableVisitingMessage(receiver,
					Prefixes.REQUEST.px()+"Nutze �7/accept �bzum �aannehmen",
					"�bKlicke um �aakzeptieren",
					"/accept",
					false,
					false);
			Chat.sendClickableVisitingMessage(receiver,
					Prefixes.REQUEST.px()+"Nutze �7/deny �bzum �cablehnen",
					"�bKlicke um �cablehnen",
					"/deny",
					false,
					false);
		}
		registerRequest(this);
	}

	@Override
	public boolean accept() {
		if(hasAccepted == false) {			
			this.hasAccepted = true;
			
			sender.sendMessage(Prefixes.REQUEST.px()+"�e"+receiver.getName()+" �bhat deine Anfrage �aangenommen");
			receiver.sendMessage(Prefixes.REQUEST.px()+"Du hast die Anfrage von �e"+sender.getName()+" �aangenommen");
			SkyBlockCommandFunction.visitIsland(sender, receiver);
			RequestManager.removeRequest(this);
			GUIManager.openGUI(receiver, new VisitAcceptedGUI(receiver));
		}
		return false;
	}

	@Override
	public boolean deny() {
		if(hasDenied == false) {			
			this.hasDenied = true;
			
			sender.sendMessage(Prefixes.REQUEST.px()+"�e"+receiver.getName()+" �bhat deine Anfrage �cabgelehnt");
			receiver.sendMessage(Prefixes.REQUEST.px()+"Du hast die Anfrage von �e"+sender.getName()+" �cabgelehnt");
			RequestManager.removeRequest(this);
			GUIManager.openGUI(receiver, new VisitDeniedGUI(receiver));
		}
		return false;
	}

}
