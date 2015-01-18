package com.edu.StudentShare;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import sun.util.logging.resources.logging;

public class AdapterLog extends  Logger
{

	@Override
	public void addHandler(Handler handler) throws SecurityException {
		// TODO Auto-generated method stub
		super.addHandler(handler);
	}

	@Override
	public void log(Level level, String msg, Object param1) {
		
		super.log(level, msg, param1);
	}

	protected AdapterLog(String name, String resourceBundleName) {
		
		super(name, resourceBundleName);
	}

}
