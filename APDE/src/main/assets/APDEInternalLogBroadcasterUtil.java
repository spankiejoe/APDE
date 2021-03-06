public class APDEInternalLogBroadcasterUtil {
	public static class APDEInternalExceptionHandler implements Thread.UncaughtExceptionHandler {
		private Thread.UncaughtExceptionHandler defaultHandler;
		
		private android.app.Activity context;
		
		public APDEInternalExceptionHandler(android.app.Activity context) {
			defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
			
			this.context = context;
		}
		
		public void uncaughtException(Thread t, Throwable e) {
			e.printStackTrace();
			APDEInternalBroadcastMessage(e.getMessage(), 'x', e.getClass().getName(), context);
			defaultHandler.uncaughtException(t, e);
		}
	}
	
	public static class APDEInternalConsoleStream extends java.io.OutputStream {
		private final byte single[] = new byte[1];
		private final char severity;
		
		private android.app.Activity context;
		
		public APDEInternalConsoleStream(char severity, android.app.Activity context) {
			this.severity = severity;
			
			this.context = context;
		}
		
		@Override
		public void close() {}
		@Override
		public void flush() {}
		
		@Override
		public void write(byte b[]) {
			write(b, 0, b.length);
		}
		
		@Override
		public void write(byte b[], int offset, int length) {
			APDEInternalBroadcastMessage(new String(b, offset, length), severity, "", context);
		}
		
		@Override
		public void write(int b) {
			single[0] = (byte) b;
			write(single, 0, 1);
		}
	}
	
	public static void APDEInternalBroadcastMessage(final String message, final char severity, final String exception, final android.app.Activity context) {
		context.runOnUiThread(new Runnable() {
			public void run() {
				android.content.Intent intent = new android.content.Intent();
				intent.setAction("com.calsignlabs.apde.LogBroadcast");
				intent.putExtra("com.calsignlabs.apde.LogSeverity", severity);
				intent.putExtra("com.calsignlabs.apde.LogMessage", message);
				intent.putExtra("com.calsignlabs.apde.LogException", exception);
				context.sendBroadcast(intent);
			}
		});
	}
}
