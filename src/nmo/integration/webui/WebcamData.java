package nmo.integration.webui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.Logger;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import nmo.LogWrapper;

public class WebcamData implements WebcamListener
{
	private static final Logger log = LogWrapper.getLogger();
	public volatile String imageBase64 = "";
	public final String cc;
	public Webcam webcam;
	BufferedImage image;
	public int faultyCameraCorrection = 0;
	public String name;

	public WebcamData(String cc, Webcam webcam)
	{
		this.cc = cc;
		this.webcam = webcam;
		this.name = this.webcam.getName();
	}

	@Override
	public void webcamImageObtained(WebcamEvent we)
	{
		if (this.image == null)
		{
			return;
		}
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(this.image, "JPG", baos);
			byte[] data = baos.toByteArray();
			this.imageBase64 = new String(Base64.getEncoder().encode(data), "UTF8");
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void webcamOpen(WebcamEvent we)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void webcamClosed(WebcamEvent we)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void webcamDisposed(WebcamEvent we)
	{
		// TODO Auto-generated method stub
	}

	public final ArrayList<WebcamWebSocketHandler> socketHandlers = new ArrayList<>();

	public synchronized WebcamWebSocketHandler[] getConnections()
	{
		return this.socketHandlers.toArray(new WebcamWebSocketHandler[0]);
	}

	public synchronized int count()
	{
		return this.socketHandlers.size();
	}
}
