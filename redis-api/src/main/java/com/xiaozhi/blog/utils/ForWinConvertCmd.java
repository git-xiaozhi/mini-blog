package com.xiaozhi.blog.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.im4java.core.ConvertCmd;


public class ForWinConvertCmd extends ConvertCmd {
	private static Log logger = LogFactory.getLog(ForWinConvertCmd.class);
	public ForWinConvertCmd() {
		super();
		this.initForWin();
	}

	public ForWinConvertCmd(boolean useGM) {
		super(useGM);
		this.initForWin();
	}

	@SuppressWarnings("unchecked")
	protected void initForWin() {

		if (System.getProperty("os.name").startsWith("Windows")) {
			
			try {
				Field field = this.getClass().getSuperclass().getSuperclass()
						.getDeclaredField("iCommands");
				field.setAccessible(true);
				List<String> value = (List<String>) field.get(this);
				value.addAll(0, Arrays.asList(new String[] { "cmd", "/C" }));
			} catch (Exception e) {
				logger.debug("######################################"+e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}
}