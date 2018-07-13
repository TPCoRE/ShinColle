import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.UUID;

import com.shincolle.nclient.NClient;

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
					System.exit(0);
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
	 * Launch a nclient safely, false means launch faild
	 * */
	public static final boolean safeLaunch(UUID uuid) {
		try {
			new NClient(uuid);
			return true;
		} catch(Throwable e) {
			return false;
		}
	}
}
