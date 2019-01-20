package nmo.integration.webui;

import java.util.Random;
import nl.captcha.text.producer.TextProducer;

public class PredictableCaptchaTextProducer implements TextProducer
{

	private final Random rand;
	private final int _length = 5;
	private final char[] _srcChars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'r', 'w', 'x', 'y', '2', '3', '4', '5', '6', '7', '8', };

	public PredictableCaptchaTextProducer(int seed)
	{
		this.rand = new Random(seed);
	}

	@Override
	public String getText()
	{
		String capText = "";
		for (int i = 0; i < this._length; i++)
		{
			capText += this._srcChars[this.rand.nextInt(this._srcChars.length)];
		}
		return capText;
	}
}