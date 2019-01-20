package nmo.config;

import java.util.TreeMap;
import com.google.gson.annotations.Expose;
import nmo.Configuration;

public class NMOUsers
{
	public static NMOUsers INSTANCE;

	public static void load() throws Exception
	{
		INSTANCE = Configuration.load(NMOUsers.class, "users.json");
	}

	public static void save() throws Exception
	{
		Configuration.save(INSTANCE, "users.json");
	}

	@Expose
	public TreeMap<String, String> users = new TreeMap<>();
}
