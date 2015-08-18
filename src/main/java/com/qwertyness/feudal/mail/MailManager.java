package com.qwertyness.feudal.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.qwertyness.feudal.Configuration;

public class MailManager implements Listener {
	private List<Mail> mail;
	
	public MailManager() {
		this.mail = new ArrayList<Mail>();
	}
	
	public void addMail(Mail mail) {
		this.mail.add(mail);
		List<Mail> mailClone = new ArrayList<Mail>(this.mail);
		if (!(mail instanceof MailDeliverer)) {
			List<Mail> relevantMail = new ArrayList<Mail>();
			for (int i = 0;i < mailClone.size();i++) {
				Mail mailDeliverer = mailClone.get(i);
				if (mail.recipiant.getUniqueId().toString().compareTo(mailDeliverer.recipiant.getUniqueId().toString()) == 0) {
					if (mailDeliverer.getClass().isAssignableFrom(MailDeliverer.class)) {
						mailDeliverer.destroy();
					}
					else {
						relevantMail.add(mail);
					}
				}
			}
			Mail mailDeliverer = new MailDeliverer(Arrays.asList(Configuration.instance.messages.prefix + "You have " + relevantMail.size() + " mail.",
					Configuration.instance.messages.prefix + "Type /mail <index (0 through amount - 1)> to read mail or /mail delete to delete all mail."),
					(Player)mail.recipiant, false, relevantMail);
			mailDeliverer.sendMessages((Player)mail.recipiant);
			this.mail.add(mailDeliverer);
		}
	}
	public void removeMail(Mail mail) {
		for (int i = 0;i < this.mail.size();i++) {
			Mail m = this.mail.get(i);
			if (mail.equals(m)) {
				this.mail.remove(i);
			}
		}
	}
	public List<Mail> getMail() {
		return this.mail;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		List<Mail> relevantMail = new ArrayList<Mail>();
		for (Mail mail : this.mail) {
			if (mail.compareRecipiant(event.getPlayer())) {
				relevantMail.add(mail);
			}
		}
		Mail mailDeliverer = new MailDeliverer(Arrays.asList(Configuration.instance.messages.prefix + "You have " + relevantMail.size() + " mail.",
				Configuration.instance.messages.prefix + "Type /mail <index (0 through amount - 1)> to read mail or /mail delete to delete all mail."),
				event.getPlayer(), false, relevantMail);
		mailDeliverer.sendMessages(event.getPlayer());
		this.addMail(mailDeliverer);
	}
	
	public class MailDeliverer extends CommandMail {
		private List<Mail> mail;

		public MailDeliverer(List<String> messages, Player recipiant, boolean recurring, List<Mail> mail) {
			super("mail", new String[] {"delete"}, recipiant, messages, recurring);
			
			this.mail = mail;
		}

		@Override
		public void run(String[] args) {
			Player player = Bukkit.getPlayer(this.getRecipiant().getUniqueId());
			if (args.length < 1) {
				if (player != null) {
					this.sendMessages(player);
				}
				return;
			}
			try {
				int index = Integer.parseInt(args[0]);
				if (index >= this.mail.size()) {
					if (player != null) {
						player.sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.invalidIndex);;
					}
					return;
				}
				Mail mail = this.mail.get(index);
				mail.sendMessages((Player)recipiant);
			}
			catch (NumberFormatException e) {
				if (args[0].equalsIgnoreCase("delete")) {
					for (Mail mail : this.mail) {
						mail.destroy();
					}
					this.mail.clear();
					this.mailMessages = Arrays.asList(Configuration.instance.messages.prefix + "You have " + this.mail.size() + " mail.",
							Configuration.instance.messages.prefix + "Type /mail <index (0 through amount - 1)> to read mail or /mail delete to delete all mail.");
					((Player)recipiant).sendMessage(Configuration.instance.messages.prefix + "You have deleted your mail.");
				}
			}
		}
		
	}
	
	public boolean checkCommandMail(String command, String[] args, CommandSender sender) {
		for (int i = 0;i < this.mail.size();i++) {
			if (!(this.mail.get(i) instanceof CommandMail)) {
				return false;
			}
			CommandMail commandMail = (CommandMail) this.mail.get(i);
			if (commandMail.getCommand().equalsIgnoreCase(command) && commandMail.compareSubcommands(args) && commandMail.compareRecipiant(sender)) {
				commandMail.runWithDestroy(args);
				return true;
			}
		}
		return false;
	}
}
