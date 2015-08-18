package com.qwertyness.feudal.mail;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.qwertyness.feudal.Feudal;

public class Mail {
	protected List<String> mailMessages;
	protected OfflinePlayer recipiant;
	protected boolean recurring;

	public Mail(List<String> mailMessages, OfflinePlayer recipiant, boolean recurring) {
		this.recipiant = recipiant;
		this.recurring = recurring;
		
		if (mailMessages == null) {
			this.mailMessages = new ArrayList<String>();
		}
		else {
			this.mailMessages = mailMessages;
		}
	}
	
	public List<String> getMessages() {
		return this.mailMessages;
	}
	
	public void sendMessages(Player player) {
		for (String message : this.mailMessages) {
			player.sendMessage(message);
		}
		if (!recurring && !(this instanceof CommandMail)) {
			this.destroy();
		}
	}
	
	public OfflinePlayer getRecipiant() {
		return this.recipiant;
	}
	
	public boolean compareRecipiant(CommandSender sender) {
		if (this.recipiant == null) {
			return true;
		}
		if (!(sender instanceof OfflinePlayer)) {
			return false;
		}
		OfflinePlayer offlinePlayer = (OfflinePlayer) sender;
		if (offlinePlayer.getUniqueId().toString().equals(this.recipiant.getUniqueId().toString())) {
			return true;
		}
		return false;
	}
	
	public void destroy() {
		Feudal.getInstance().getMailManager().removeMail(this);
	}
}
