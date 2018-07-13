import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * ShinColle Starter
 * */
public final class SC$Starter {
	
	/**
	 * Starter
	 * */
	public static final void premain(String arg, Instrumentation inst) throws Throwable {
		System.out.println("ShinColle -> Starts(Version: Alter-0.0.0)!");
		
		Class.forName("SC$CodeRuler");
		inst.addTransformer(new ClassFileTransformer() {
			
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				try {
					return SC$CodeRuler.codefix(classfileBuffer, className);
				} catch(Throwable e) {
					e.printStackTrace();
					SC$Starter.exit(0);
				}
				
				return null;
			}
		}, true);
		
		System.out.println("ShinColle -> Retransform!");
		
		//回滚加载过的classes
		Class[] cs = inst.getAllLoadedClasses();
		for(Class c : cs) {
			if(inst.isModifiableClass(c)) inst.retransformClasses(c);
		}
		
		System.out.println("ShinColle -> Listening!");
	}
	
	/**
	 * Shutdown
	 * */
	public static final void exit(int status) {
		try {
			try {
				Class.forName("net.minecraft.client.Minecraft").getMethod("shutdownMinecraftApplet").invoke(null);
			} catch(Throwable e) {}
			
			System.exit(status);
		} catch(Throwable e) {
			try {
				Method shutdown = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", int.class);
				shutdown.setAccessible(true);
				shutdown.invoke(null, status);
			} catch(Throwable ie) {
				ie.addSuppressed(e);
				throw new RuntimeException("No idea!", ie);
			}
		}
	}
}
