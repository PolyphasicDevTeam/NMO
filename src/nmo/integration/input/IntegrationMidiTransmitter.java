package nmo.integration.input;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import org.apache.logging.log4j.Logger;
import nmo.ActivitySource;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;

public class IntegrationMidiTransmitter extends Integration
{
	public IntegrationMidiTransmitter()
	{
		super("midiTransmitter");
	}

	public static final IntegrationMidiTransmitter INSTANCE = new IntegrationMidiTransmitter();
	public static final ActivitySource MIDI_TRANSMITTER = new ActivitySource("midiTransmitter");
	private static final Logger log = LogWrapper.getLogger();
	int transmitterLoop = -1;

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.midiTransmitter.enabled;
	}

	@Override
	public void init()
	{

	}

	@Override
	public void update() throws Exception
	{
		this.transmitterLoop++;
		if (this.transmitterLoop % 600 == 0)
		{
			MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
			for (int i = 0; i < infos.length; i++)
			{
				MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
				if (NMOConfiguration.INSTANCE.integrations.midiTransmitter.transmitters.contains(device.getDeviceInfo().getName()))
				{
					if (!device.isOpen() && device.getMaxTransmitters() != 0)
					{
						final String name = device.getDeviceInfo().getName() + "/" + device.getDeviceInfo().getDescription() + "/" + device.getDeviceInfo().getVendor();
						log.info("Connected MIDI device: " + name);
						device.getTransmitter().setReceiver(new Receiver()
						{
							@Override
							public void send(MidiMessage message, long timeStamp)
							{
								MainDialog.resetActivityTimer(MIDI_TRANSMITTER);
							}

							@Override
							public void close()
							{
								log.info("Disconnected MIDI device: " + name);
							}
						});
						device.open();
					}
				}
			}
		}
	}

	@Override
	public void shutdown()
	{

	}
}
