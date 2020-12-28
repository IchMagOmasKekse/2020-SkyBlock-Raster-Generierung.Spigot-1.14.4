package me.crafttale.de.tablist;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;

public class TablistManager {
	
	private static Scoreboard sb;
	private static Team t_default;
	private static Team t_donor1;
	private static Team t_donor2;
	private static Team t_donor3;
	private static Team t_donor4;
	private static Team t_epic;
	private static Team t_supporter;
	private static Team t_admin;
	private static Team t_developer;
	
	public static String pxToPlayernameSeperator = " ";
	
	private static ConcurrentHashMap<Player, Integer> givenTeam = new ConcurrentHashMap<Player, Integer>();
	
	private static int getTeam(Player p) {
		if(p.hasPermission("skyblock.tablist.developer")) return 0;
		else if(p.hasPermission("skyblock.tablist.admin")) return 1;
		else if(p.hasPermission("skyblock.tablist.supporter")) return 2;
		else if(p.hasPermission("skyblock.tablist.default")) return 3;
		else if(p.hasPermission("skyblock.tablist.donor1")) return 4;
		else if(p.hasPermission("skyblock.tablist.donor2")) return 5;
		else if(p.hasPermission("skyblock.tablist.donor3")) return 6;
		else if(p.hasPermission("skyblock.tablist.donor4")) return 7;
		else if(p.hasPermission("skyblock.tablist.epic")) return 8;
		else return 3;
	}
	public static String getTeamPrefix(Player p) {
		switch(getTeam(p)) {
		case 0: return t_developer.getPrefix();
		case 1: return t_admin.getPrefix();
		case 2: return t_supporter.getPrefix();
		case 3: return t_default.getPrefix();
		case 4: return t_donor1.getPrefix();
		case 5: return t_donor2.getPrefix();
		case 6: return t_donor3.getPrefix();
		case 7: return t_donor4.getPrefix();
		case 8: return t_epic.getPrefix();
			default: return t_default.getPrefix()+" !NON-RANK! ";
		}
	}
	public static String getTeamSuffix(Player p) {
		switch(getTeam(p)) {
		case 0: return t_developer.getSuffix();
		case 1: return t_admin.getSuffix();
		case 2: return t_supporter.getSuffix();
		case 3: return t_default.getSuffix();
		case 4: return t_donor1.getSuffix();
		case 5: return t_donor2.getSuffix();
		case 6: return t_donor3.getSuffix();
		case 7: return t_donor4.getSuffix();
		case 8: return t_epic.getSuffix();
		default: return t_default.getSuffix()+" !NON-RANK! ";
		}
	}
	public static void unset() {
		for(Player p : givenTeam.keySet()) removeTeam(p, givenTeam.get(p));
	}
	public static void reloadAllPlayers() {
		unset();
		for(Player p : Bukkit.getOnlinePlayers()) setTablist(p);
	}
	private static void removeTeam(Player p, int index) {
		switch(index) {
		case 0:
			p.setPlayerListName(p.getPlayerListName().replace(t_developer.getPrefix()+pxToPlayernameSeperator, ""));
			t_developer.removeEntry(p.getName()); break;
		case 1:
			p.setPlayerListName(p.getPlayerListName().replace(t_admin.getPrefix()+pxToPlayernameSeperator, ""));
			t_admin.removeEntry(p.getName()); break;
		case 2:
			p.setPlayerListName(p.getPlayerListName().replace(t_supporter.getPrefix()+pxToPlayernameSeperator, ""));
			t_supporter.removeEntry(p.getName()); break;
		case 3:
			p.setPlayerListName(p.getPlayerListName().replace(t_default.getPrefix()+pxToPlayernameSeperator, ""));
			t_default.removeEntry(p.getName()); break;
		case 4:
			p.setPlayerListName(p.getPlayerListName().replace(t_donor1.getPrefix()+pxToPlayernameSeperator, ""));
			t_donor1.removeEntry(p.getName()); break;
		case 5:
			p.setPlayerListName(p.getPlayerListName().replace(t_donor2.getPrefix()+pxToPlayernameSeperator, ""));
			t_donor2.removeEntry(p.getName()); break;
		case 6:
			p.setPlayerListName(p.getPlayerListName().replace(t_donor3.getPrefix()+pxToPlayernameSeperator, ""));
			t_donor3.removeEntry(p.getName()); break;
		case 7:
			p.setPlayerListName(p.getPlayerListName().replace(t_donor4.getPrefix()+pxToPlayernameSeperator, ""));
			t_donor4.removeEntry(p.getName()); break;
		case 8:
			p.setPlayerListName(p.getPlayerListName().replace(t_epic.getPrefix()+pxToPlayernameSeperator, ""));
			t_epic.removeEntry(p.getName()); break;
		}
		givenTeam.remove(p, index);
		p.setPlayerListName("§8§m"+p.getName());
	}
	
