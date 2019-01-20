package nmo.integration.discord;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import nmo.Action;
import nmo.CommonUtils;
import nmo.Integration;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;

public class IntegrationDiscord extends Integration
{
	public static final IntegrationDiscord INSTANCE = new IntegrationDiscord();
	static JDA jda = null;
	static String lastMessage = "";

	public IntegrationDiscord()
	{
		super("discord");
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.discord.enabled;
	}

	@Override
	public void init() throws Exception
	{
		if (CommonUtils.isNullOrEmpty(NMOConfiguration.INSTANCE.integrations.discord.authToken))
		{
			throw new Exception("You need to specify discord authToken in the configuration file in order for discord integration to work");
		}
		jda = new JDABuilder(AccountType.CLIENT).setToken(NMOConfiguration.INSTANCE.integrations.discord.authToken).buildBlocking();
		jda.getPresence().setGame(Game.of("NMO"));
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.discord.messages.length; i++)
		{
			final SendableMessage message = NMOConfiguration.INSTANCE.integrations.discord.messages[i];
			this.actions.put("/discord/" + i, new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationDiscord.this.send(message, parameters);
				}

				@Override
				public String getName()
				{
					return "SEND " + message.name;
				}

				@Override
				public String getDescription()
				{
					return message.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return true;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return true;
				}
			});
		}
	}

	@Override
	public void update() throws Exception
	{
		if (!lastMessage.equals(MainDialog.scheduleStatusShort))
		{
			lastMessage = MainDialog.scheduleStatusShort;
			jda.getPresence().setGame(Game.of(MainDialog.scheduleStatusShort));
		}
	}

	@Override
	public void shutdown() throws Exception
	{
		if (jda != null)
		{
			jda.shutdown();
		}
	}

	public void send(SendableMessage message, Map<String, String[]> parameters)
	{
		if (jda != null)
		{
			String actualMessage = "`NMO` <:nmo:341127544113987585> " + message.message;
			// replace context
			String[] context = parameters == null ? null : parameters.get("context");
			if (context != null)
			{
				actualMessage = actualMessage.replaceAll("\\Q{context}\\E", StringUtils.join(context, " "));
			}
			switch (message.targetType)
			{
			case SERVER:
				TextChannel channel = jda.getTextChannelById(message.targetID);
				if (channel != null)
				{
					channel.sendMessage(actualMessage).complete();
				}
				break;
			case GROUP:
				Group group = jda.asClient().getGroupById(message.targetID);
				if (group != null)
				{
					group.sendMessage(actualMessage).complete();
				}
				break;
			case USER:
				User user = jda.getUserById(message.targetID);
				if (user != null)
				{
					PrivateChannel pchannel = user.openPrivateChannel().complete();
					pchannel.sendMessage(actualMessage).complete();
				}
				break;
			}
		}
	}
}
