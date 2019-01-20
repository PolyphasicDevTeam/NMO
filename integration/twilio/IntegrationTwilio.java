package nmo.integration.twilio;

import java.net.URI;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import nmo.Action;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.config.NMOConfiguration;

public class IntegrationTwilio extends Integration
{
	public IntegrationTwilio()
	{
		super("twilio");
	}

	public static final IntegrationTwilio INSTANCE = new IntegrationTwilio();
	private static final Logger log = LogWrapper.getLogger();

	public void call(String fromS, String toS)
	{
		TwilioRestClient client = new TwilioRestClient.Builder(NMOConfiguration.INSTANCE.integrations.twilio.accountSID, NMOConfiguration.INSTANCE.integrations.twilio.authToken).build();
		PhoneNumber from = new PhoneNumber(fromS);
		PhoneNumber to = new PhoneNumber(toS);
		URI uri = URI.create(NMOConfiguration.INSTANCE.integrations.twilio.callingURI);
		Call call = Call.creator(to, from, uri).create(client);
		log.info("Call from " + fromS + " to " + toS + " executed with SID " + call.getSid());
	}

	public void call(StoredPhoneNumber number)
	{
		this.call(NMOConfiguration.INSTANCE.integrations.twilio.numberFrom, number.number);
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.twilio.enabled;
	}

	@Override
	public void init()
	{
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.twilio.phoneNumbers.length; i++)
		{
			final StoredPhoneNumber number = NMOConfiguration.INSTANCE.integrations.twilio.phoneNumbers[i];
			this.actions.put("/twilio/" + i, new Action()
			{
				@Override
				public String getName()
				{
					return "CALL " + number.name + ": " + number.number;
				}

				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationTwilio.this.call(number);
				}

				@Override
				public String getDescription()
				{
					return "Makes an automated phone call to " + number.name + " (" + number.number + ").";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return number.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return number.secret;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return number.secret;
				}
			});
		}
	}

	@Override
	public void update() throws Exception
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shutdown()
	{
		// TODO Auto-generated method stub
	}
}