	public static void editTablist(Player p) {
	    int i = getTeam(p);
	    if(givenTeam.containsKey(p) == false || givenTeam.get(p) != i) {
	    	if(givenTeam.containsKey(p))removeTeam(p, givenTeam.get(p));
	    	switch (i) {
	    	case 0:
	    		t_developer.addEntry(p.getName());
	    		p.setPlayerListName(t_developer.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_developer+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 1:
	    		t_admin.addEntry(p.getName());
	    		p.setPlayerListName(t_admin.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_admin+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 2:
	    		t_supporter.addEntry(p.getName());
	    		p.setPlayerListName(t_supporter.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_supporter+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 3:
	    		t_default.addEntry(p.getName());
	    		p.setPlayerListName(t_default.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_default+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 4:
	    		t_donor1.addEntry(p.getName());
	    		p.setPlayerListName(t_donor1.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_donor1+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 5:
	    		t_donor2.addEntry(p.getName());
	    		p.setPlayerListName(t_donor2.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_donor2+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 6:
	    		t_donor3.addEntry(p.getName());
	    		p.setPlayerListName(t_donor3.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_donor3+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 7:
	    		t_donor4.addEntry(p.getName());
	    		p.setPlayerListName(t_donor4.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_donor4+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	case 8:
	    		t_epic.addEntry(p.getName());
	    		p.setPlayerListName(t_epic.getPrefix()+pxToPlayernameSeperator+p.getPlayerListName().replace(p.getName(), Settings.suffix_epic+p.getName()));
	    		givenTeam.put(p, i);
	    		break;
	    	default:
	    		break;
	    	}
	    	p.setScoreboard(sb);
	    }
	}
	public TablistManager() {
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		t_default = sb.registerNewTeam("default");
		t_donor1 = sb.registerNewTeam("donor1");
		t_donor2 = sb.registerNewTeam("donor2");
		t_donor3 = sb.registerNewTeam("donor3");
		t_donor4 = sb.registerNewTeam("donor4");
		t_epic = sb.registerNewTeam("epic");
		t_supporter = sb.registerNewTeam("supporter");
		t_admin = sb.registerNewTeam("admin");
		t_developer = sb.registerNewTeam("developer");
		
		t_default.setPrefix(Settings.prefix_default);
		t_donor1.setPrefix(Settings.prefix_donor1);
		t_donor2.setPrefix(Settings.prefix_donor2);
		t_donor3.setPrefix(Settings.prefix_donor3);
		t_donor4.setPrefix(Settings.prefix_donor4);
		t_epic.setPrefix(Settings.prefix_epic);
		t_supporter.setPrefix(Settings.prefix_supporter);
		t_admin.setPrefix(Settings.prefix_admin);
		t_developer.setPrefix(Settings.prefix_developer);
		
		t_default.setSuffix(Settings.suffix_default);
		t_donor1.setSuffix(Settings.suffix_donor1);
		t_donor2.setSuffix(Settings.suffix_donor2);
		t_donor3.setSuffix(Settings.suffix_donor3);
		t_donor4.setSuffix(Settings.suffix_donor4);
		t_epic.setSuffix(Settings.suffix_epic);
		t_supporter.setSuffix(Settings.suffix_supporter);
		t_admin.setSuffix(Settings.suffix_admin);
		t_developer.setSuffix(Settings.suffix_developer);
		//you can add as many as you want
	}
	
	/**
	 * Setzt die Tablist für einen Spieler
	 * @param p
	 */
	public static void setTablist(Player p) {
//		if(p.isOp()) p.setPlayerListName("§cAdmin §7"+p.getName());
//		else p.setPlayerListName("§aSpieler §7"+p.getName());
		if(sb != null) editTablist(p);
		p.setPlayerListHeaderFooter("\n§6§lC R A F T T A L E . D E\n§7§m--§8§m==§8[§9§o BETA RELEASE! §8]§m==§7§m--\n\n§b"+SkyBlock.getCurrentDate()+"\n§3"+SkyBlock.getCurrentTime()+" Uhr", "\n§6www.crafttale.de\n");
	}
	
}
